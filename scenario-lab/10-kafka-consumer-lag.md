# Kafka Consumer Lag — lag climbs while CPU looks idle

## Incident

`fulfillment-consumer` lag on topic `orders.paid` grows from seconds to hours after a “harmless” change: enrichment now calls a fraud HTTP API **inside** `poll` processing, with a 5 s timeout. Consumer group has 6 instances, 12 partitions. CPU per pod is low. Disk and GC fine. Ops tries adding consumers — lag barely improves.

## Symptoms

- `records-lag-max` monotonic up during business hours
- `poll` interval warnings / max.poll.interval violations → rebalances
- Processing rate (msg/s) far below produce rate
- Many threads blocked on HTTP; Kafka client thread unhealthy
- Adding consumers past partition count does nothing

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| Kafka | Consumer `max.poll.records=500`, `max.poll.interval.ms=300000` |
| Concurrency | One consumer thread per instance; processing sync in `poll` loop |
| Fraud API | p95 ~800 ms, p99 ~4 s during the incident (illustrative) |
| Produce rate | ~2k msg/s (illustrative) |

## Metrics

```
kafka.consumer.lag.max        5e3 → 2e6
consume.rate                  400 msg/s (need ~2000)
fraud.http.p99                ~4s
rebalance.count               elevated
cpu                           ~15%
```

Illustrative: lag + slow dependency + rebalances ⇒ processing budget broken.

## Logs

```
2026-08-03T20:11:02.001Z WARN  Consumer - poll took 312s > max.poll.interval; leaving group
2026-08-03T20:11:02.040Z INFO  ConsumerCoordinator - Member ... sending LeaveGroup
2026-08-03T20:11:15.220Z INFO  FraudClient - status=200 elapsed=3920ms order=...
2026-08-03T20:11:40.000Z WARN  Consumer - rebalance in progress; lag grows
```

## Initial Hypotheses

1. Per-message blocking HTTP destroys throughput (500 × seconds)
2. Rebalance storm from max.poll.interval breaches worsens lag
3. Underscaled partitions / consumers (but partition count may already bind)
4. Serialization / GC issue (CPU idle argues against)
5. Producer spike only (check produce vs consume rates)

## Questions

- Why doesn’t “scale to 30 consumers” help with 12 partitions?
- What do you measure first: lag, process time per record, or GC?
- What assumption does batching `max.poll.records=500` make when each record does I/O?
- What if produce rate ×100 — how do you protect the system?
- Batch fraud checks vs per-message — trade-offs?

## Investigation

1. **Lag + rates**  
   Compare produce rate vs consume rate; lag is integral of the deficit.

2. **Time per record**  
   Histograms around fraud call; multiply by `max.poll.records`.

3. **Rebalance correlation**  
   Lag stairs up when members leave from poll interval violations.

4. **Thread dump**  
   Consumer threads in `FraudClient` / `socketRead` ([performance-engineering/tools/jstack.md](../performance-engineering/tools/jstack.md)).

5. **Config review**  
   `max.poll.records`, `max.poll.interval.ms`, pause/resume patterns.

6. **Partition math**  
   Max useful consumers = partitions (per group).

7. **Backpressure design**  
   Consider async processing with bounded queues carefully (ordering/commit semantics).

## Root Cause

Synchronous fraud HTTP in the poll loop made average handling time too high to keep up with produce rate. Large `max.poll.records` batches amplified time-between-polls past `max.poll.interval.ms`, causing leave/rebalance loops that further reduced effective processing. Extra consumer instances beyond 12 partitions could not raise parallelism.

## Resolution

- **Immediate:** raise interval carefully **or** cut `max.poll.records`; feature-flag fraud off / sample; scale fraud API; pause producer if needed.
- **Proper:** batch fraud requests; parallelize within partition only if ordering allows; separate “consume quickly, process with bounded workers,” committing carefully; or move fraud earlier in the pipeline.
- Ensure consumer count ≤ partitions unless using cooperative protocols with other patterns.

## Prevention

- SLO: consume rate ≥ produce rate with headroom
- Synthetic lag alerts with process-time budgets
- Load test consumers with dependency delays injected
- Review any new I/O in poll loops as a capacity change

## Principal Engineer Discussion

- Ordering vs throughput: per-partition single-thread vs parallel with keys.
- Where should fraud live in the order lifecycle?
- Kafka exactly-once / transactional commit with external side effects — pitfalls (see also scenario 08).
- When is lag “OK” (overnight batch) vs SEV?

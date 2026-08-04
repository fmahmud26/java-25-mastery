# Low-Latency Architecture (JVM Services)

Design so **tail latency** stays inside an SLO under realistic load — not chasing nanoseconds for CRUD APIs.

## Mental Model

```text
Request budget (e.g. 100ms p99)
  − queueing  − GC/safepoint  − locks  − downstream  − your code
  = remaining slack (often tiny)
```

Every wait multiplies under fan-out. Optimize the budget, not a microbenchmark in isolation.

## Building Blocks

| Lever | Why it moves p99 |
|-------|------------------|
| Allocation discipline | Fewer young GC storms |
| GC choice / heap headroom | Pause distribution |
| Contended locks / single queues | Convoy latency |
| Queueing ahead of the service | Little’s Law — depth × service time |
| Downstream timeouts + bulkheads | Cap foreign tails |
| Warmup / avoid deopt loops | Cold-path spikes |
| Batching | Helps throughput; **hurts** single-op latency — know the trade |

## Mechanical Sympathy (interview depth, not HFT cosplay)

- Cache-line false sharing on hot counters → pad or use `LongAdder` / striped counters ([false-sharing Q](../java-interview-questions/performance/q02-false-sharing.md))  
- Prefer sequential memory access; avoid huge pointer-chasing graphs on hot paths  
- Busy-spin / thread affinity / off-heap: only when measured need and ops expertise exist  
- Most product services get more from **fewer allocations + less contention + ZGC/G1 fit** than from CPU pinning  

## Production Scenario — checkout p99 cliff

p50 fine, p99 spikes every few minutes. JFR shows GC pauses aligning with spikes; alloc profiler shows JSON tree per item in cart loop.

**Fix:** stream/reuse buffers; bound cart size; verify pause histogram; only then consider collector change.

## Production Scenario — “we need low latency so we batch”

Team batches DB writes every 50ms to raise TPS. Single-order latency jumps; support tickets rise. Throughput SLO met, latency SLO burned.

**Fix:** separate latency-sensitive path from bulk path; or smaller batches with deadline flush.

## When Not to Chase Low Latency

- Admin/batch jobs  
- Already p99 ≪ SLO with headroom  
- Network RTT dominates (optimize the remote or cache, not your hashmap)  

## Trade-offs

| Aggressive low-latency | Complexity, CPU (spin), ops burden, brittle code |
|------------------------|--------------------------------------------------|
| Simple VT + pools | Readable; good enough for most I/O services |

## Principal Decision

State the **percentile and budget**, name the top three budget consumers (with evidence), and refuse optimizations that do not move that percentile.

### Related

[tail-latency.md](./tail-latency.md) · [high-throughput-systems.md](./high-throughput-systems.md) · [../garbage-collection/trade-offs.md](../garbage-collection/trade-offs.md) · [jvm-observability.md](./jvm-observability.md)

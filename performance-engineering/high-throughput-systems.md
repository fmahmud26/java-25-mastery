# High-Throughput Systems (JVM)

Maximize **successful work per second** without destroying latency SLOs or melting dependencies.

## Mental Model

```text
Ingress → admit/shed → bounded concurrency → work → egress
              ↑                    ↑
         backpressure         pools / VT caps
```

Throughput is a **system** property: CPU, GC, locks, disk, DB, and downstream quotas. Local microbench wins often vanish at the integration boundary.

## Levers

| Lever | Effect | Risk |
|-------|--------|------|
| More concurrency (VT / pools) | Higher occupancy | Stampede on DB/PSP |
| Batching / pipelining | Amortize overhead | Latency, partial failure |
| Connection / object pooling | Cut setup cost | Stale connections, leaks |
| Caching | Cut remote work | Stale reads, stampede |
| Partitioning / sharding | Scale write path | Hot keys, rebalancing |
| Async handoff (queue) | Smooth bursts | Lag, dual-write bugs |
| Zero-copy / fewer allocs | More CPU for useful work | Complexity |

## Virtual Threads and Throughput

VT raise how many **blocked** tasks you can hold. They do not raise DB QPS. Cap in-flight work per dependency; see [when-vt-do-not-help](../virtual-threads/when-vt-do-not-help.md).

## Production Scenario — event pipeline

Consumers at 2k msg/s; after VT rewrite, in-flight explodes, Postgres saturates, success throughput **falls**.

**Fix:** semaphore / pool sized to DB; measure successful commits/s, not started tasks/s.

## Production Scenario — write path at 100×

Black Friday: need 100× ingest. Options: horizontal scale + partitions; batch inserts; spill to log then async materialize; shed non-critical traffic.

Principal question: *Which writes are synchronous to the user, and which can be durable-async?*

## Failure Modes

| Failure | Signal |
|---------|--------|
| Thrashing concurrency | High CPU + low success RPS |
| GC under alloc storm | Rising GC time fraction |
| Lock serialization | One hot monitor; flat scaling curve |
| Retry amplification | Dependency RPS ≫ useful work |
| Unbounded queue | Lag hours; OOM |

## Measure Protocol

1. Define success (HTTP 2xx + idempotent business commit).  
2. Ramp concurrency; plot success RPS vs p99.  
3. Find the **knee**; operate left of the cliff with headroom.  
4. Change one variable; re-measure ([scientific-method](./scientific-method.md)).  

## When Not to Maximize Throughput

- Latency-critical single operations (prefer low-latency path)  
- Exactly-once / strongly consistent ledgers where correctness caps batching  
- When dependency quotas are the real ceiling — negotiate capacity, don’t spin threads  

## Principal Decision

At design review: show the **bottleneck hypothesis**, the admit/shed policy, and the dashboard that proves success throughput under stress — not a claim that “we use virtual threads so we scale.”

### Related

[throughput.md](./throughput.md) · [low-latency-architecture.md](./low-latency-architecture.md) · [../system-design/distributed-systems/backpressure.md](../system-design/distributed-systems/backpressure.md) · [../reactive-programming/backpressure.md](../reactive-programming/backpressure.md)

# Latency

**Definition:** time until a useful response. Talk **percentiles** (p50/p95/p99), never averages alone.

## Budgeting (Principal habit)

Break an SLO into parts:

```text
p99 ≤ 200ms end-to-end
  edge/LB: 5ms
  app: 20ms
  cache: 2ms hit / 0 on miss path accounting
  DB: 30ms
  dependency: 80ms
  margin/GC/queue: rest
```

If one dependency is 150ms p99, you **cannot** meet 200ms without parallelizing, caching, or async.

## Sync vs async

| Path | Use when |
|------|----------|
| Sync | User needs answer now (redirect URL, pay result) |
| Async | Work can complete later (email, analytics, thumbnails) |

Don’t put Kafka on the redirect critical path unless you must.

## Tail latency amplifiers

- Head-of-line blocking, lock contention  
- GC pauses, noisy neighbors  
- Retry amplification (see retry storms)  
- Large payloads / unbounded fan-out  
- Cross-region RTTs (~50–150ms+)  

Mitigations: hedging (carefully), timeouts, isolation pools, smaller blast radius, regional affinity.

## Caching and latency

A 95% hit rate at 1ms with 5% misses at 50ms → average ≠ p99. Quote **hit and miss path** SLOs separately.

Related: [throughput.md](./throughput.md), [caching.md](./caching.md), [observability.md](./observability.md).

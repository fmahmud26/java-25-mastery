# Contention

Multiple threads competing for locks, monitors, or single-consumer structures — latency rises while CPU may look low.

## Mental Model

```text
Runnable wait for CPU  ≠  BLOCKED wait for monitor
Throughput collapses when critical sections serialize work
```

## How to Measure

| Tool | Signal |
|------|--------|
| `jstack` / `jcmd Thread.print` | Many BLOCKED on same lock |
| JFR | Java Monitor Blocked / Wait events |
| async-profiler wall-clock | Off-CPU time |
| Application metrics | Queue depth, lock wait timers |

## Common Java shapes

- Synchronized maps/services on request path  
- Single connection / single queue consumer  
- Log appender locks  
- Fair locks under high concurrency  
- False sharing (harder — needs careful experiments)

## Experiment sketch

Baseline p99 + JFR monitors → shard lock / concurrent structure / remove lock from I/O → re-measure p99 and CPU.

## Claim template

“At 4k RPS, JFR showed 35% samples in MonitorBlocked on `OrderService`; after striping, MonitorBlocked negligible; p99 180→40ms; CPU +15% (expected — more useful work).”

### Related

[thread-analysis.md](./thread-analysis.md) · [jstack.md](./jstack.md) · [cpu-profiling.md](./cpu-profiling.md)

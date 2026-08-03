# Concurrency — Performance

## Cost model (rules of thumb)

| Mechanism | Typical cost / cliff |
|-----------|----------------------|
| Uncontended `synchronized` | Cheap on modern HotSpot |
| Contended lock | Context switches, queueing, p99 latency |
| `volatile` read/write | Barriers; usually fine for flags |
| CAS retry storms | Hot counters → prefer `LongAdder` |
| Oversized platform pool | Thrashing; little CPU gain |
| Undersized pool + blocking | Queue blow-up / timeouts |
| Shared `HashMap` | Data races; never “faster” than CHM |

## Scalability cliffs

1. **Single global lock** — all requests serialize.
2. **Hot CHM key** — one bin/key updated by everyone.
3. **Lock held across I/O** — amplifies contention (and VT pinning historically).
4. **Too many platform threads** — memory + scheduling overhead.
5. **False sharing** — atomics/pads on same cache line (advanced).
6. **Allocation under lock** — longer critical sections.

## Measurement

| Signal | Tool |
|--------|------|
| `BLOCKED` / deadlocks | `jstack` / `jcmd Thread.print` / thread dump |
| Hot locks / methods | async-profiler (lock / cpu), JFR |
| Pool saturation | queue depth, active count, rejection metrics |
| CHM / atomic hotspots | flame graphs in `ConcurrentHashMap` / `AbstractQueuedSynchronizer` |

Related: [../../performance-engineering](../../performance-engineering/), [cas.md](../../concurrency/cas.md).

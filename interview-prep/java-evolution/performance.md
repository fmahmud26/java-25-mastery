# Java Evolution — Performance

New features are not free — know the wins and the traps.

## Typical wins

| Feature | Perf angle |
|---------|------------|
| Records | Less boilerplate; immutable sharing; still objects (not value types) |
| Virtual threads | Huge concurrency for blocking I/O; not a CPU speedup |
| Pattern matching | Clarity; negligible vs I/O; may enable better null-safety |
| Sequenced collections | Better APIs; same underlying structures |
| Modern GC (ZGC/G1 tuning) | Tail latency — upgrade + tune, don’t assume defaults forever |

## Pitfalls

1. Expecting virtual threads to speed CPU-bound loops.  
2. Allocating millions of records/sec without pooling/reuse where needed.  
3. Migrating JDK but keeping pin-heavy synchronized + blocking libraries unmeasured.  
4. Enabling experimental/preview in prod without rollout discipline.

## Measurement

- Baseline on old LTS vs new LTS: throughput, p99, RSS, GC pauses (JFR).  
- For Loom: track carrier utilization and pinning JFR events.

Related: [performance.md](../../16-java-25-features/performance.md), [../../virtual-threads](../../virtual-threads/).

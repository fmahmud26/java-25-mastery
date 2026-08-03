# JVM — Real-World Usage

| Scenario | Practice | Why |
|----------|----------|-----|
| Service heap sizing | Set `-Xms≈-Xmx` after load test | Avoid resize storms |
| Plugins / hot reload | Bounded classloaders; discard + GC | Prevent metaspace leaks |
| Containers | Respect cgroup memory; don’t overcommit `-Xmx` | OOM-kill vs Java heap OOM |
| Latency SLOs | Prefer ZGC/Shenandoah when pauses dominate | Tail latency |
| Throughput batch | Parallel/G1 often fine | Amortize GC vs CPU |
| Diagnostics always-on | JFR continuous / on-demand | Cheap flight recorder |

## Production rules of thumb

- Heap dump on OOM for Java heap; for Metaspace OOM hunt **loaders**, not `-Xmx`.
- Treat “RSS high, heap fine” as **native** until proven otherwise (NMT, direct buffers, threads).
- Don’t tune JIT flags first — fix allocation and lock contention.
- Java 25: prefer FFM over ad-hoc JNI when you control the boundary.

Related: [../../modern-java-engineering](../../modern-java-engineering/), [jvm-architecture.md](../../jvm-internals/jvm-architecture.md).

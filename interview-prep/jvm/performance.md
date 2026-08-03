# JVM — Performance

## Cost centers interviewers care about

| Area | Symptom | Typical cause |
|------|---------|---------------|
| Allocation rate | GC churn, young GC | Short-lived objects, autoboxing |
| JIT warm-up | Slow first minutes | Cold code still interpreted / C1 |
| Deopts | Latency spikes | Speculative fail (class change, uncommon trap) |
| Safepoints | Time-to-safepoint (TTSP) | Long counted loops without polls (rarer now) |
| Metaspace | Native growth | Classloader leaks (redeploy / generated classes) |
| Native | RSS ≫ heap | Direct buffers, JNI, threads, code cache |

## Escape analysis impact

| If object escapes | If not |
|-------------------|--------|
| True heap alloc (+ header) | May scalar-replace / eliminate alloc |
| Visible in allocation profiles | “Missing” allocs that source suggests |

Don’t claim EA in production without profiles — it’s an *optimization*.

## Measurement

- **JFR**: Allocation, Compilation, Safepoint, Class Loading  
- **async-profiler**: CPU + alloc flame graphs  
- **`jstat` / `jcmd GC.heap_info`**: heap occupancy trends  
- **NMT**: native categories  

Related: [../../performance-engineering](../../performance-engineering/), [jit-compiler.md](../../jvm-internals/jit-compiler.md).

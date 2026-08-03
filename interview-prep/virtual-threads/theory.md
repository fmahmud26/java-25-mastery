# Virtual Threads — Theory

## Contracts

| Trait | Virtual thread | Platform thread |
|-------|----------------|-----------------|
| Mapping | Many-to-few onto **carriers** | 1:1 OS thread |
| Cost | Very cheap | Expensive (stack + OS) |
| Scale | Millions blocked tasks feasible | Thousands |
| Best for | Concurrent **blocking I/O** | CPU-bound / scarce native resources |
| Pooling | **Create per task** — don’t pool | Pool and reuse |
| Blocking | Usually **unmounts** carrier | Occupies OS thread |

Same programming model: `Thread`, `ExecutorService`, `Future`, stack traces.

## Mental model

```text
many virtual threads  ──mount/unmount──►  few carrier (platform) threads  ──►  OS
```

- Goal: restore **thread-per-request** scalability without forcing reactive code.
- JVM schedules VTs on a **dedicated** ForkJoin-style carrier pool — **not** `ForkJoinPool.commonPool()`.
- **Pinning** = VT cannot unmount while blocked → carrier stuck → scalability cliff (version-dependent causes).

## When *not* to use (interview shortlist)

| Situation | Prefer |
|-----------|--------|
| Pure CPU crunch | Sized platform pool / FJP / parallel streams |
| Need thread-local affinity / scarce native TLS | Platform threads carefully |
| Legacy code with long lock + blocking I/O | Redesign critical sections (lock-over-I/O); on JDK 25 don’t recite synchronized-pin lore as primary |
| “Pool of 200 virtual threads” | Wrong — use per-task executor |

## Structured concurrency (Java 25)

`StructuredTaskScope` is a **preview** API (JEP 505 in JDK 25): treat forked subtasks as one unit — join, cancel, error propagation. Pairs naturally with virtual threads. Requires `--enable-preview`; API still evolving (factories + `Joiner`).

Related chapter: [virtual-threads.md](../../virtual-threads/virtual-threads.md), [platform-threads.md](../../virtual-threads/platform-threads.md), [thread-pinning.md](../../virtual-threads/thread-pinning.md).

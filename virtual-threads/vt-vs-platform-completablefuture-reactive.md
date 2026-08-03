# Compare: Platform Threads vs Virtual Threads vs CompletableFuture vs Reactive

## Comparison Matrix

| | Platform threads | Virtual threads | CompletableFuture | Reactive (WebFlux etc.) |
|--|------------------|-----------------|-------------------|-------------------------|
| Style | Sync blocking | Sync blocking | Async callbacks/chains | Async operators |
| Concurrency scale | Thousands | Massive blocked tasks | Task/thread-pool bound | Event-loop efficient |
| Debug stacks | Simple | Simple | Harder | Hardest |
| CPU-bound | Good with pools | Don’t oversubscribe | OK | OK |
| Blocking libs | Needs threads | Natural fit | Must wrap/offload | Must wrap/offload |
| Backpressure | Queue/pool | Must add explicitly | Manual | First-class |
| Best for | CPU, carriers | I/O-bound services | Composition/fan-in | Streaming, backpressure pipelines |

## Mental Models

```text
Platform:  1 OS thread per running/blocked task (pooled)
Virtual:   M:N blocked tasks to carriers
CF:        async stages on executors; non-blocking preferred
Reactive:  async + operators + backpressure; event loops
```

## Code Sketches

```java
// Platform
Executors.newFixedThreadPool(8).submit(cpuTask);

// Virtual
Executors.newVirtualThreadPerTaskExecutor().submit(blockingIoTask);

// CompletableFuture
CompletableFuture.supplyAsync(this::callA, exec)
        .thenCombine(CompletableFuture.supplyAsync(this::callB, exec), this::merge);

// Reactive (conceptual)
Mono.zip(repo.find(id), client.enrich(id), this::merge);
```

## Choosing (PE)

| Situation | Prefer |
|-----------|--------|
| Blocking JDBC monolith/service | **VT** |
| Heavy CPU workers | **Platform pool** |
| Complex async composition with existing CF | CF (optionally on VT executor) |
| End-to-end streaming with backpressure | **Reactive** |
| Mixed | VT for request I/O + platform for CPU + reactive only where streaming needed |

## Migration Note

Don’t rewrite stable WebFlux to VT “for fashion.” Do adopt VT for new blocking services and when simplifying async wrappers around JDBC.

## Interview Questions

- VT vs reactive one-liner?  
- Can CF run on VT executor?  
- When keep WebFlux?

### Related

[virtual-threads-vs-reactive-programming.md](./virtual-threads-vs-reactive-programming.md) · [principal-architecture-decisions.md](./principal-architecture-decisions.md)

# Platform Threads

OS-backed threads (`Thread.ofPlatform()` / classic `new Thread`). One platform thread ≈ one kernel schedulable entity (implementation-dependent carrier model aside from VT).

## Mental Model

```text
platform thread ↔ OS thread ↔ expensive, limited count (~hundreds–thousands)
virtual thread  ↔ JVM-scheduled, mounts on carriers (see VT materials)
```

This folder emphasizes platform-thread pools for CPU-bound and classic server models.

## Internal Mechanics

Stack memory reserved per platform thread; context switch involves kernel. Blocking a platform thread blocks that OS thread. Pool reuse amortizes create cost.

## Code

```java
Thread t = Thread.ofPlatform()
        .name("payment-worker-", 0)
        .daemon(false)
        .start(() -> processJob());

try (var pool = Executors.newFixedThreadPool(8)) {
    pool.submit(this::cpuBoundScore);
}
```

## Production Scenario — high-traffic APIs

CPU scoring / crypto: fixed platform pool sized to cores. Blocking JDBC on large platform pools → thread explosion; prefer bounded pools + queue or VT for blocking I/O.

## Failure Scenario

`newCachedThreadPool()` under traffic spike → tens of thousands of platform threads → native OOM / death.

## Debugging Strategy

`jstack` / thread dump: count `RUNNABLE` vs `BLOCKED` vs `WAITING`. Too many threads → pool misconfig.

## Performance

Good for CPU-bound parallelism (≈ cores). Poor for 1-thread-per-request blocking I/O at huge scale.

## Trade-offs

Predictable OS scheduling vs cost. VT for mass blocking concurrency; platform for tight CPU and when pinning/thread-local/legacy libs require it.

## Interview Questions

- Platform vs virtual thread?  
- How do you size a fixed pool for CPU work?

## Principal-Level Discussion

Separate pools by workload (CPU vs blocking vs event). Never one unbounded pool for everything. Document thread-name prefixes for dumps.

### Related

[thread-lifecycle.md](./thread-lifecycle.md) · [executor-service.md](./executor-service.md) · [creating-threads.md](./creating-threads.md)

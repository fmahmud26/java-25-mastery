# Interview — Virtual Threads (Java 25)

Staff/Principal depth. Pair with [scenarios.md](./scenarios.md) and [principal-architecture-decisions.md](./principal-architecture-decisions.md).

---

## L1 — Definitions

**Why virtual threads?**  
Make thread-per-request scalable for blocking I/O without forcing reactive style.

**Platform vs virtual vs carrier?**  
- **Platform:** 1:1 with OS thread; expensive  
- **Virtual:** JVM-scheduled lightweight thread; mounts on carrier to run  
- **Carrier:** platform thread that currently executes a VT  

**Does blocking unmount?**  
Usually yes for JDK blocking APIs (`LockSupport.park`, socket I/O, `sleep`, etc.) — carrier free for other VTs.

---

## L2 — Mechanics

**JVM scheduling?**  
M:N — many VTs, few carriers from the **dedicated virtual-thread scheduler** (ForkJoinPool-*style* work-stealing). That is **not** `ForkJoinPool.commonPool()` (which serves parallel streams / default CF async). Mount → run → block → unmount → another VT mounts.

**Pinning (history + Java 25)?**  
Historically: blocking inside `synchronized` pinned VT to carrier. **JEP 491 (Java 24+)** largely removes pinning for `synchronized`. Still: don’t hold locks across I/O; native frames / some cases can still constrain. Prefer short critical sections; use `ReentrantLock` carefully (locks also shouldn’t wrap I/O).

**Pool VTs?**  
**No.** `Executors.newVirtualThreadPerTaskExecutor()`. Pool scarce resources (DB, HTTP, CPU).

**ThreadLocals?**  
Work but amplify memory at huge scale; prefer `ScopedValue` for immutable request context where possible.

---

## L3 — Systems Thinking

**10K concurrent HTTP — does VT help?**  
Yes for thread scarcity; no for vendor RPS / DB pool / memory without admission control.

**DB pool exhaustion after VT?**  
Expected if you open the floodgates. Throughput ≈ `poolSize / queryLatency`. VT makes waiters cheap, not connections free.

**VT vs CompletableFuture vs Reactive?**  
| Style | When |
|-------|------|
| VT | Blocking I/O services, readable stacks |
| CF | Async composition; can run on VT executor |
| Reactive | Streaming + backpressure pipelines |

**When VT does NOT help?**  
CPU-bound; slow SQL; rate limits; lock-over-I/O; already saturated deps; unbounded admission → stampede.

---

## L4 — Principal Scenarios

**“p99 unchanged after enabling VT”**  
Dependency was the limit. VT improved OS efficiency, not latency. Next: SQL, indexes, caching, capacity.

**“Errors spiked after VT”**  
Lost implicit concurrency cap of platform pool. Add max in-flight + per-dep bulkheads + timeouts.

**Architecture decision one-liner**  
VT for waiting; platform pools for CPU; size connection pools for the fleet; never confuse cheap threads with free capacity.

---

## Rapid-Fire Bank

| Q | A |
|---|---|
| Carrier count roughly? | ~ available processors (implementation detail; not infinite) |
| `Thread.sleep` on VT? | Parks / releases carrier |
| Hikari to 10k? | No — kill the DB |
| Spring MVC + VT vs WebFlux? | Prefer MVC+VT for blocking stacks unless need reactive |
| Structured concurrency? | **Preview** in Java 25 (JEP 505) — `StructuredTaskScope`; needs `--enable-preview`; idea: fork/join related tasks as one unit |
| Memory free? | No — heap stacks + request state |
| Pinning monitor on Java 25? | Much less with JEP 491; still design for short locks |
| Stampede control? | Semaphore / rate limit / circuit breaker / load shed |

---

## Live Coding Sketch

```java
try (var vts = Executors.newVirtualThreadPerTaskExecutor()) {
    var f1 = vts.submit(() -> httpClient.send(reqA, BodyHandlers.ofString()));
    var f2 = vts.submit(() -> dataSource.getConnection()); // still pool-bound!
    // Prefer timeouts + try-with-resources on connections
}
```

Discuss: connection still from pool of 40; VT doesn’t enlarge Hikari.

### Related

[vt-vs-platform-completablefuture-reactive.md](./vt-vs-platform-completablefuture-reactive.md) · [when-vt-do-not-help.md](./when-vt-do-not-help.md) · [experiments.md](./experiments.md)

# Concurrency — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| HTTP request handling (blocking I/O) | Virtual thread per request | Scales without reactive rewrite |
| CPU compress / crypto | Fixed platform pool (~cores) | VT doesn’t add cores |
| Shared in-process cache | `ConcurrentHashMap` | Concurrent reads/writes |
| Request-local mutable map | Plain `HashMap` | No sharing → no sync tax |
| Feature flag / shutdown | `volatile` boolean | Visibility without mutex |
| Metrics counters | `LongAdder` / Micrometer | Less CAS contention |
| Limit outbound connections | `Semaphore` | Backpressure |
| Fan-in N async jobs | Latch / structured scope (preview) | Clear completion |

## Production rules of thumb

- Prefer **immutability / confinement** over shared mutable state.
- Never expose a live unsynchronized `HashMap` from a singleton.
- Keep critical sections short — no remote I/O under locks.
- Document lock order if you nest locks.
- Size platform pools from cores + measured blocking; don’t guess “200 threads.”
- On Java 25: VT for I/O concurrency; platform pools for CPU; CHM for shared maps.

Related: [../../modern-java-engineering](../../modern-java-engineering/), [../../virtual-threads](../../virtual-threads/).

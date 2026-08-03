# Virtual Threads — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Spring MVC / servlet request | Enable VT executor | Thread-per-request scales |
| JDBC / blocking HTTP clients | Run on VTs | Familiar blocking code |
| Image compress / crypto | Platform pool | CPU-bound |
| Fan-out to 50 microservices | VT + `Semaphore`/bulkhead | Cheap tasks, bounded deps |
| Shared cache mutations | Short locks / CHM; I/O outside | Avoid pin / contention |
| Replace WebFlux? | Only if stack is blocking-friendly | Reactive still valid for event-loop ecosystems |

## Production rules of thumb

- **Don’t pool** virtual threads; use `newVirtualThreadPerTaskExecutor()`.
- Cap concurrency at the **resource** (DB pool size, HTTP client limits), not by starving threads.
- Keep monitors short; prefer fetch-then-publish over lock-during-I/O.
- Separate **I/O orchestration (VT)** from **CPU (platform)**.
- Treat structured concurrency as **preview** on Java 25 — fine for experiments; gate in prod until final.
- Observe carriers/pinning under load before declaring victory.

Related: [virtual-threads-vs-reactive-programming.md](../../virtual-threads/virtual-threads-vs-reactive-programming.md), [../../modern-java-engineering](../../modern-java-engineering/).

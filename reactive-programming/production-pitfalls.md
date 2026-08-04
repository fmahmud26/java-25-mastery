# Reactive Production Pitfalls

Patterns that look “scalable” in demos and fail under load.

## 1. Blocking on the Event Loop

**Symptom:** Latency cliff; event-loop threads in `RUNNABLE` inside JDBC/driver; other requests starve.

**Cause:** Sync JDBC, file I/O, `Thread.sleep`, or CPU-heavy work on Netty/reactor-http threads.

**Fix:** Hop to a bounded worker pool (`publishOn` / bounded elastic); or run blocking adapters on **virtual threads** with a hard concurrency cap. Never “just block — it’s only 2ms.”

## 2. Unbounded flatMap

**Symptom:** Connection pool exhaustion, downstream 429s, then retry storms.

**Cause:** `flatMap` without concurrency cap fans out one inner subscription per element.

**Fix:** Cap concurrency; apply bulkheads per dependency; shed load at ingress.

## 3. Unbounded Buffer “for safety”

**Symptom:** Rising old-gen / humongous; Full GC or OOM; lag metrics look “fine” until death.

**Cause:** `onBackpressureBuffer()` without bound / overflow policy.

**Fix:** Bound + drop/error/latest; alert on drop rate; see [backpressure.md](./backpressure.md).

## 4. Retry Amplification

**Symptom:** Dependency already sick; traffic × (1 + retries) finishes it off.

**Cause:** Eager `retry` / `retryWhen` without jittered backoff, budget, or idempotency.

**Fix:** Retry only safe operations; budget per request; circuit breaker; align with [networking/retry-storms.md](../networking/retry-storms.md).

## 5. Context / MDC Loss

**Symptom:** Logs lack `requestId`; traces break across operators.

**Cause:** Thread-locals do not follow async hops; VT ThreadLocals also need care at scale.

**Fix:** Propagate context explicitly (Reactor Context, baggage, OpenTelemetry); prefer structured correlation over ThreadLocal.

## 6. subscribe() Without Lifecycle

**Symptom:** Leaked subscriptions; duplicate consumers; “works in test, doubles in prod.”

**Cause:** Fire-and-forget `subscribe()` in web filters without dispose on cancel.

**Fix:** Tie subscription to request cancellation; use framework adapters (WebFlux) that manage lifecycle.

## 7. Hot vs Cold Confusion

**Symptom:** Late subscribers miss data; or reconnect storms re-run expensive cold sources.

**Cause:** Treating a shared hot Flux like a cold DB query (or vice versa).

**Fix:** Document source semantics; use `share`/`publish` deliberately; cache only with TTL and invalidation.

## Failure Investigation Loop

```text
Symptom (latency / OOM / pool wait)
  → Is event-loop blocked? (thread dump)
  → Is in-flight concurrency unbounded? (metrics)
  → Is buffer growing? (heap / queue depth)
  → Are retries amplifying? (dependency RED)
  → Fix one layer; re-measure
```

## When Reactive Is Still Right

Streaming APIs, complex fan-in with cancel, teams fluent in the stack, and backpressure that must live **inside** the pipeline — not only at the thread pool.

## Principal Perspective

Most “reactive incidents” are **resource-control** incidents wearing an operator costume. Bound concurrency, place blocking correctly, and own overflow policy.

### Related

[operators-and-pipelines.md](./operators-and-pipelines.md) · [vs-virtual-threads.md](./vs-virtual-threads.md) · [interview.md](./interview.md)

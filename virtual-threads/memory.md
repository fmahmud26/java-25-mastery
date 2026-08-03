# Memory and Virtual Threads

VTs are cheap relative to platform threads — **not free**.

## Problem

Need 10K–100K concurrent blocked request handlers. Platform stacks blow RSS. Naive: “virtual threads cost zero.”

## Mental Model

```text
Platform thread: large fixed stack reservation (order of MB-ish OS-dependent)
Virtual thread:  heap-allocated stack chunks that grow/shrink
```

## Cost Drivers

| Cost | Notes |
|------|-------|
| Per-VT metadata + stack chunks | Millions ⇒ significant heap |
| ThreadLocals | Amplified with huge **in-flight** VT counts if each holds large context — clear/avoid; prefer Scoped Values |
| Scoped Values (JEP 506 final) | Designed for immutable bound context — **not** the same cost profile as ThreadLocal caches |
| Per-task objects | Request DTOs, buffers dominate often |
| Carriers | Few platform stacks |

## Code — avoid ThreadLocal explosion

```java
// Risky at huge scale — legacy MDC/ThreadLocal heavy frameworks
// Prefer ScopedValue (Java 25 final) where applicable for immutable context
ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
ScopedValue.where(REQUEST_ID, id).run(() -> handle());
```

## Production Scenario — 10K concurrent requests

Memory grows with concurrent in-flight requests (bodies, sessions), not only VT count. Cap in-flight with load shedding. Measure RSS before/after enabling VT under same admitted concurrency.

## Failure Scenario

Unbounded VT create + large ThreadLocals (security context copies) → GC thrash / OOM. Symptom: heap full of stacks/ThreadLocalMaps while CPU low.

## Trade-offs

VT wins vs platform on blocked concurrency per GB. Still lose if each request holds megabyte buffers. Optimize payloads and caps first when RSS dominates.

## Experiment Ideas

See [experiments.md](./experiments.md): measure RSS / heap for N blocked platform vs virtual threads.

## When memory is the limiter

- Unbounded admission  
- Large ThreadLocals  
- Retained response buffers  
- Fan-out creating N VTs × M deps per request without bound  

## Interview / PE

Are VTs free? ThreadLocal concerns? When does memory become the limiter vs DB pool?

### Related

[experiments.md](./experiments.md) · [downstream-limitations.md](./downstream-limitations.md) · [scenarios.md](./scenarios.md)

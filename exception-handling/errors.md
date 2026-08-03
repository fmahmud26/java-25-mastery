# Error

Serious problems usually originating in the JVM or environment (`OutOfMemoryError`, `StackOverflowError`, `NoClassDefFoundError`, …). Subclass of `Throwable`, **not** `Exception`.

## Mental Model

```text
Error ≈ “the platform is in trouble”
Don’t build business recovery on catch(Error)
```

## Production Scenarios

| Scenario | Error-ish signal | Action |
|----------|------------------|--------|
| Huge report | OOM | Kill/fail job; limit payload; scale memory |
| Deep recursion bug | StackOverflow | Fix code; don’t catch and continue |
| Bad deploy/classpath | NoClassDefFoundError | Roll back; fix artifact |

## Bad vs Improved

```java
// Bad
try {
    generateHugeReport();
} catch (OutOfMemoryError e) {
    return Report.empty(); // lies; heap may be corrupted/unstable
}
```

Fail the unit of work; alert; restore capacity. Catching OOM to “return empty” hides outages.

## Strategy

At thread uncaught handlers: log + metric + exit policy for critical workers. Application code: **don’t catch Error**.

## Principal Discussion

Resilience is **load shedding and isolation**, not catching `Error`. Use memory limits, circuit breakers, and bounded queues so you never rely on OOM handlers for control flow.

### Related

[exception-hierarchy.md](./exception-hierarchy.md) · [error-handling-strategy.md](./error-handling-strategy.md)

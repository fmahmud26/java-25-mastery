# Callable

Task returning a value and allowed to throw Exception — `V call()`.

## Mental Model

```text
Callable<V> → Future<V>
success value or ExecutionException
```

## Internal Mechanics

Submitted to `ExecutorService`; result stored in Future. Checked exceptions wrapped when retrieved via `get()`.

## Code

```java
Callable<CaptureResult> task = () -> psp.capture(cmd);
Future<CaptureResult> f = executor.submit(task);
try {
    return f.get(2, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    f.cancel(true);
    throw new PaymentTransientException(cmd.paymentId(), e);
} catch (ExecutionException e) {
    throw new PaymentGatewayException(cmd.paymentId(), e.getCause());
}
```

## Production Scenario — payments

Parallel fraud + capture calls with timeouts via `invokeAll` / separate futures.

## Failure Scenario

Ignoring `ExecutionException` cause → lose root PSP error.

## Debugging Strategy

Always log `getCause()` chain. Timeouts must cancel and have idempotency.

## Performance

Future allocation; wait cost. Prefer structured concurrency patterns when available/preview policies allow.

## Trade-offs

Callable+Future vs reactive vs VT + join.

## Interview Questions

- How are checked exceptions surfaced?  
- invokeAll vs submit loop?

## Principal-Level Discussion

Timeouts without cancel/idempotency → duplicate charges. Design Callable boundaries with payment safety.

### Related

[future.md](./future.md) · [runnable.md](./runnable.md) · [executor-service.md](./executor-service.md)

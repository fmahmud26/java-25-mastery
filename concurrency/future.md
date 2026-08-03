# Future

Handle to an asynchronous result — `get`, `cancel`, `isDone`.

## Mental Model

```text
submit → Future → get (block) / get(timeout) / cancel
```

Classic `Future` is not composable (unlike CompletableFuture — cover there if present elsewhere).

## Internal Mechanics

State machine: running / completed / cancelled. `get` waits; wraps task failures in `ExecutionException`.

## Code

```java
Future<InventoryHold> hold = inventoryExecutor.submit(() -> inventory.reserve(sku, qty));
Future<CaptureResult> cap = paymentExecutor.submit(() -> payments.capture(cmd));
try {
    InventoryHold h = hold.get(200, TimeUnit.MILLISECONDS);
    CaptureResult c = cap.get(2, TimeUnit.SECONDS);
    return finalizeOrder(h, c);
} catch (TimeoutException e) {
    hold.cancel(true);
    cap.cancel(true);
    throw e;
}
```

## Production Scenario — high-traffic APIs

Fan-out to inventory + pricing with deadlines; fail request if budget exceeded.

## Failure Scenario

`get()` without timeout on stuck worker → request thread wedged; cascade latency.

## Debugging Strategy

Dump: request threads WAITING on Future.get; worker BLOCKED — find lock. Always timeout Futures on request paths.

## Performance

Blocking get couples threads — use carefully with platform pools (deadlock if pool exhausted and tasks submit to same pool).

## Trade-offs

Future vs CompletableFuture vs message-driven.

## Interview Questions

- Why can Future deadlock a fixed pool?  
- cancel(true) guarantees?

## Principal-Level Discussion

Same-pool recursive submits are a classic outage. Separate pools or careful async design; timeouts everywhere on RPCs.

### Related

[executor-service.md](./executor-service.md) · [callable.md](./callable.md) · [deadlock.md](./deadlock.md)

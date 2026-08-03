# Runnable

Task that returns nothing and cannot throw checked exceptions — `void run()`.

## Mental Model

```text
Executor.execute(Runnable) / Thread.start
fire-and-forget work unit
```

## Internal Mechanics

Functional interface. Uncaught exceptions in `run` kill the thread / hit handler — they don’t propagate to submitter unless wrapped by Future machinery for `submit`.

## Code

```java
Runnable charge = () -> paymentProcessor.capture(cmd);
executor.execute(charge);

// Prefer submit if you need exception visibility
Future<?> f = executor.submit(charge);
f.get(); // ExecutionException wraps failures
```

## Production Scenario — orders

Async email/notify after order paid — Runnable on a bounded pool (don’t use request thread).

## Failure Scenario

`execute(runnable)` swallowing errors (only thread handler logs) — missed payment notifications with no alert.

## Debugging Strategy

UncaughtExceptionHandler + metrics. Prefer `submit` + central error handling for critical tasks.

## Performance

Zero result allocation vs Callable/Future.

## Trade-offs

Runnable simplicity vs Callable result/error channel.

## Interview Questions

- Runnable vs Callable?  
- Does execute propagate exceptions to caller?

## Principal-Level Discussion

Critical side effects need observability (outbox pattern > fire-and-forget Runnable).

### Related

[callable.md](./callable.md) · [executor-service.md](./executor-service.md) · [future.md](./future.md)

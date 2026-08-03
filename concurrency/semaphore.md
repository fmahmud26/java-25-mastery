# Semaphore

Permits limiting concurrent access — rate/concurrency throttle.

## Mental Model

```text
semaphore(permits)
acquire → enter critical pool of size permits
release → return permit
```

## Internal Mechanics

AQS. Fair optional. `tryAcquire` for shedding.

## Code

```java
Semaphore pspLimit = new Semaphore(20); // max 20 in-flight PSP calls

CaptureResult capture(CaptureCommand cmd) throws InterruptedException {
    if (!pspLimit.tryAcquire(100, TimeUnit.MILLISECONDS)) {
        throw new PaymentTransientException(cmd.paymentId(), "psp saturated");
    }
    try {
        return psp.capture(cmd);
    } finally {
        pspLimit.release();
    }
}
```

## Production Scenario — payments / high-traffic APIs

Protect downstream with bulkhead (max in-flight). Inventory warehouse connection limits.

## Failure Scenario

Forget release → permit leak → permanent saturation. Acquire then blocking forever without tryAcquire.

## Debugging Strategy

Metrics on available permits; dump threads in acquire wait.

## Performance

Throttle improves tail latency of dependency; too tight → user-facing failures (intentional shed).

## Trade-offs

Semaphore vs pool size vs circuit breaker vs queue.

## Interview Questions

- Binary semaphore vs lock?  
- Fair semaphore use?

## Principal-Level Discussion

Semaphores are **bulkheads**. Pair with timeouts, metrics, and load shedding policy — don’t silently queue forever.

### Related

[executor-service.md](./executor-service.md) · [contention.md](./contention.md)

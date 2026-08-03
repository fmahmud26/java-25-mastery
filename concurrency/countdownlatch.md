# CountDownLatch

One-shot gate: threads wait until count reaches zero.

## Mental Model

```text
latch = N
countDown() × N → await() returns
cannot reset
```

## Internal Mechanics

AQS shared mode. `await` waits for count 0; `countDown` releases when last.

## Code

```java
CountDownLatch ready = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {
    executor.execute(() -> {
        warmCache();
        ready.countDown();
    });
}
ready.await(30, TimeUnit.SECONDS); // API starts serving
```

## Production Scenario — high-traffic APIs

Wait for caches/warmers before taking traffic. Payment batch: wait for N validations.

## Failure Scenario

Forgot countDown on error path → await forever. Using latch for reusable cycles → need CyclicBarrier/Phaser.

## Debugging Strategy

WAITING on CountDownLatch; check count via reflection/metrics if instrumented; ensure finally countDown.

## Performance

Lightweight coordination.

## Trade-offs

Latch vs Barrier vs CompletableFuture allOf.

## Interview Questions

- Latch vs CyclicBarrier?  
- Why one-shot?

## Principal-Level Discussion

Always timed await in startup/readiness. Failure to reach zero → fail deploy, don’t hang.

### Related

[cyclicbarrier.md](./cyclicbarrier.md) · [phaser.md](./phaser.md)

# BlockingQueue

Thread-safe handoff with blocking/timed offer/take — backbone of job processing.

## Mental Model

```text
producers → BlockingQueue → consumers
bounded queue = backpressure
```

## Internal Mechanics

`ArrayBlockingQueue` (bounded array), `LinkedBlockingQueue` (optionally bounded), `SynchronousQueue`, `PriorityBlockingQueue`, `DelayQueue`. Putting/taking synchronize via locks/CAS internals.

## Code

```java
BlockingQueue<PaymentJob> q = new ArrayBlockingQueue<>(10_000);

boolean accepted = q.offer(job, 100, TimeUnit.MILLISECONDS);
if (!accepted) {
    metrics.rejected();
    throw new CapacityExceededException();
}

PaymentJob next = q.take(); // worker
```

## Production Scenario — orders / job processing

API enqueues payment capture jobs; workers process with idempotency. Bound the queue; shed load.

## Failure Scenario

Unbounded `LinkedBlockingQueue` → OOM under slow consumers. Consuming without poison-pill shutdown → hang. Dual consumers without idempotency → double capture.

## Debugging Strategy

Queue depth metrics; dump workers in `take`; producer threads blocked in `put`.

## Performance

Bounded array often predictable. Tune worker count vs depth.

## Trade-offs

In-process queue vs Kafka — durability/replay/multi-instance.

## Interview Questions

- put vs offer?  
- Array vs Linked BlockingQueue?  
- Why bound queues?

## Principal-Level Discussion

In-JVM queues are **not** a message bus. Use for decouple within process; use brokers for durable cross-process work.

### Related

[executor-service.md](./executor-service.md) · [concurrent-queues.md](./concurrent-queues.md) · [semaphore.md](./semaphore.md)

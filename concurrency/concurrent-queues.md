# Concurrent Queues (Non-blocking)

`ConcurrentLinkedQueue` / `ConcurrentLinkedDeque` — lock-free multi-producer/consumer queues (no blocking take).

## Mental Model

```text
offer/poll never block waiting for elements
empty poll → null (spin/backoff externally)
```

## Internal Mechanics

Michael-Scott style CAS queues (conceptually). Unbounded. No `take()` blocking.

## Code

```java
ConcurrentLinkedQueue<Event> events = new ConcurrentLinkedQueue<>();
events.offer(event);
Event e = events.poll();
```

## Production Scenario

High-rate telemetry handoff where waiter parks elsewhere; or work stealing structures.

## Failure Scenario

Busy-spin on poll → CPU burn. Unbounded growth → OOM. Using when BlockingQueue semantics needed.

## Debugging / Performance / Trade-offs

Prefer BlockingQueue for worker handoff. ConcurrentLinked* for non-blocking algorithms.

## Interview Questions

- ConcurrentLinkedQueue vs ArrayBlockingQueue?  
- Why no blocking take?

### Related

[blockingqueue.md](./blockingqueue.md) · [cas.md](./cas.md)

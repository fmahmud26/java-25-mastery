# Starvation

A thread never gets needed resources — unfair scheduling, writer blocked by readers, low-priority forever.

## Mental Model

```text
system makes progress overall, but one waiter never runs
```

## Internal Mechanics

Fair locks reduce barging starvation (costly). Reader-heavy RW locks can starve writers. Thread priorities unreliable on JVM for business logic.

## Code

```java
new ReentrantLock(true); // fair — less barging, more overhead
```

## Production Scenario — caches

Constant catalog reads starve refresh writer → stale prices.

## Failure Scenario

Some users’ jobs never run in priority queue without aging.

## Debugging Strategy

Metrics: max wait time for lock; writer wait histograms; fair vs unfair experiments.

## Performance / Trade-offs

Fairness vs throughput. Prefer bounded wait + shedding over silent starvation.

## Interview Questions

- Starvation vs deadlock?  
- Fair ReentrantLock effect?

## Principal-Level Discussion

SLO on wait time; use timeouts; don’t rely on Thread priority for fairness.

### Related

[readwritelock.md](./readwritelock.md) · [contention.md](./contention.md) · [deadlock.md](./deadlock.md)

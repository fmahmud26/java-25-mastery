# Livelock

Threads not blocked but make no progress — keep responding to each other.

## Mental Model

```text
both yield politely forever → no useful work
CPU may be busy
```

## Internal Mechanics

Retry loops without backoff; conflicting conflict-resolution; over-aggressive lock avoidance.

## Code

```java
// Both threads forever give way
while (true) {
    if (tryLockA()) {
        if (tryLockB()) return;
        unlockA();
    }
    // no backoff — livelock risk with peer doing reverse
}
```

## Production Scenario — payments

Two reconciler instances constantly “yielding” leadership without fencing → no settlement progress.

## Failure Scenario

High CPU, no throughput, no BLOCKED in dump (RUNNABLE spinning).

## Debugging Strategy

Dump shows RUNNABLE in retry loops; metrics show zero progress; add backoff/jitter and attempt budgets.

## Performance / Trade-offs

Backoff + jitter; escalate to lock ordering; random sleep.

## Interview Questions

- Livelock vs deadlock?  
- How does dump differ?

## Principal-Level Discussion

Idempotent leaders with leases (DB/ZooKeeper/etc.) beat polite livelock patterns.

### Related

[deadlock.md](./deadlock.md) · [starvation.md](./starvation.md) · [contention.md](./contention.md)

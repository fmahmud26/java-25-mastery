# ReentrantLock

Explicit lock API — same mutual exclusion idea as `synchronized`, with extras.

## Mental Model

```text
lock.lock()
try { critical }
finally { lock.unlock() }   // mandatory
```

Reentrant; optional fairness; `tryLock`; interruptible lock; multiple `Condition`s.

## Internal Mechanics

AQS (AbstractQueuedSynchronizer) queue of waiters. Unfair by default (better throughput). Fair lock reduces barging, more overhead.

## Code

```java
private final ReentrantLock lock = new ReentrantLock();
private final Condition notEmpty = lock.newCondition();

void put(Job j) {
    lock.lock();
    try {
        queue.add(j);
        notEmpty.signal();
    } finally {
        lock.unlock();
    }
}

Job take() throws InterruptedException {
    lock.lockInterruptibly();
    try {
        while (queue.isEmpty()) notEmpty.await();
        return queue.remove();
    } finally {
        lock.unlock();
    }
}
```

## Production Scenario — orders

Try-lock with timeout for non-critical enrichment; fail fast under overload.

```java
if (lock.tryLock(50, TimeUnit.MILLISECONDS)) {
    try { updateInMemoryView(order); }
    finally { lock.unlock(); }
} else {
    metrics.lockTimeout();
}
```

## Failure Scenario

Forgot `unlock` in finally → permanent deadlock-like hang. Lock held across blocking remote calls → pool starvation.

## Debugging Strategy

Dump may show `WAITING` on AQS; `jstack` + `lock` info. Code search for unlock discipline.

## Performance

Unfair ≈ synchronized often. Fair locks slower under contention. Don’t hold across I/O.

## Trade-offs

| Prefer synchronized | Prefer ReentrantLock |
|---------------------|----------------------|
| Simple sections | tryLock / interruptible / Conditions |
| Less boilerplate | Need structured unlock |

## Interview Questions

- synchronized vs ReentrantLock?  
- Fair vs unfair?  
- Condition vs wait/notify?

## Principal-Level Discussion

Use ReentrantLock when you need its features — not as a status symbol. Never lock then call PSP/DB.

### Related

[synchronized.md](./synchronized.md) · [readwritelock.md](./readwritelock.md) · [deadlock.md](./deadlock.md)

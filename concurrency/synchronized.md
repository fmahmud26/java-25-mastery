# synchronized

Language-level mutual exclusion on an object’s **intrinsic lock** (monitor).

## Mental Model

```text
synchronized (lock) { critical section }
enter monitor → exclusive + HB on exit → subsequent enter
reentrant: same thread can re-enter
```

## Internal Mechanics

Monitorenter/monitorexit bytecodes; biased/lightweight/heavy locks historically (HotSpot evolves). Unlock happens-before next lock on same monitor. Wait/notify tied to same monitor.

## Code

```java
public final class Account {
    private final Object lock = new Object();
    private long balanceCents;

    public void deposit(long amount) {
        synchronized (lock) {
            balanceCents += amount;
        }
    }

    public boolean withdraw(long amount) {
        synchronized (lock) {
            if (balanceCents < amount) return false;
            balanceCents -= amount;
            return true;
        }
    }
}
```

Prefer private lock over `synchronized` methods on public `this` (external code can lock your instance).

## Production Scenario — payments

Per-paymentId striped locks for in-memory idempotency state (or CHM); never one JVM-global lock for all payments.

## Failure Scenario

Locking different objects for related fields → race. Nested locks A then B elsewhere B then A → deadlock.

## Debugging Strategy

Dump: `BLOCKED (on object ...)` — identify lock identity; see deadlock.md.

```text
"http-nio-8080-exec-5" #42 BLOCKED
   - waiting to lock <0x00000006a1b2c3d0> (a java.lang.Object)
   - locked <0x...>
```

## Performance

Fine for coarse sections; under high contention use concurrent structures / striping / StampedLock read bias carefully.

## Trade-offs

Simple vs ReentrantLock features (tryLock, interruptible, conditions).

## Interview Questions

- Intrinsic lock vs ReentrantLock?  
- Why private final lock objects?  
- Guarantees of synchronized?

## Principal-Level Discussion

`synchronized` remains valid and clear. Don’t rewrite to ReentrantLock without needing its features. Measure contention before splitting.

### Related

[monitor.md](./monitor.md) · [intrinsic-locks.md](./intrinsic-locks.md) · [reentrantlock.md](./reentrantlock.md) · [deadlock.md](./deadlock.md)

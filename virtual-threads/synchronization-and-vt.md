# Synchronization and Virtual Threads

Locks, monitors, and VT interactions.

## Mental Model

```text
VTs can use synchronized / ReentrantLock / concurrent utilities
Blocking on locks: generally unmounts (with JDK evolution for synchronized)
Never hold locks across slow I/O
```

## Mechanics

- Mutual exclusion still required for shared mutable state — VT ≠ data-race immunity.  
- Java 24+ (JEP 491): synchronized doesn’t pin; wait/notify work with unmount behavior improved.  
- `ReentrantLock.lock()` parking cooperates with VT.  
- Prefer concurrent collections / immutability to reduce lock scope.

## Code

```java
private final ReentrantLock lock = new ReentrantLock();

void update(Order o) {
    lock.lock();
    try {
        state.apply(o); // memory only
    } finally {
        lock.unlock();
    }
    publisher.publish(o); // I/O outside lock
}
```

## Production Scenario — orders

In-memory state machine guarded by lock; persistence/outbox **after** release.

## Failure Scenario

Lock held during PSP HTTP call → serialization of all payments + carrier issues on older JDKs.

## Interview / PE

Do VTs remove the need for synchronization? (No.) Lock + I/O anti-pattern?

### Related

[thread-pinning.md](./thread-pinning.md) · [virtual-threads.md](./virtual-threads.md)

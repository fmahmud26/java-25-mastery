# ReadWriteLock

Allows concurrent readers **or** one writer — good for read-heavy structures.

## Mental Model

```text
many readLock holders  XOR  one writeLock holder
```

## Internal Mechanics

`ReentrantReadWriteLock` — writer preference/unfair options; readers don’t block readers. Write lock exclusivity. Reentrancy rules matter (don’t upgrade read→write naively — deadlock risk).

## Code

```java
private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
private Map<String, Product> catalog = Map.of();

Product get(String sku) {
    rw.readLock().lock();
    try { return catalog.get(sku); }
    finally { rw.readLock().unlock(); }
}

void refresh(Map<String, Product> next) {
    rw.writeLock().lock();
    try { catalog = Map.copyOf(next); }
    finally { rw.writeLock().unlock(); }
}
```

## Production Scenario — caches / product catalog

Frequent reads, rare full refresh. Often simpler: `volatile` immutable map swap without RW lock.

## Failure Scenario

Writer starvation if readers constantly arrive (policy-dependent). Attempting lock upgrade holds read then wants write → deadlock.

## Debugging Strategy

Many readers RUNNABLE/WAITING; writer WAITING long — starvation or heavy read load.

## Performance

Wins when reads dominate and critical section non-trivial. For tiny reads, overhead may exceed benefit vs plain synchronized or immutable publish.

## Trade-offs

RW lock vs immutable snapshot (`volatile`/`AtomicReference`) vs CHM.

## Interview Questions

- When RW lock vs synchronized?  
- Why is lock upgrade dangerous?

## Principal-Level Discussion

Prefer immutable snapshot publish for catalogs when possible — simpler than RW locking.

### Related

[stampedlock.md](./stampedlock.md) · [reentrantlock.md](./reentrantlock.md) · [concurrenthashmap.md](./concurrenthashmap.md)

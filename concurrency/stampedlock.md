# StampedLock

Optimistic reads + conversion — higher throughput potential for read-heavy workloads; **not reentrant**; easier to misuse.

## Mental Model

```text
optimisticRead → validate stamp → maybe fallback to readLock
writeLock returns stamp; unlockWrite(stamp)
```

## Internal Mechanics

Stamp encodes mode/version. Optimistic read doesn’t block writers but must validate. No reentrancy — nested locking deadlocks yourself.

## Code

```java
private final StampedLock sl = new StampedLock();
private long inventory;

long getInventory() {
    long stamp = sl.tryOptimisticRead();
    long v = inventory;
    if (!sl.validate(stamp)) {
        stamp = sl.readLock();
        try { v = inventory; }
        finally { sl.unlockRead(stamp); }
    }
    return v;
}

void setInventory(long v) {
    long stamp = sl.writeLock();
    try { inventory = v; }
    finally { sl.unlockWrite(stamp); }
}
```

## Production Scenario — caches

Hot read path of a numeric gauge / version; writers rare. For maps, CHM usually better.

## Failure Scenario

Forgetting validate; using like reentrant lock; holding optimistic assumptions while calling alien methods.

## Debugging Strategy

Harder than synchronized; favor simpler locks unless profile proves need.

## Performance

Can beat RW locks on read-mostly microbenchmarks — measure in real app.

## Trade-offs

Complexity vs synchronized/CHM. Prefer clarity unless contention profile demands it.

## Interview Questions

- Optimistic read meaning?  
- Why not reentrant?  
- StampedLock vs ReadWriteLock?

## Principal-Level Discussion

StampedLock is an expert tool. Most services never need it — CHM + immutability first.

### Related

[readwritelock.md](./readwritelock.md) · [contention.md](./contention.md)

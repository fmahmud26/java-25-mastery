# CAS (Compare-And-Swap)

Atomic hardware-supported: update only if value still equals expected.

## Mental Model

```text
CAS(expected, new) → success or retry
lock-free progress for single-variable updates
```

## Internal Mechanics

CPU instructions (`CMPXCHG` etc.) via `Unsafe`/`VarHandle` inside `Atomic*`. Failed CAS → retry loop. ABA: value A→B→A fools CAS; mitigate with versioning (`AtomicStampedReference`) or careful design.

## Code

```java
AtomicInteger stock = new AtomicInteger(100);

boolean reserve(int n) {
    for (;;) {
        int cur = stock.get();
        if (cur < n) return false;
        if (stock.compareAndSet(cur, cur - n)) return true;
    }
}
```

## Production Scenario — inventory (single JVM)

Optimistic stock decrement without coarse locking. Multi-node still needs DB/Redis.

## Failure Scenario

Hot CAS loop on one Atomic → CPU spinning (livelock-ish contention); ABA in freelist algorithms.

## Debugging Strategy

Perf: CPU high in Atomic* methods → hot key contention; stripe or lock.

## Performance

Excellent under low-moderate contention; degrades when many cores CAS same location — use LongAdder for stats, striping for hot counters.

## Trade-offs

Lock-free complexity vs ReentrantLock clarity for multi-step invariants.

## Interview Questions

- Explain CAS.  
- What is ABA?  
- CAS vs synchronized?

## Principal-Level Discussion

CAS is the engine under CHM/queues. Product code should use Atomic/CHM APIs, not raw loops, unless building infrastructure.

### Related

[atomic-variables.md](./atomic-variables.md) · [concurrenthashmap.md](./concurrenthashmap.md) · [contention.md](./contention.md)

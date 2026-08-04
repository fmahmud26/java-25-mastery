# CAS (Compare-And-Swap)

Atomic hardware-supported: update only if value still equals expected.

## Mental Model

```text
CAS(expected, new) → success or retry
lock-free progress for single-variable updates
```

## Internal Mechanics

CPU instructions (`CMPXCHG` etc.) exposed through `VarHandle.compareAndSet` / `Atomic*` APIs (historically `Unsafe`). Failed CAS → retry loop. **ABA:** value A→B→A fools CAS; mitigate with versioning (`AtomicStampedReference` / `AtomicMarkableReference`) or careful design.

Volatile read/CAS success create the HB edges libraries rely on — see [happens-before.md](./happens-before.md) and [varhandles.md](./varhandles.md).

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

## Production Scenario — CHM under the hood

`ConcurrentHashMap` uses CAS on bins/nodes for scalable updates — product code uses the map API; CAS is the engine.

## Failure Scenario

Hot CAS loop on one Atomic → CPU spinning (livelock-ish contention); ABA in freelist algorithms; retry loops without backoff under extreme contention.

## Debugging Strategy

Perf: CPU high in Atomic* methods → hot key contention; stripe, LongAdder, or lock. Prove with JFR / async-profiler.

## Performance

Excellent under low-moderate contention; degrades when many cores CAS same location — use LongAdder for stats, striping for hot counters.

## Trade-offs

Lock-free complexity vs ReentrantLock clarity for multi-step invariants. CAS does not make multi-field updates atomic.

## Interview Questions

- Explain CAS.  
- What is ABA?  
- CAS vs synchronized?  
- Why LongAdder for metrics?  

## Principal-Level Discussion

CAS is the engine under CHM/queues. Product code should use Atomic/CHM APIs, not raw loops, unless building infrastructure with litmus tests.

### Related

[atomic-variables.md](./atomic-variables.md) · [concurrenthashmap.md](./concurrenthashmap.md) · [contention.md](./contention.md) · [varhandles.md](./varhandles.md)

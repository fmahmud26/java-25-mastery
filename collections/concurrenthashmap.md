# ConcurrentHashMap

Concurrent hash map (`java.util.concurrent`) — **safe concurrent readers/writers** without locking the entire map on every op (OpenJDK segmented/bin locking / CAS design; Java 8+ node-bin model).

## 1. Mental Model

```text
many threads get/put → CHM
HashMap → data races / corruption under concurrent writers
```

## 2. Internals (engineering view)

| Topic | Behavior |
|-------|----------|
| Structure | Hash bins similar spirit to HashMap; concurrent-safe updates |
| Hashing / buckets / collisions | Hash → bin; lists/trees; concurrent helpers |
| Resizing | Cooperative / concurrent resize (implementation-specific; don’t assume HashMap resize) |
| Nulls | **No null keys or values** |
| Iteration | Weakly consistent — may see updates; **not** fail-fast CME like HashMap |
| Memory | Similar node overhead + concurrency machinery |
| Aggregate ops | `compute`, `merge`, `putIfAbsent` — atomic per key |

Exact locking/CAS details evolve by JDK — interview for **guarantees**, not bit-identical field names.

## 3. Complexity

Average O(1) ops; contention on hot keys can degrade a single bin.

## 4. Code — session storage / rate limiting

```java
public final class SessionStore {
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public Session getOrCreate(String id) {
        return sessions.computeIfAbsent(id, Session::new);
    }

    public void touch(String id) {
        sessions.computeIfPresent(id, (_, s) -> s.touchedNow());
    }
}

// rate limiting counters (illustrative — prefer token bucket + window design)
ConcurrentHashMap<String, LongAdder> hits = new ConcurrentHashMap<>();
hits.computeIfAbsent(userId, _ -> new LongAdder()).increment();
```

## 5. Scenarios

| Scenario | CHM role |
|----------|----------|
| Customer lookup (shared) | In-process index with concurrent traffic |
| Session storage | Per-node sessions + TTL sweeper |
| Rate limiting | Per-key counters |
| Catalog | Often immutable snapshot better than live mutation |

## 6. When CHM instead of HashMap / Hashtable / syncMap?

| vs | Choose CHM |
|----|------------|
| HashMap | Any concurrent writers/readers needing safety |
| `Collections.synchronizedMap` | Better scalability than coarse lock |
| Hashtable | Always prefer CHM in new code |
| Redis | Multi-instance / durability / eviction — external |

## 7. Failure Scenario

| | |
|--|--|
| Symptom | Hot key latency; memory growth; logic bugs with compound non-atomic sequences |
| Cause | Unbounded CHM; read-modify-write outside `compute`; assuming iteration snapshot |
| Fix | Bounds/eviction; use atomic per-key methods; externalize shared state |
| Prevent | Size metrics; load tests; don’t treat CHM as distributed cache |

## 8. Interview (Senior → Principal)

- Why not synchronize HashMap?  
- Null policy difference?  
- Weakly consistent iterators mean what?  
- **Staff:** hot-key contention remedies?  
- **Principal:** CHM sessions vs Redis — consistency, failover, sticky sessions?

### Related

[hashmap.md](./hashmap.md) · [blockingqueue.md](./blockingqueue.md) · [decision-matrix.md](./decision-matrix.md)

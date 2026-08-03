# HashMap

Default `Map` — hash table of key→value. One `null` key; not thread-safe. Java 8+ treeified bins (still the model on Java 25).

## 1. Mental Model

```text
key → spread(hash) → bucket index → Node chain or TreeNode
avg get/put ~ O(1); degraded under bad hashes / huge bins
```

## 2. Internals

| Topic | OpenJDK behavior |
|-------|------------------|
| Data structure | `Node<K,V>[]` table, power-of-two length |
| Hashing | `hashCode` + spread (XOR high bits) |
| Buckets | Index `(n-1) & hash` |
| Collisions | Linked list; **treeify** if bin ≥ ~8 and capacity ≥ 64 |
| Resizing | When `size > capacity × loadFactor` (default **0.75**); capacity ×2; rebin |
| Ordering | Unspecified |
| Iteration | Fail-fast; order not stable |
| Memory | Table + nodes (+ trees); resize allocates large arrays |
| Concurrency | **Unsafe** for concurrent writers |

Detail: [hashing.md](./hashing.md) · [hash-buckets.md](./hash-buckets.md) · [hash-collisions.md](./hash-collisions.md) · [load-factor.md](./load-factor.md) · [rehashing.md](./rehashing.md)

## 3. Complexity

Average O(1) get/put/remove; tree bin O(log n); worst pathological (pre-tree era) O(n).

## 4. Code — customer lookup

```java
public final class CustomerDirectory {
    private final Map<String, Customer> byId;

    public CustomerDirectory(int expected) {
        this.byId = HashMap.newHashMap(expected);
    }

    public Optional<Customer> find(String id) {
        return Optional.ofNullable(byId.get(id));
    }
}
```

## 5. Scenarios

| Scenario | Role of HashMap |
|----------|-----------------|
| Customer lookup | Request-local / single-writer index |
| Product catalog | Build then publish `Map.copyOf` |
| Caching | Only with size bound + eviction (else use proper cache) |
| Session storage | Prefer CHM or external store |

## 6. When HashMap instead of X?

| vs | Choose HashMap when |
|----|---------------------|
| ConcurrentHashMap | Single-threaded or immutable publish |
| TreeMap | No sort/range needed |
| LinkedHashMap | Order irrelevant |
| List scan | Always (for lookup by key) |

## 7. Failure Scenario

| | |
|--|--|
| Symptom | High CPU, lost entries, CME, OOM |
| Causes | Concurrent use; mutable keys; unbounded growth; resize storms |
| Fix | CHM/confine; immutable keys; bound+evict; capacity hint |
| Prevent | Review shared maps; metrics on size; load tests |

## 8. Interview (Senior → Principal)

- Walk get/put (hash → bucket → equals).  
- Why load factor 0.75? Treeify why?  
- **Staff:** millions of entries, GC and resize behavior?  
- **Principal:** global HashMap in a payment service — yes/no and alternatives?

### Related

[concurrenthashmap.md](./concurrenthashmap.md) · [linkedhashmap.md](./linkedhashmap.md) · [equals.md](./equals.md) · [hashcode.md](./hashcode.md) · [decision-matrix.md](./decision-matrix.md)

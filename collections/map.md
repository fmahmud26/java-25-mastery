# Map

Key → value associations. **Not** a `Collection`, but central to real systems (lookup, cache, index).

## 1. Mental Model

```text
customerId ──► Customer
sku         ──► Product
sessionId   ──► SessionState
```

## 2. Contract Highlights

| Method | Role |
|--------|------|
| `get`/`put`/`remove`/`containsKey` | Basic |
| `compute`/`merge`/`putIfAbsent` | Atomic-ish updates (impl-dependent concurrency) |
| `keySet`/`values`/`entrySet` | Views |
| Factories | `Map.of`, `Map.copyOf` |

## 3. Implementations

| Impl | Notes |
|------|-------|
| [HashMap](./hashmap.md) | Default; not concurrent |
| [LinkedHashMap](./linkedhashmap.md) | Insertion/access order |
| [TreeMap](./treemap.md) | Sorted / Navigable |
| [ConcurrentHashMap](./concurrenthashmap.md) | Concurrent |
| Hashtable | Legacy |

## 4. Internals Themes

Hashing, buckets, collisions, load factor, resize — see [hashing.md](./hashing.md) spine and [hashmap.md](./hashmap.md).

## 5. Scenarios

| Scenario | Map choice |
|----------|------------|
| Customer lookup | HashMap / CHM |
| Product catalog | Immutable copy / CHM |
| Caching | LinkedHashMap / external |
| Session storage | CHM + TTL |
| Rate limiting | CHM counters |

## 6. Decision Matrix (maps)

| Need | Choose |
|------|--------|
| Fast unordered | HashMap |
| Concurrent | ConcurrentHashMap |
| LRU building block | LinkedHashMap access-order |
| Range by key | TreeMap |
| Null key | HashMap only (not CHM) |

## 7. Failure Scenario

Shared HashMap across threads → lost updates / infinite loops historically / data corruption. Fix: CHM or confine.

## 8. Interview

- Why Map isn’t Collection?  
- HashMap vs CHM vs TreeMap?  
- **Staff:** resize storms under load?  
- **Principal:** in-process map vs Redis for sessions — criteria?

### Related

[hashmap.md](./hashmap.md) · [decision-matrix.md](./decision-matrix.md) · [concurrenthashmap.md](./concurrenthashmap.md)

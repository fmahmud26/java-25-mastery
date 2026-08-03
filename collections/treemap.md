# TreeMap

Red-black **tree** `NavigableMap` — keys sorted by natural order or `Comparator`.

## 1. Mental Model

```text
sorted keys in a balanced BST
get/put/remove O(log n)
range: subMap, tailMap, headMap
```

## 2. Internals

| Topic | Behavior |
|-------|----------|
| Structure | Red-black tree entries |
| Ordering | Comparator or Comparable keys |
| Hashing | None — comparison based |
| Null keys | Not allowed with natural ordering |
| Iteration | Ascending key order; fail-fast |
| Concurrency | Not thread-safe (`ConcurrentSkipListMap` for concurrent navigable) |
| Memory | Node overhead; no hash table |

## 3. Complexity

O(log n) for get/put/remove; range views O(log n) to bound + O(k) iterate.

## 4. Scenarios

- **Leaderboards** by score key (or better: Redis ZSET at scale).  
- **Time-keyed indexes** in-process (`Instant` → event).  
- Range queries: “all SKUs between A and M”.

```java
NavigableMap<Integer, String> board = new TreeMap<>(Comparator.reverseOrder());
board.put(900, "alice");
board.put(850, "bob");
var top = board.entrySet().stream().limit(10).toList();
```

## 5. When TreeMap instead of HashMap?

| Need | Choice |
|------|--------|
| Pure lookup | HashMap / CHM |
| Sorted iteration / ranges | **TreeMap** |
| Approx concurrent navigable | ConcurrentSkipListMap |

## 6. Failure Scenario

Keys with inconsistent `compareTo` vs `equals` → TreeMap contract broken (subtle bugs). Comparator must be consistent with equals for Set/Map contracts.

## 7. Interview

- TreeMap complexity? Red-black role?  
- compareTo vs equals consistency?  
- **Principal:** in-JVM TreeMap leaderboard vs Redis — when switch?

### Related

[treeset.md](./treeset.md) · [hashmap.md](./hashmap.md) · [comparable.md](./comparable.md) · [comparator.md](./comparator.md)

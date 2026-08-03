# Interview — Collections (Senior / Staff / Principal)

Use with [decision-matrix.md](./decision-matrix.md). Answer: **structure → complexity → concurrency → failure → alternative**.

---

## Core comparisons

### ArrayList vs LinkedList vs ArrayDeque

| | ArrayList | LinkedList | ArrayDeque |
|--|-----------|------------|------------|
| Structure | `Object[]` | Nodes | Ring `Object[]` |
| get(i) | O(1) | O(n) | N/A (not List) |
| Ends | Amortized O(1) add | O(1) | Amortized O(1) |
| Locality | Excellent | Poor | Excellent |
| Default | **Yes (List)** | Rare | **Yes (queue/stack)** |

### HashMap vs LinkedHashMap vs TreeMap vs CHM

| | HashMap | LinkedHashMap | TreeMap | CHM |
|--|---------|---------------|---------|-----|
| Order | No | Insert/access | Sorted | No |
| Concurrent | No | No | No | **Yes** |
| Null key | 1 | 1 | No* | No |
| Complexity | ~O(1) | ~O(1) | O(log n) | ~O(1) |

\*natural order

### HashSet vs TreeSet vs LinkedHashSet

Hash unique → HashSet; insertion order → LinkedHashSet; sorted/range → TreeSet; concurrent → CHM key set.

---

## Internals rapid fire

- HashMap: hash → spread → bucket → list/tree; resize at load factor 0.75; treeify ~8.  
- Fail-fast vs weakly consistent iterators.  
- PriorityQueue = heap; iteration **not** sorted.  
- COW = copy on write; read-heavy only.  
- BlockingQueue = bounded backpressure tool.

---

## Scenario drills

1. **Customer lookup** at 50K RPS — HashMap vs CHM vs Redis?  
2. **Product catalog** refresh — live CHM vs immutable `Map.copyOf` publish?  
3. **Caching** — LinkedHashMap LRU vs Caffeine?  
4. **Leaderboards** — TreeMap vs PriorityQueue vs Redis ZSET?  
5. **Job queues** — ArrayDeque vs ArrayBlockingQueue vs Kafka?  
6. **Session storage** — CHM + TTL vs Redis?  
7. **Rate limiting** — CHM+LongAdder vs edge gateway?

---

## Failure drills

- Shared HashMap corruption  
- Unbounded queue OOM  
- CME while deleting during for-each  
- Mutable key lost in HashSet  
- COW write storm GC  
- Resize storms on bulk HashMap load  

---

## Staff / Principal prompts

1. Design an in-process cache with eviction, metrics, and concurrency — justify each collection.  
2. When is an in-JVM collection the wrong abstraction (distributed, durable, multi-node)?  
3. How do you capacity-plan a BlockingQueue for a payment capture workers pool?  
4. Hot-key contention on CHM — symptoms and remedies?  
5. What do you ban in code review (`LinkedList` default, `Hashtable`, unbounded queues, mutable keys)?

---

## Topic index

[README](./README.md) · [decision-matrix](./decision-matrix.md) · [hashmap](./hashmap.md) · [concurrenthashmap](./concurrenthashmap.md) · [arraylist](./arraylist.md) · [blockingqueue](./blockingqueue.md) · [copyonwritearraylist](./copyonwritearraylist.md)

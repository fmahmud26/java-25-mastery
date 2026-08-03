# Decision Matrix — When X Instead of Y?

Principal-style chooser for Java 25 collections. Defaults favor **simplicity + locality** unless requirements force otherwise.

## List

| Need | Choose | Not |
|------|--------|-----|
| Default list, random access | **ArrayList** | LinkedList |
| Stack/queue ends only | **ArrayDeque** | LinkedList / Stack |
| Mid-list insert via iterator (rare) | LinkedList (measure) | assume LinkedList is “faster” |
| Many iterators, rare writes, snapshot iteration | **CopyOnWriteArrayList** | CHM-as-list; COW under heavy writes |
| Legacy sync list | Prefer explicit concurrency design | Vector |

## Set

| Need | Choose | Not |
|------|--------|-----|
| Unique, fast contains | **HashSet** | TreeSet unless sorted |
| Unique + insertion order | **LinkedHashSet** | HashSet when order matters |
| Sorted / range | **TreeSet** | HashSet |
| Concurrent unique keys | `ConcurrentHashMap.newKeySet()` | sync’d HashSet casually |

## Map

| Need | Choose | Not |
|------|--------|-----|
| Default map | **HashMap** | Hashtable |
| Concurrent readers/writers | **ConcurrentHashMap** | HashMap + hope |
| Insertion or access order / LRU skeleton | **LinkedHashMap** | TreeMap for LRU |
| Sorted keys / range queries | **TreeMap** | HashMap |
| Null key needed | HashMap (one null key) | CHM (no null keys) |
| Multi-node cache | Redis/Caffeine + policy | Giant JVM HashMap as SoT |

## Queue / Deque

| Need | Choose | Not |
|------|--------|-----|
| Single-thread handoff / stack | **ArrayDeque** | Stack / LinkedList |
| Priority / leaderboard top-N drain | **PriorityQueue** | sorted ArrayList for hot path without measuring |
| Producer/consumer across threads | **BlockingQueue** (e.g. ArrayBlockingQueue, LinkedBlockingQueue) | busy-spin ArrayDeque |
| Bounded load shedding | Bounded BlockingQueue + **rejection policy** | unbounded queue “forever” |

## Concurrency quick picks

| Situation | Structure |
|-----------|-----------|
| Shared mutable map | ConcurrentHashMap |
| Request-local / single writer | HashMap |
| Read-mostly list of listeners | CopyOnWriteArrayList |
| Work queue between threads | BlockingQueue |
| Publish immutable snapshot | `Map.copyOf` / unmodifiable publish |

## Scenario → structure

| Scenario | Structure | Why |
|----------|-----------|-----|
| Customer lookup | HashMap / CHM | O(1) avg by id |
| Product catalog | Immutable `Map` refresh | Read-heavy, atomic publish |
| Caching | LinkedHashMap access-order **or** Caffeine | Eviction policy required |
| Leaderboards | TreeMap / PriorityQueue / Redis ZSET | Ordering is the product |
| Job queues | BlockingQueue | Blocking + bounds |
| Session storage | CHM + TTL **or** Redis | Memory + multi-instance |
| Rate limiting | CHM + window / token bucket | Concurrent counters |

## Anti-patterns

- `LinkedList` as default List  
- `Hashtable` / `Vector` in new code  
- Unbounded CHM/queue as “cache”  
- Mutable keys in hash structures  
- Synchronizing on a HashMap instead of using CHM when contended  

### Related

[README.md](./README.md) · [interview.md](./interview.md)

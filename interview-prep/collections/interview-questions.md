# Collections — Interview Questions (L1–L4)

Escalate only when the interviewer pushes. HashMap is the spine; other structures follow the same ladder.

---

## Level 1 — Junior

### What is a HashMap?

**Answer:** A `Map` implementation that stores key–value pairs and finds values by key using hashing. Average get/put are O(1). Keys must obey `equals`/`hashCode`. It is **not** thread-safe.

### List vs Set vs Map?

**Answer:** List = ordered sequence with duplicates; Set = unique elements; Map = key→value associations.

### How do you iterate a HashMap?

**Answer:** `for (var e : map.entrySet())`, or `forEach`, or keySet/values views. Prefer `entrySet` when you need both key and value.

---

## Level 2 — Mid-level

### How does HashMap work internally?

**Answer:** Keys are hashed to a bucket index in a power-of-two table. Collisions chain in a bin; long bins can treeify. On load factor breach the table resizes and entries redistribute. Lookup: hash → bucket → `equals` walk/tree search.

### HashMap vs LinkedHashMap vs TreeMap?

**Answer:** HashMap = unordered best-effort O(1). LinkedHashMap = hash plus linked list for insert/access order. TreeMap = red-black tree, sorted keys, O(log n), needs `Comparable`/`Comparator`.

### Why override both equals and hashCode?

**Answer:** HashMap uses hash to find the bin and equals to find the entry. Unequal hashCodes for equal objects → duplicates / failed gets.

---

## Level 3 — Senior

### Why can HashMap performance degrade?

**Answer:**  
- Poor hash distribution → long bins (CPU in equals chains / trees).  
- Repeated resizing under growth.  
- Huge maps → GC + CPU cache misses.  
- Mutable keys → “lost” entries and surprising misses (logical corruption).  
- Using HashMap under concurrency → corruption/infinite loops historically; today still data races / lost updates.  
- Wrong workload: frequent true ordered scans better on other structures.

### When ConcurrentHashMap vs synchronizing a HashMap?

**Answer:** CHM for concurrent readers/writers with fine-grained concurrency. Synchronized map wraps coarse locking — simpler but less scalable. Often best: confine HashMap to one thread or replace with immutable snapshot.

### How would you implement a simple LRU cache?

**Answer:** `LinkedHashMap` in access-order overriding `removeEldestEntry`, or HashMap + doubly linked list. Discuss capacity, thread safety, and O(1) ops.

---

## Level 4 — Expert

### How would you diagnose a production service with high CPU and excessive HashMap contention?

**Answer (structured):**

1. **Confirm symptoms** — CPU saturation, latency spikes, thread states (`RUNNABLE` hot methods vs `BLOCKED`).  
2. **Profile** — async-profiler / JFR: samples in `HashMap.get/put`, `ConcurrentHashMap`, lock inflations, or `equals`/`hashCode`.  
3. **Hypotheses**  
   - Single global `HashMap` locked by many threads  
   - CHM with extreme hot-key updates  
   - Pathological hashing / large equality cost  
   - Resize storms on a growing shared map  
4. **Evidence** — map size metrics, lock profiles, allocation rate, flame graphs.  
5. **Remedies (risk-ordered)**  
   - Remove shared mutable map; use request-local maps  
   - Stripe by key / shard maps  
   - CHM + `compute` carefully; LongAdder for counters  
   - Cache outside JVM (Redis) if multi-instance  
   - Fix key `hashCode` if skewed  
6. **Validate** — load test, compare p99 CPU and profiles, add regression metrics (map size, wait time).

**Common Mistake at L4:** Saying “just use ConcurrentHashMap” without profiling or naming the contention site.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | What does `ArrayList` guarantee about index access? |
| 2 | How does `ArrayList` grow? |
| 3 | When is `LinkedList` worse than `ArrayList` in real JVMs? |
| 4 | Service GC thrashing with millions of short-lived maps per request — how do you approach it? |

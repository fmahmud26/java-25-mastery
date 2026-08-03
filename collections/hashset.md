# HashSet

`Set` backed by a **HashMap** (elements as keys, dummy value). Unordered unique elements.

## 1. Mental Model

```text
HashSet  ≈  HashMap<E, PRESENT>
contains → map.containsKey
```

## 2. Internals

Inherits HashMap hashing/buckets/collisions/resize/load factor. Iteration order unspecified and may change on resize. Not thread-safe. Allows one `null`.

## 3. Complexity

Average O(1) add/contains/remove; same caveats as HashMap.

## 4. Scenarios

- **Idempotency keys processed** (bounded window).  
- **SKU uniqueness** in an import batch.  
- Rate limiting “seen IPs” — must bound size.

## 5. When HashSet instead of TreeSet / LinkedHashSet?

| Need | Choice |
|------|--------|
| Fast unique | HashSet |
| Stable insertion order | LinkedHashSet |
| Sorted | TreeSet |
| Concurrent | `ConcurrentHashMap.newKeySet()` |

## 6. Failure Scenario

Mutable element mutated after add → membership broken. Unbounded set as “cache” → OOM.

## 7. Interview

- How is HashSet implemented?  
- Why iteration order can change?  
- **Principal:** dedupe pipeline at 100K events/s — Set vs Bloom vs external store?

### Related

[hashmap.md](./hashmap.md) · [linkedhashset.md](./linkedhashset.md) · [treeset.md](./treeset.md)

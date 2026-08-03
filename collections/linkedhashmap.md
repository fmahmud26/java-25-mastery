# LinkedHashMap

Hash table + **doubly-linked list** of entries — predictable iteration (insertion order or access order).

## 1. Mental Model

```text
HashMap buckets for O(1) lookup
    +
linked list through entries for order
```

## 2. Internals

| Topic | Behavior |
|-------|----------|
| Structure | HashMap + before/after links on entries |
| Ordering | Insertion (default) or **access-order** (`true` ctor) |
| LRU hook | Override `removeEldestEntry` |
| Complexity | Like HashMap + pointer maintenance |
| Concurrency | Not thread-safe |
| Memory | Extra pointers per entry vs HashMap |

## 3. Code — cache skeleton

```java
public final class LruSkuCache extends LinkedHashMap<String, Product> {
    private final int max;

    public LruSkuCache(int max) {
        super(16, 0.75f, true); // access-order
        this.max = max;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Product> eldest) {
        return size() > max;
    }
}
```

For production caches prefer **Caffeine/Guava** (eviction, stats, concurrency) — LinkedHashMap is the teaching/building block.

## 4. Scenarios

| Scenario | Use |
|----------|-----|
| Caching | Access-order + eldest removal (single-thread) |
| Product catalog display order | Insertion-order map |
| Session sticky local | Careful — prefer CHM/Redis for multi-thread |

## 5. When LinkedHashMap instead of HashMap / TreeMap?

| Need | Choice |
|------|--------|
| Fast + ignore order | HashMap |
| Preserve insert/access order | **LinkedHashMap** |
| Sorted by key | TreeMap |
| Concurrent LRU | External cache / CHM + policy — not naive LHM |

## 6. Failure Scenario

Access-order LinkedHashMap shared across threads → corruption. Fix: confine or use concurrent cache lib.

## 7. Interview

- Insertion vs access order?  
- How do you build LRU with LinkedHashMap?  
- **Principal:** why not ship LinkedHashMap LRU in a multi-threaded API tier?

### Related

[hashmap.md](./hashmap.md) · [treemap.md](./treemap.md) · [decision-matrix.md](./decision-matrix.md)

# Soft References

`SoftReference<T>` — GC may clear under **memory pressure**; typically retained longer than weak.

## Mental Model

```text
“Keep if the heap is comfortable; drop when under pressure.”
Good idea for optional caches — bad as the only eviction policy.
```

## Technical Mechanism

```java
SoftReference<byte[]> cache = new SoftReference<>(new byte[1_000_000]);
byte[] data = cache.get(); // null if cleared
```

HotSpot clearing is implementation-dependent (often considers free heap / recency). **Not** a hard SLA for “stays N minutes.”

## When to Prefer Soft vs Weak

| Prefer soft | Prefer weak |
|-------------|-------------|
| Memory-sensitive cache entries | Don’t prevent GC of keys / identity |
| “Nice to keep” decoded images, etc. | Class metadata-style maps |

## Production Implications

- Soft caches can still grow until pressure → sudden clear storms + CPU.  
- Prefer **Caffeine/Guava** with max size, TTL, stats, and load shedding.  
- Incident “large cache” often starts as unbounded strong map; soft-only rewrites without bounds still hurt.

## Interview / PE

Is soft reference clearing deterministic? Why production prefers explicit cache libraries?

### Related

[weak-references.md](./weak-references.md) · [memory-leaks.md](./memory-leaks.md) · [incidents.md](./incidents.md)

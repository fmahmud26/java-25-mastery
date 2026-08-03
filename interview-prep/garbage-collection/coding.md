# Garbage Collection — Coding

GC interviews rarely want collectors implemented; they want **root reasoning** and **leak patterns**.

| Problem | Structure / idea |
|---------|------------------|
| Find leak suspects | Dominator tree in heap dump |
| Cache unbounded | Soft/Weak refs or size-bounded cache |
| Classloader leak | Static field → loader → all classes |
| Chatty alloc | Object pooling *last*; fix design first |

```java
// Unbounded cache — classic “old gen fills” story
final Map<String, byte[]> cache = new ConcurrentHashMap<>();

void put(String key, byte[] value) {
    cache.put(key, value); // no eviction → promotion → Full GC / OOME
}

// Better sketch: bounded
final Map<String, byte[]> bounded = Collections.synchronizedMap(
        new LinkedHashMap<>(16, 0.75f, true) {
            @Override protected boolean removeEldestEntry(Map.Entry<String, byte[]> e) {
                return size() > 10_000;
            }
        });
```

```java
// SoftReference — GC-friendly cache hint (still measure)
var soft = new SoftReference<>(expensiveGraph);
var g = soft.get(); // may be null after memory pressure
```

**Talk track:** GC frees *unreachable* objects only. Caches and listeners keep reachability — that’s an app bug, not “GC is broken.”

Related: [interview.md](../../garbage-collection/interview.md).

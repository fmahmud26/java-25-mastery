# Memory Leaks

In Java: objects remain **strongly reachable** longer than the design intended — heap (or metaspace) climbs until thrashing or OOM.

## Mental Model

```text
Not “lost pointer” like C.
Lost *intent*: we forgot to unretain.
```

## Common Causes

| Cause | Example |
|-------|---------|
| Static / global collections | Unbounded `static Map` |
| Caches | No max size / TTL |
| Listeners | Register, never unregister |
| `ThreadLocal` | Missing `remove()` on pooled threads |
| Closures / inner classes | Capture large outer state |
| Unbounded queues | Grow forever under backlog |
| Classloader leaks | Redeploy retains old loader |
| Resources | Growing buffers, unclosed streams |

```java
try {
    TL.set(ctx);
    handle();
} finally {
    TL.remove(); // mandatory with executors
}
```

## Detection Pattern

```text
Symptoms → Metrics (heap after GC ↑) → Heap dump → Dominator / path to root
        → Root cause → Fix → Prevention (bounds + metrics)
```

Full playbook: [investigation.md](./investigation.md) · [practical/memory-leak.md](./practical/memory-leak.md)

## Leak vs Undersized Heap

| Leak | Undersized |
|------|------------|
| Live set grows over time at steady traffic | Live set stable; traffic/heap mismatch |
| Dump shows unexpected retainers | Dump shows expected domain objects |
| Fix code | Right-size or reduce live set / cache |

## Interview / PE

Define Java leak. Top 5 causes. How prove with a dump?

### Related

[object-retention.md](./object-retention.md) · [outofmemoryerror.md](./outofmemoryerror.md) · [incidents.md](./incidents.md)

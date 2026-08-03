# Practical: Memory Leak

## Symptoms

- Heap **after GC** trends upward over hours/days  
- More frequent full/mixed collections; rising GC CPU  
- Eventually `OutOfMemoryError: Java heap space`  
- RSS grows without matching traffic growth  

## Metrics

```text
heap used after GC (baseline)
old generation occupancy
allocation rate (stable + rising live set ⇒ retention)
custom: cache.size, listener.count, sessions
```

## Heap Dump

Capture with [heap-dump.md](./heap-dump.md) while the process is still alive and the baseline is elevated.

## Analysis

1. Dominator tree → largest retained sets  
2. Path to GC root → static field / thread / listener list  
3. Confirm instance counts vs expected cardinality  

## Root Cause → Fix → Prevention

| Cause | Fix | Prevention |
|-------|-----|------------|
| Unbounded cache | Max size + TTL | Cache metrics + review gate |
| Listener leak | Unregister symmetrically | Lifecycle tests |
| ThreadLocal | `remove()` in finally | Framework filter / ScopedValue |
| Static collection | Bound or remove | Ban unbounded statics |
| Loader leak | Drop refs to old loader | Metaspace monitor on redeploy |

## Checklist

- [ ] Unbounded caches  
- [ ] Static `List`/`Map`  
- [ ] `ThreadLocal` + executors  
- [ ] Listeners / callbacks  
- [ ] Unclosed resources / growing buffers  
- [ ] Classloader / redeploy  

Full narrative incidents: [../incidents.md](../incidents.md)

### Related

[../memory-leaks.md](../memory-leaks.md) · [../investigation.md](../investigation.md) · [../principal-engineer.md](../principal-engineer.md)

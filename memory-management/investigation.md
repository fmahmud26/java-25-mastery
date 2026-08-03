# Investigation Method (Memory)

Standard PE loop for memory incidents.

```text
Symptoms → Metrics → Heap dump (or NMT/metaspace) → Analysis → Root cause → Fix → Prevention
```

---

## 1. Symptoms

- Latency ↑, GC CPU ↑, promotions ↑  
- Heap used after GC trends up  
- OOM / container OOMKill  
- RSS ↑ with flat traffic  
- Metaspace committed ↑ after redeploys  

## 2. Metrics

| Metric | Why |
|--------|-----|
| Heap used / committed after GC | Leak vs churn |
| Old gen / occupancy | Retention |
| Allocation rate | GC pressure |
| GC pause / frequency | Symptom severity |
| Metaspace used | Loader/class growth |
| Threads | Stack RSS / native thread OOM |
| Direct buffer / NMT | Off-heap |
| Cache size / hit rate | Product metrics |

```bash
jstat -gcutil <pid> 1000
jcmd <pid> GC.heap_info
jcmd <pid> VM.metaspace
# app metrics: jvm.memory.used{area=heap}, gc.pause
```

## 3. Heap Dump

```bash
jcmd <pid> GC.heap_dump /tmp/heap.hprof
# or -XX:+HeapDumpOnOutOfMemoryError
```

Prefer dump when live set is high but before process death. Mind PII/secrets in dumps.

## 4. Analysis

1. Leak Suspects (MAT)  
2. Dominator Tree — largest retained chunks  
3. Path to GC Root for a fat object  
4. Histogram — top classes by instance count / size (`byte[]`, `char[]`, `HashMap$Node`)  
5. Compare two dumps over time if needed  

Details: [practical/heap-dump.md](./practical/heap-dump.md)

## 5. Root Cause

Name the **retainer** and the **missing eviction/unregister/remove**. Examples:

- Static cache without bound  
- Listener list growth  
- `ThreadLocal` on pool threads  
- Session map  
- Loader held by ThreadLocal/static  

## 6. Fix

- Bound + evict; unregister; `TL.remove()`; close resources  
- Or right-size if dump shows *expected* live set  

## 7. Prevention

- Max size + TTL + metrics on every cache  
- Load tests with heap/GC dashboards  
- Alerts: heap after GC, metaspace, RSS vs limit  
- Code review checklist for static collections / listeners / ThreadLocal  

### Related

[incidents.md](./incidents.md) · [memory-leaks.md](./memory-leaks.md) · [principal-engineer.md](./principal-engineer.md)

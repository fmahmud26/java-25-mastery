# Realistic Incidents (Memory)

Each follows: Symptoms → Metrics → Dump → Analysis → Root cause → Fix → Prevention.

---

## 1) Heap Continuously Increasing

**Symptoms:** After-GC heap climbs over hours; Full GC more often; eventual heap OOM.

**Metrics:** `heap.used` sawtooth with rising baseline; old gen ↑; allocation rate stable.

**Dump:** Dominator shows one `ConcurrentHashMap` / cache dominating.

**Analysis:** Path to root → `static final Cache HOLDER`.

**Root cause:** Unbounded strong cache.

**Fix:** Max size + eviction (Caffeine); or remove feature cache.

**Prevention:** Cache occupancy metric + alert; review ban on unbounded `Map`.

---

## 2) Unexpected OOM

**Symptoms:** Pod OOMKilled **or** `OutOfMemoryError` with a non-heap message; “heap looks fine.”

**Metrics:** Heap 40% used; RSS at cgroup limit **or** Metaspace at max **or** direct memory high.

**Dump / tools:** Heap dump inconclusive → `VM.native_memory`, `VM.metaspace`, direct buffer MXBean, thread count.

**Root cause (pick):** Metaspace leak; direct buffers; threads×stacks; cgroup < heap+native.

**Fix:** Address that domain; align container limit with total footprint.

**Prevention:** NMT in staging; metaspace/direct/thread alerts; document RSS budget.

---

## 3) Large Cache

**Symptoms:** Memory tracks unique keys forever; GC cannot reclaim; hit rate high but heap huge.

**Metrics:** Cache estimated size → millions; heap after GC correlates with key cardinality.

**Dump:** Millions of `Cache.Node` / `byte[]` values.

**Root cause:** Business cache without bound/TTL; or soft refs without max size.

**Fix:** Size bound, TTL/TTI, weight by value size; load shed.

**Prevention:** Cache design review; SLO for max retained MB.

---

## 4) Listener Leak

**Symptoms:** Heap grows with connection/session churn; domain objects retained after logout.

**Metrics:** Listener count ↑; sessions should be 0 but retainers remain.

**Dump:** `ArrayList` of listeners → outer `Service` → entire world.

**Root cause:** `register` without `unregister` (or weak listener never used correctly).

**Fix:** Symmetric lifecycle; try-with-resource style registration; weak listeners only if API-safe.

**Prevention:** Tests that force GC and assert listener count; leak tests in CI for session end.

---

## 5) ThreadLocal Leak

**Symptoms:** Heap grows with traffic on **thread pool**; each request sets `ThreadLocal`; memory never returns.

**Metrics:** Heap ↑; thread count stable (pooled!).

**Dump:** `Thread` → `ThreadLocalMap` → large `byte[]` / `SecurityContext` / `Connection`.

**Root cause:** Missing `remove()` in `finally`; pooled thread retains value forever.

**Fix:**

```java
try {
    TL.set(v);
    run();
} finally {
    TL.remove();
}
```

**Prevention:** SpotBugs/Error Prone rules; wrapper utilities; prefer ScopedValue for immutable request context on modern Java where applicable.

---

## 6) Large Object Allocation

**Symptoms:** Latency spikes; humongous allocations (G1); old gen jumps; occasional alloc failure.

**Metrics:** JFR allocation events show huge `byte[]`; GC humongous logs.

**Dump:** Few gigantic `byte[]` / full-file buffers.

**Root cause:** Read entire upload/file/JSON into memory.

**Fix:** Streaming; size caps; reject oversize; chunked processing.

**Prevention:** Max request size at edge; alloc profiling in load tests.

---

## Cross-check card

```text
After-GC heap ↑ at steady QPS? → leak retention
OOM message? → pick domain
Path to root? → name the field/collection
Bound missing? → product bug, not “JVM bug”
```

### Related

[investigation.md](./investigation.md) · [practical/memory-leak.md](./practical/memory-leak.md) · [principal-engineer.md](./principal-engineer.md)

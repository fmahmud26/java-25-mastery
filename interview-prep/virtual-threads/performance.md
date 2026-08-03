# Virtual Threads — Performance

## Cost model

| Factor | Effect |
|--------|--------|
| VT create/mount | Cheap vs platform thread |
| Blocking unmount | Carrier reused → high concurrency |
| Pinning / native block | Carrier stuck → effective pool shrinks |
| CPU-bound VT flood | Queue on few carriers; no extra cores |
| Huge `ThreadLocal` caches | Memory blow-up × VT count |
| Unlimited fan-out | Overwhelm DB / HTTP deps (need bounds) |

## Scalability cliffs

1. **CPU tasks on VTs** — thrash scheduler; steal from I/O VTs (most common “VT made it worse”).  
2. **Unbounded submit** — overwhelm DB / HTTP deps (need bounds).  
3. **Residual pinning** — native/JNI/FFM, some local file I/O, class-init (JFR `jdk.VirtualThreadPinned`). On Java 25, do **not** lead with “synchronized pins” (JEP 491).  
4. **Lock-over-I/O** — design latency/correctness even when unmount works.  
5. **Pooling VTs** — pointless; wrong mental model.  
6. **Huge ThreadLocal state** — memory × in-flight VT count.  
7. **Reactive + VT cargo-cult** — extra complexity if blocking + VT already fits.

## Measurement

| Signal | Tool |
|--------|------|
| Carrier saturation / pinning | JFR virtual-thread / pinned events; `jcmd` thread dump |
| Throughput vs platform pool | Load test same handler on both |
| Downstream overload | DB connections, HTTP 429/timeouts |
| CPU wrongly on VT executor | Profiler: hot pure-Java compute frames on virtual threads |

Related: [thread-scalability.md](../../virtual-threads/thread-scalability.md), [../../performance-engineering](../../performance-engineering/).

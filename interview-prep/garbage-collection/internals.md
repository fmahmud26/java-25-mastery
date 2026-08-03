# Garbage Collection — Internals

Focus: **generational + G1 vs ZGC** (whiteboard).

```text
Roots → mark live graph → reclaim unreachable
Young: copy/evacuate Eden+Survivor
Old:  mark concurrently or STW; compact / region evacuate
```

## Must-explain pieces

| Piece | Point |
|-------|-------|
| GC roots | Thread stacks, statics, JNI, waiting threads, etc. |
| Minor / young GC | Collect young; promote survivors that age out |
| Promotion | Live young objects → old |
| Full GC | Whole-heap collection; expensive fallback (esp. G1) |
| Remembered sets / cards | Track old→young refs so young GC is correct |
| Barriers | Read/write barriers keep concurrent collectors correct |
| Allocation failure | Eden/region full → schedule collection |

## G1 (regions)

```text
Heap = equal regions [E][E][S][O][O][H]…
Young evacuate → concurrent mark → mixed GC (garbage-first old regions)
Pause goal: MaxGCPauseMillis (soft); Full GC if evacuations fail
```

## ZGC

| Trait | Detail |
|-------|--------|
| Colored pointers + load barriers | Object state while concurrent |
| Concurrent mark & relocate | Mutators mostly keep running |
| Pauses | Concurrent relocate; aim is low pauses — **measure your build**; do not quote a universal “sub-ms” guarantee |
| Generational ZGC | On Java 25, ZGC **is** generational (not an optional `-XX:+ZGenerational` mode) |

## G1 vs ZGC one-liner

G1: **predictable-ish pause budget**, region evacuations, mixed collections.  
ZGC: **minimize pause**, concurrent relocate, great for large heaps / latency SLOs; throughput trade-off possible.

Related: [mark.md](../../garbage-collection/mark.md), [g1-gc.md](../../garbage-collection/g1-gc.md), [zgc.md](../../garbage-collection/zgc.md), [full-gc.md](../../garbage-collection/full-gc.md).

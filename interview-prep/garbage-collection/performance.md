# Garbage Collection — Performance

## What to quote

| Metric | Meaning |
|--------|---------|
| Allocation rate | Bytes/sec into Eden — drives young GC frequency |
| Pause time (STW) | Mutator delay — hits p99 |
| Throughput | 1 − (GC CPU / total CPU) |
| Promotion rate | Young → old pressure |
| Heap occupancy after GC | Live set size — lower bound on heap need |

## Degradation causes

1. **High allocation** — too many young GCs / CPU in barriers  
2. **Premature promotion** — survivor too small / tenuring threshold  
3. **Old-gen fragmentation / humongous** (G1) — to-space exhaustion → Full GC  
4. **Live set ≈ heap** — constant Full GC / OOME  
5. **Wrong collector** — Parallel pauses vs latency SLO; ZGC overhead on tiny heaps  
6. **`System.gc()`** / explicit full collections in libraries  

## Tuning basics (order matters)

1. Fix leaks / allocation churn  
2. Size heap from live set + headroom (`-Xms`/`-Xmx`)  
3. Pick collector for SLO (G1 vs ZGC)  
4. Soft pause goal (`MaxGCPauseMillis`) — don’t fantasize 1ms on G1 with huge live set  
5. Only then: region size, pause goal, threads — measure each change  

## Measurement

- `-Xlog:gc*` / GCToolkit parsers  
- JFR GC + Allocation events  
- `jstat -gcutil`, `jcmd GC.heap_info`  
- Heap dump (Eclipse MAT / VisualVM) for leaks  

Related: [../../performance-engineering](../../performance-engineering/), [minor-gc.md](../../garbage-collection/minor-gc.md), [full-gc.md](../../garbage-collection/full-gc.md).

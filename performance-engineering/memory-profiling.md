# Memory Profiling

Broader than allocation rate: **retention**, heap occupancy, native/metaspace, dump analysis.

## Measure

| Question | Tool |
|----------|------|
| Who allocates? | Allocation profiling / JFR |
| Who retains? | Heap dump + MAT |
| How big live set? | After-GC occupancy, `GC.heap_info` |
| Native? | NMT, direct buffers, thread stacks |

## Loop

Measure occupancy trend → hypothesize leak vs sizing → dump if retention suspected → fix → re-measure after-GC baseline under same load.

### Related

[heap-dumps.md](./heap-dumps.md) · [allocation-profiling.md](./allocation-profiling.md) · [heap-sizing.md](./heap-sizing.md)

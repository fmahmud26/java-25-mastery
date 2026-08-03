# Compact / Evacuate

Move live objects to create contiguous free space (or into empty regions).

## Mental Model

```text
Before:  [live][gap][live][gap][live]
After:   [live][live][live][ free ............ ]
```

**Evacuate** in G1/ZGC/Shenandoah: copy live objects out of chosen regions, then reclaim whole regions.

## Technical Mechanism

| Approach | Cost character |
|----------|----------------|
| STW compact | Simple; pause scales with live data (classic) |
| Concurrent relocate | Mutators keep running; barriers update refs |
| Copying young GC | Evacuate survivors only |

## Production Implications

Compaction reduces fragmentation and helps large allocations succeed. It is also where many pause and CPU costs appear — collector choice decides how much is STW vs concurrent.

### Related

[mark.md](./mark.md) · [sweep.md](./sweep.md) · [g1-gc.md](./g1-gc.md) · [zgc.md](./zgc.md)

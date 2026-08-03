# Collector Trade-offs

Choose with **measurements**, not slogans.

## Dimensions

```text
Throughput   — useful app work / wall time
Latency      — pause & tail latency under load
Footprint    — Java heap + native GC structures + headroom
CPU overhead — concurrent GC + barriers vs STW bursts
Complexity   — operability, failure modes, tuning surface
```

## Qualitative Matrix (Java 25)

| Collector | Emphasizes | Watch |
|-----------|------------|-------|
| **Serial** | Simplicity, small footprint | Long STW as heap grows |
| **Parallel** | Throughput | Pause length |
| **G1** | Balance; pause *goals*; default-ish servers | Full GC / humongous; goal ≠ guarantee |
| **ZGC** | Low pause design; large heaps | CPU/footprint vs G1 — measure |
| **Shenandoah** | Low pause design | Mode (gen vs not); measure vs ZGC/G1 |

No row is “always fastest.” Blog percentages are not SLOs.

## Decision Sketch

```text
Batch, pause OK           → consider Parallel (or G1)
General server            → start G1; size heap; fix alloc/leaks
Strict tail latency       → bakeoff ZGC / Shenandoah vs G1
Tiny 1-CPU tool           → Serial or G1; pin explicitly
```

## PE Rules

1. Fix leaks and allocation storms before collector shopping.  
2. One change at a time; record JFR + GC logs.  
3. Compare p99 **application** latency, not only GC pause averages.  
4. Document the chosen collector in the runbook.

### Related

[g1-gc.md](./g1-gc.md) · [zgc.md](./zgc.md) · [shenandoah.md](./shenandoah.md) · [incidents.md](./incidents.md)

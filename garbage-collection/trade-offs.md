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
| **ZGC** | Low pause design; large heaps; generational | CPU/footprint vs G1 — measure |
| **Shenandoah** | Low pause design; optional generational mode | Mode (gen vs not); measure vs ZGC/G1 |

No row is “always fastest.” Blog percentages are not SLOs.

## Decision Sketch

```text
Batch, pause OK           → consider Parallel (or G1)
General server            → start G1; size heap; fix alloc/leaks
Strict tail latency       → bakeoff ZGC / Shenandoah vs G1
Tiny 1-CPU tool           → Serial or G1; pin explicitly
```

## Measure Protocol (bakeoff)

1. Fix leaks / obvious alloc storms first.  
2. Pin JDK 25 build, machine class, heap (`-Xms`/`-Xmx`), workload W.  
3. Capture: success RPS, app latency histogram, GC pause histogram, CPU%, RSS.  
4. Run G1 baseline ≥ peak-shaped load.  
5. One alternate collector; same W; n≥3 windows.  
6. Pick by **SLO metric** (usually app p99) with CPU/footprint constraints documented.  
7. Write runbook: flags, dashboards, rollback.

Tools: [diagnostics.md](./diagnostics.md) · [../performance-engineering/jvm-observability.md](../performance-engineering/jvm-observability.md)

## Production Scenario — wrong reason to switch

Team enables ZGC because “modern.” G1 had zero Full GC and p99 already inside SLO. CPU rises 12%; no latency win. Rollback. Collector shopping without a pause hypothesis wastes capacity.

## PE Rules

1. Fix leaks and allocation storms before collector shopping.  
2. One change at a time; record JFR + GC logs.  
3. Compare p99 **application** latency, not only GC pause averages.  
4. Document the chosen collector in the runbook.  
5. `MaxGCPauseMillis` is a **goal**, not a contract (G1).  

### Related

[g1-gc.md](./g1-gc.md) · [zgc.md](./zgc.md) · [shenandoah.md](./shenandoah.md) · [incidents.md](./incidents.md) · [../performance-engineering/low-latency-architecture.md](../performance-engineering/low-latency-architecture.md)

# G1 GC (Garbage-First)

Region-based, generational, mostly concurrent collector. **Default** on typical Java 25 server-class configurations. Designed to balance throughput with **pause-time goals**.

```bash
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 ...
# MaxGCPauseMillis is a goal for the ergonomics — not a hard SLA guarantee
```

## Mental Model

```text
Heap = equal-sized regions
  Eden / Survivor / Old / Humongous labels
Young GC: evacuate young regions
Concurrent mark: find live data in old
Mixed GC: young + old regions with most garbage (“garbage first”)
Full GC: fallback when incremental strategies fail
```

## Technical Mechanism

1. Regions (~1–32MB range depending on heap; VM chooses).  
2. Young collections evacuate Eden+Survivor.  
3. Concurrent marking identifies live objects.  
4. Mixed collections reclaim high-garbage old regions incrementally.  
5. Remembered sets + barriers track cross-region references.  
6. Humongous objects span regions — watch fragmentation.

## Production Implications

| Strength | Good general-purpose server default |
|----------|--------------------------------------|
| Tune lightly | Heap size, pause *goal*, fix allocation/leaks first |
| Watch | Mixed GC efficacy, humongous, to-space / evacuation failures, Full GC |

Prefer measuring pause **distribution** (p50/p99) over a single average.

## Trade-offs vs ZGC / Shenandoah

G1 often chosen when you want balanced behavior without low-pause collector overhead. If tail latency dominates the SLA, evaluate ZGC/Shenandoah **with the same workload** — see [trade-offs.md](./trade-offs.md).

## Interview / PE

Explain regions, young vs mixed vs full. Is `MaxGCPauseMillis` a guarantee? (No.) What is humongous?

### Related

[zgc.md](./zgc.md) · [shenandoah.md](./shenandoah.md) · [full-gc.md](./full-gc.md) · [heap-sizing.md](./heap-sizing.md)

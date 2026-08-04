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

## Internals (L1→L3)

| Level | What to know |
|-------|----------------|
| L1 | Collect garbage-first *regions*, not whole generations in one go |
| L2 | Young evacuate Eden+Survivor; mixed adds high-garbage old regions after concurrent mark |
| L3 | Remembered sets (RSets) + write barriers track cross-region refs; collection set chosen to meet pause *goal*; humongous objects span ≥1 region |

### Phases you will say in interviews

1. **Young-only** collections reclaim Eden/Survivor.  
2. **Concurrent mark** (with brief STW handshakes) discovers live objects in old.  
3. **Mixed** collections reclaim selected old regions incrementally.  
4. **Full GC** — compacted fallback; treat as a **distress signal**, not normal steady state.  

### Humongous & evacuation failure

- Objects ≥ region size / 2 typically take humongous regions — fragmentation and failed humongous alloc can force aggressive GC.  
- **Evacuation failure / to-space exhaustion:** survivors do not fit → often leads toward Full GC territory. Fix live set / heap size / alloc spikes before exotic flags.

## Production Implications

| Strength | Good general-purpose server default |
|----------|--------------------------------------|
| Tune lightly | Heap size (`-Xms`≈`-Xmx` when stable), pause *goal*, fix allocation/leaks first |
| Watch | Mixed GC efficacy, humongous, evacuation failures, Full GC count |
| Measure | Pause **distribution** (p50/p99), not a single average |

## Production Scenario — mixed GC not keeping up

Old gen occupancy climbs between mixed cycles; eventually Full GC. Alloc rate OK; live set grew (cache unbounded).

**Fix:** bound cache; verify after-GC occupancy; only then revisit `MaxGCPauseMillis` / heap.

## Production Scenario — humongous spike

Large `byte[]` uploads allocate humongous regions; young GC “fine” but heap fragments; latency spikes.

**Fix:** stream uploads; size limits; monitor humongous occupancy in GC logs/JFR.

## Tuning order (Principal)

1. Fix leaks and allocation storms.  
2. Size heap for live set + headroom.  
3. Confirm Full GC ≈ 0 in steady state.  
4. Adjust pause goal cautiously.  
5. Bakeoff ZGC/Shenandoah only with same workload evidence.

## Trade-offs vs ZGC / Shenandoah

G1 often chosen when you want balanced behavior without low-pause collector overhead. If tail latency dominates the SLA, evaluate ZGC/Shenandoah **with the same workload** — see [trade-offs.md](./trade-offs.md).

## When Not to Blame G1

p99 spikes with quiet GC logs → locks, safepoints, downstream. See [diagnostics.md](./diagnostics.md).

## Interview / PE

Explain regions, young vs mixed vs full. Is `MaxGCPauseMillis` a guarantee? (No.) What is humongous? What does evacuation failure imply?

### Related

[zgc.md](./zgc.md) · [shenandoah.md](./shenandoah.md) · [full-gc.md](./full-gc.md) · [heap-sizing.md](./heap-sizing.md) · [incidents.md](./incidents.md)

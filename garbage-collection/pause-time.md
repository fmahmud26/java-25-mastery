# Pause Time

Time mutators are stopped for GC (plus related safepoint sync). The latency metric ops actually feel.

## Mental Model

```text
Application latency ≠ only GC pause
But GC pause is a common cause of multi-ms to multi-s spikes
Measure distribution: p50 / p95 / p99 / max — not only average
```

## Technical Mechanism

| Component | Notes |
|-----------|-------|
| GC STW pause | Young / mixed / full / remark / etc. |
| Time-to-safepoint | Delay until all threads reach safepoint |
| Concurrent work | Not pause, but CPU contention can raise latency |

G1: `-XX:MaxGCPauseMillis` influences ergonomics — **goal**, not a warranty.

## How to Measure

```bash
-Xlog:gc*,safepoint=info
JFR: GC pause events, safepoint events
jcmd <pid> GC.heap_info   # occupancy context, not pause histogram alone
```

Plot pause times from GC logs or JMC. Compare with application p99 timestamps.

## Production Implications

- A 3s spike needs correlation: GC log pause ≈ spike? If not, look elsewhere (locks, I/O, CPU).  
- Shrinking heap to “reduce pause” can **increase** pause frequency or Full GC risk — measure.  
- Collector switches for pause must include throughput/CPU cost check.

## Interview / PE

Goal vs guarantee for G1 pauses? Why averages lie? Safepoint vs GC pause?

### Related

[stop-the-world.md](./stop-the-world.md) · [gc-logs.md](./gc-logs.md) · [incidents.md](./incidents.md)

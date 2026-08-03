# Garbage Collection — Production Guide (Java 25)

How HotSpot reclaims unreachable objects, how collectors differ, and how to investigate latency and heap pressure **with evidence** — not folklore.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Mental map

```text
Allocate (TLAB / Eden)
  → die young (cheap young collections) OR promote to old
  → concurrent / incremental reclaim (collector-specific)
  → brief STW phases for roots / handshakes
  → Full GC only as stress / fallback signal
```

## Study path

1. Fundamentals: [gc-fundamentals](./gc-fundamentals.md) · [why-gc-exists](./why-gc-exists.md) · [mark](./mark.md) · [sweep](./sweep.md) · [compact](./compact.md) · [stop-the-world](./stop-the-world.md)  
2. Generational: [generational-gc](./generational-gc.md) · [young-generation](./young-generation.md) · [old-generation](./old-generation.md) · [allocation](./allocation.md) · [minor-gc](./minor-gc.md) · [major-gc](./major-gc.md) · [full-gc](./full-gc.md)  
3. Collectors: [serial-gc](./serial-gc.md) · [parallel-gc](./parallel-gc.md) · [g1-gc](./g1-gc.md) · [zgc](./zgc.md) · [shenandoah](./shenandoah.md) · [trade-offs](./trade-offs.md)  
4. Ops: [heap-sizing](./heap-sizing.md) · [allocation-rate](./allocation-rate.md) · [pause-time](./pause-time.md) · [gc-logs](./gc-logs.md) · [diagnostics](./diagnostics.md) · [incidents](./incidents.md) · [interview](./interview.md)

## One-line PE rule

**Measure allocation rate, pause distribution, and live set after GC before changing collectors or flags — `MaxGCPauseMillis` is a goal, not a contract.**

## Accuracy notes (Java 25)

| Topic | Fact |
|-------|------|
| Default GC | **G1** on typical server-class configs; constrained environments may still select Serial unless you pin a collector. JEP 523 (G1 everywhere) targets a **later** JDK — do not claim it for 25. |
| ZGC | Enable with `-XX:+UseZGC`. On modern JDKs including 25, ZGC is **generational** (non-generational mode removed earlier). |
| Shenandoah | `-XX:+UseShenandoahGC`. Generational mode is a **product** feature (JEP 521) via `ShenandoahGCMode=generational`; **default Shenandoah mode remains non-generational** unless you set it. |
| Claims | Pause/throughput numbers are workload-dependent — always validate with JFR/GC logs. |

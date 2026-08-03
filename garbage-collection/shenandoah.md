# Shenandoah

Low-pause concurrent compacting collector in HotSpot. Focus: evacuate live objects **concurrently** with mutators.

```bash
java -XX:+UseShenandoahGC ...

# Generational mode — product feature on Java 25 (JEP 521); not the default mode
java -XX:+UseShenandoahGC -XX:ShenandoahGCMode=generational ...
```

## Mental Model

```text
Concurrent mark + concurrent evacuation
Short STW phases for roots / coordination
Barriers keep mutator view consistent while objects move
```

## Java 25 — Generational Mode (JEP 521)

- Generational Shenandoah is a **product** feature (no experimental unlock required for that mode).  
- **Default** Shenandoah mode remains **non-generational** unless you set `ShenandoahGCMode=generational` (JEP 521 non-goal: do not change the default mode).  
- JEP 535 (generational by default) is separate future work — do not assume it for Java 25.

## Production Implications

| Consider when | Low-pause alternative to ZGC; platform/support fit |
|---------------|-----------------------------------------------------|
| Measure | Same bakeoff discipline as ZGC vs G1 |
| Generational | Try when throughput/efficiency of single-gen Shenandoah is insufficient — verify with metrics |

## Trade-offs

Competes in the low-pause design space with ZGC. Choice depends on JDK build, platform, and measured workload — not brand preference.

## Interview / PE

Enable flags? Generational status on Java 25 vs default mode? STW fully gone? (No.)

### Related

[zgc.md](./zgc.md) · [g1-gc.md](./g1-gc.md) · [trade-offs.md](./trade-offs.md)

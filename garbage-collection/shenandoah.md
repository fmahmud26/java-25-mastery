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
Competes with ZGC in the “low pause” design space
```

## Internals (L1→L2)

| Level | View |
|-------|------|
| L1 | GC moves objects while threads run; barriers maintain correctness |
| L2 | Mark → evacuate → update references with concurrent phases; brief STW for roots |
| Generational mode | Young/old split to reclaim short-lived objects more cheaply (when enabled) |

Exact barrier design differs from ZGC (Brooks-style forwarding is the classic Shenandoah teaching story). Interview bar: **concurrent evacuation + barriers + short STW**, not bit trivia.

## Java 25 — Generational Mode (JEP 521)

- Generational Shenandoah is a **product** feature (no experimental unlock required for that mode).  
- **Default** Shenandoah mode remains **non-generational** unless you set `ShenandoahGCMode=generational` (JEP 521 non-goal: do not change the default mode).  
- JEP 535 (generational by default) is separate future work — do **not** assume it for Java 25.

## Production Implications

| Consider when | Low-pause alternative to ZGC; platform/support fit |
|---------------|-----------------------------------------------------|
| Measure | Same bakeoff discipline as ZGC vs G1 — app p99, CPU, RSS, throughput |
| Generational | Try when throughput/efficiency of single-gen Shenandoah is insufficient — verify with metrics |
| Ops | Know how to read Shenandoah-specific log lines; pin collector in deploy config |

## Production Scenario — gen mode bakeoff

Non-gen Shenandoah meets pause SLO but burns extra CPU. Enable `ShenandoahGCMode=generational`; young death rate high → CPU down, pauses still OK. Keep only if evidence holds across peak week.

## When Not to Use

- Default G1 already meets SLOs  
- Unsupported/unfamiliar platform build  
- Switching collectors to mask a leak  

## Trade-offs

Competes with ZGC on low-pause goals. Choice depends on JDK build, platform, and measured workload — not brand preference. See [trade-offs.md](./trade-offs.md).

## Interview / PE

Enable flags? Generational status on Java 25 vs default mode? STW fully gone? (No.) How does it compare to ZGC at Principal level? (Measure both; explain barriers/CPU trade.)

### Related

[zgc.md](./zgc.md) · [g1-gc.md](./g1-gc.md) · [trade-offs.md](./trade-offs.md) · [diagnostics.md](./diagnostics.md)

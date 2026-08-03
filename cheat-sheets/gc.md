# GC — Cheat Sheet

**Sources:** [../garbage-collection/README.md](../garbage-collection/README.md) · [trade-offs](../garbage-collection/trade-offs.md) · [g1-gc](../garbage-collection/g1-gc.md) · [zgc](../garbage-collection/zgc.md) · [shenandoah](../garbage-collection/shenandoah.md) · [java-interview-questions/gc](../java-interview-questions/gc/)

## PE rule (chapter)

**Measure allocation rate, pause distribution, live set after GC before changing collectors/flags.** `MaxGCPauseMillis` is a goal, not a contract.

## Generational sketch

```text
Allocate → die young (minor) OR promote → old reclaim → Full GC = stress signal
```

## Collector chooser (directional — validate)

| Priority | Lean toward | Enable / note |
|----------|-------------|----------------|
| Balanced server default | **G1** | Typical default on server-class; see chapter accuracy notes |
| Ultra-low pause, large heap | **ZGC** | `-XX:+UseZGC`; **generational** on modern JDKs incl. 25 |
| Low pause alternative | **Shenandoah** | `-XX:+UseShenandoahGC`; generational via JEP 521 **mode** — default mode still non-gen unless set |

**Do not claim for Java 25:** JEP 523 “G1 everywhere” — explicitly **not** Java 25 ([GC README accuracy](../garbage-collection/README.md)).

## Diagnose before tuning

| Observation | Direction |
|-------------|-----------|
| Frequent young GC, small live set | High **allocation rate** — fix churn |
| Live set climbs after GC | **Retention** / leak — dump |
| Humongous (G1) | Huge objects — shrink / region awareness |
| Blind flag paste | Revert; one change + measure |

Bank: [gc questions](../java-interview-questions/gc/)

## Logs / evidence

GC logs + JFR — [gc-logs](../garbage-collection/gc-logs.md) · [diagnostics](../garbage-collection/diagnostics.md)

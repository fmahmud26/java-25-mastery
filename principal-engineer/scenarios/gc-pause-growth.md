# Scenario: GC Pauses / Allocation Storm Eating the SLO

## Context

p99 climbs; GC logs show frequent young collections or rising pause time. Someone proposes “switch to ZGC today.”

## Constraints

- Java 25; G1 is current collector unless proven otherwise  
- Change window exists, but one change at a time  

## Options

| Option | Approach |
|--------|----------|
| **A. Blind collector swap** | `-XX:+UseZGC` immediately |
| **B. Measure first** | Alloc rate, live set after GC, pause distribution |
| **C. Cut allocation / retention** | Churn fix, leak fix, object reuse where measured |
| **D. Heap / G1 tune** | Region/pause goals after B |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Narrative speed | May hide leak; CPU/throughput trade-offs; violates measure-first |
| B | Correct next step | Time |
| C | Durable fix | Eng work |
| D | Marginal wins | Flag folklore if B skipped |

## Decision

**B → C → D; A only after bakeoff with evidence.** Long pauses are a **symptom**. Fix allocation storms and retention before collector shopping ([garbage-collection/trade-offs.md](../../garbage-collection/trade-offs.md)).

Do not claim universal ZGC pause numbers. On Java 25 do **not** use `-XX:+ZGenerational` (generational is the only ZGC mode).

## Reasoning

Frequent young GC + small live set ⇒ churn. Climbing live set after GC ⇒ retention. Collector choice is a later lever.

## Risks

- `MaxGCPauseMillis` treated as a contract  
- Humongous objects ignored on G1  

## Migration

| Wave | Work | Abort |
|------|------|-------|
| 0 | GC log + JFR alloc; after-GC live set | — |
| 1 | Fix top allocators / leak | — |
| 2 | Controlled G1 tune | Pause worse |
| 3 | ZGC/Shenandoah bakeoff | Throughput/CPU budget fail |

## Success metrics

- p99 within SLO  
- Alloc rate / pause p99 improved with before/after artifacts  

Related: [../../scenario-lab/03-high-gc.md](../../scenario-lab/03-high-gc.md) · [../../cheat-sheets/gc.md](../../cheat-sheets/gc.md)

# Interview Prep — Integrated System

This folder teaches **how to think and structure answers** in interviews — not how to memorize scripts.

**Primary path:** tracks + formats + [answer-framework.md](./answer-framework.md).  
**Secondary:** depth packs (seven lenses) — use only after a failed mock exposes a hole ([senior-priorities.md](./senior-priorities.md)).

## Start here

1. [Answer framework](./answer-framework.md) — PCR-OTDR (maps to LLD/SD/PE templates)  
2. Pick a [track](./tracks/) for your next interview type  
3. Run a [format](./formats/) (mock, debug, rapid-fire, …)  
4. Pull chapter depth / Q-bank / scenario-lab when stuck — packs last  

## Answer spine (use every time)

```text
Problem → Context → Reasoning → Options → Trade-offs → Decision → Result
```

Full guide: [answer-framework.md](./answer-framework.md)

## Tracks (interview types)

| Track | Focus | Hub |
|-------|--------|-----|
| Core Java | Language, APIs, contracts | [tracks/core-java.md](./tracks/core-java.md) |
| Coding | Algorithms under time | [tracks/coding.md](./tracks/coding.md) |
| Concurrency | JMM, pools, VT | [tracks/concurrency.md](./tracks/concurrency.md) |
| JVM | Runtime, JIT, classloading | [tracks/jvm.md](./tracks/jvm.md) |
| Performance | Measure → fix → prove | [tracks/performance.md](./tracks/performance.md) |
| LLD | Objects, SOLID, concurrency in design | [tracks/lld.md](./tracks/lld.md) |
| System Design | Capacity, data, scale | [tracks/system-design.md](./tracks/system-design.md) |
| Distributed Systems | Failure, consistency, delivery | [tracks/distributed-systems.md](./tracks/distributed-systems.md) |
| Principal Engineer | Strategy, standards, migrations | [tracks/principal-engineer.md](./tracks/principal-engineer.md) |

## Formats (how you practice)

| Format | What it trains | Path |
|--------|----------------|------|
| Mock interviews | End-to-end timing + structure | [formats/mock-interviews](./formats/mock-interviews/) |
| Scenario questions | Judgment under constraints | [formats/scenarios](./formats/scenarios/) |
| Debugging interviews | Hypothesize → evidence → fix | [formats/debugging](./formats/debugging/) |
| Architecture interviews | Boundaries, evolution, trade-offs | [formats/architecture](./formats/architecture/) |
| Rapid-fire | Crisp models without panic | [formats/rapid-fire](./formats/rapid-fire/) |
| Deep-dive | One topic to production depth | [formats/deep-dive](./formats/deep-dive/) |

## Repo map (don’t reinvent)

| Need | Go to |
|------|--------|
| Question bank by category | [../java-interview-questions](../java-interview-questions/) |
| Incident investigations (spoilers last) | [../scenario-lab](../scenario-lab/) |
| Cheat sheets (revision only) | [../cheat-sheets](../cheat-sheets/) |
| Coding patterns | [../coding-problems](../coding-problems/) |
| LLD systems | [../low-level-design](../low-level-design/) |
| System designs | [../system-design](../system-design/) |
| Distributed failure labs | [../system-design/distributed-systems](../system-design/distributed-systems/) |
| PE scenarios | [../principal-engineer/scenarios](../principal-engineer/scenarios/) |
| Executable experiments | [../experiments](../experiments/) |

## Depth packs (secondary)

Topic packs (theory → interview L1–L4) remain for **vertical fill-in** after tracks/formats:

[collections](./collections/) · [concurrency](./concurrency/) · [jvm](./jvm/) · [virtual-threads](./virtual-threads/) · [garbage-collection](./garbage-collection/) · [streams](./streams/) · [oop-solid](./oop-solid/) · [completable-future](./completable-future/) · [java-evolution](./java-evolution/) · [system-design](./system-design/) · [low-level-design](./low-level-design/)

See [dimensions.md](./dimensions.md) · [levels.md](./levels.md) · [how-to-study.md](./how-to-study.md)

## Anti-goals

- Memorizing “perfect” answers  
- Trivia without constraints  
- Skipping measurement in performance talks  
- Jumping to microservices/VT/Kafka without a bottleneck  
- Studying all depth packs before running a timed mock  

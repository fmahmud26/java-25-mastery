# Performance Notes — JDK 25

**Rule:** measure on your workload. Do not treat JEP motivation benchmarks as SLOs.

## Levers introduced or productized in 25

| Lever | JEP | How to think |
|-------|-----|----------------|
| Compact object headers | 519 | Opt-in; may reduce heap/GC pressure for huge object counts — **A/B test** |
| AOT cache ergonomics | 514 | Faster *startup* when training is representative |
| AOT method profiles | 515 | Faster *warmup to peak* when training matches production |
| Generational Shenandoah mode | 521 | Latency/throughput trade-offs — bakeoff |
| JFR CPU-time (exp.) | 509 | Better CPU profiles on Linux → optimize the right methods |
| Scoped Values | 506 | Lower overhead context vs ThreadLocal at huge VT counts — measure |

## Anti-claims

- “Java 25 is X% faster” — **unsupported** as a blanket statement.  
- Compact headers are **not** default in 25.  
- Preview/incubator features are not performance commitments.

### Related

[jvm-capabilities.md](./jvm-capabilities.md) · [features/jep-519-compact-object-headers.md](./features/jep-519-compact-object-headers.md)

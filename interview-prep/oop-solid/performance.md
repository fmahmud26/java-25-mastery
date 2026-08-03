# OOP + SOLID — Performance

OOP/SOLID are design tools — performance shows up when abstraction becomes **allocation or dispatch tax**.

## Cost awareness

| Choice | Cost note |
|--------|-----------|
| Deep virtual chains | Usually fine; don’t micro-optimize dispatch |
| Strategy objects per call | Allocation + GC if created hot-path |
| Records | Cheap immutable carriers; good for DTOs/events |
| Reflection / proxies for “DIP” | Measurable overhead; reserve for frameworks |
| Inheritance of huge mutable graphs | Cache misses, accidental shared mutable state |

## Degradation smells

1. God service allocating short-lived strategy objects every request.  
2. Premature abstraction layers with no variation → indirection without benefit.  
3. Mutable shared domain objects across threads (correctness first, then perf).  
4. Overuse of inheritance → megamorphic call sites (rare; profile before claiming).

## Measurement

- JFR / async-profiler: hot `invoke*` vs allocations of policy objects.  
- Prefer reuse of immutable strategies as singletons/beans when safe.

Related: [../../performance-engineering](../../performance-engineering/), [immutability.md](../../modern-java-engineering/immutability.md).

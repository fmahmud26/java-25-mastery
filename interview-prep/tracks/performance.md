# Track: Performance Interviews

## What they test

Measure before optimize; p99 budgets; allocation vs retention; honest benchmarking; wrong-layer optimization.

## PCR-OTDR emphasis

- Context must include SLO and evidence  
- Options ranked by **impact × confidence**  
- Result = re-measure  

## Practice sources

- Bank: [../../java-interview-questions/performance](../../java-interview-questions/performance/) · [gc](../../java-interview-questions/gc/)  
- Depth: [../garbage-collection](../garbage-collection/) · [../../performance-engineering](../../performance-engineering/)  
- Experiments: [../../experiments/nanotime-measurement-pitfalls](../../experiments/nanotime-measurement-pitfalls/)  
- Formats: [../formats/scenarios/p99-regression.md](../formats/scenarios/p99-regression.md) · [../formats/architecture/latency-budget.md](../formats/architecture/latency-budget.md)

## Never

Cite unmeasured microbench as fact. Say “I’d JMH/JFR/verify in prod.”

## Loop

Scenario with red herring → attribute latency → pick top fix → define validation.

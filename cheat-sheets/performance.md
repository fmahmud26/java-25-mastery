# Performance — Cheat Sheet

**Sources:** [../performance-engineering/README.md](../performance-engineering/README.md) · [scientific-method](../performance-engineering/scientific-method.md) · [latency](../performance-engineering/latency.md) · [tail-latency](../performance-engineering/tail-latency.md) · [tools/jmh](../performance-engineering/tools/jmh.md) · [java-interview-questions/performance](../java-interview-questions/performance/) · [experiments/nanotime-measurement-pitfalls](../experiments/nanotime-measurement-pitfalls/)

## Non-negotiable loop

```text
Measure → Hypothesize → Experiment → Analyze → Optimize → Re-measure
```

PE rule: **Flame graph without hypothesis = sightseeing; optimize without baseline = folklore.**

## Metrics vocabulary

| Metric | Use |
|--------|-----|
| Throughput | Work / time |
| Latency p50/p95/p99 | User experience — not averages alone |
| Allocation / GC pressure | Churn vs pauses |
| Contention / pool wait | Concurrency bottlenecks |

Depth: [metrics-vocabulary](../performance-engineering/metrics-vocabulary.md) · [throughput](../performance-engineering/throughput.md)

## Tool picker

| Question | Tool |
|----------|------|
| Where is CPU time? | JFR / JMC CPU |
| Where are allocations? | JFR allocation |
| Threads stuck? | jstack / thread analysis |
| Microbench API change? | **JMH** (not raw `nanoTime`) |
| Retained memory? | Heap dump |

[tools/](../performance-engineering/tools/) · [jfr](../performance-engineering/jfr.md)

## Interview anti-patterns (bank)

| Bad | Better |
|-----|--------|
| Single `nanoTime` “40% faster” | JMH + prod metrics — [q01](../java-interview-questions/performance/q01-jmh-vs-nanotime.md) |
| Optimize JSON (+8%) while DB is 70% | Attribute p99 budget first — [q04](../java-interview-questions/performance/q04-p99-budget.md) · [q05](../java-interview-questions/performance/q05-optimize-wrong-layer.md) |

## p99 budget sketch

Break SLO into edge + app + cache + DB + dep + margin — [system-design latency](../system-design/fundamentals/latency.md) · [architecture latency-budget drill](../interview-prep/formats/architecture/latency-budget.md)

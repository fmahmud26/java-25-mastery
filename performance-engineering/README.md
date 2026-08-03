# Performance Engineering — Java Laboratory

Professional discipline: **measure before you change**, change one variable, re-measure. Never claim “X is faster” without stating **workload, environment, metric, and method**.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Scientific loop (non-negotiable)

```text
Measure → Hypothesize → Experiment → Analyze → Optimize → Re-measure
```

Details: [scientific-method.md](./scientific-method.md)

## Study path

1. Mindset: [scientific-method](./scientific-method.md) · [profiling](./profiling.md) · [metrics-vocabulary](./metrics-vocabulary.md)  
2. Metrics: [throughput](./throughput.md) · [latency](./latency.md) · [tail-latency](./tail-latency.md) · [gc-pressure](./gc-pressure.md) · [contention](./contention.md)  
3. Tools: [jfr](./jfr.md) · [jmc](./jmc.md) · [jcmd](./jcmd.md) · [jstack](./jstack.md) · [heap-dumps](./heap-dumps.md) · [jmh](./tools/jmh.md) · [tools/](./tools/)  
4. Profiling modes: [cpu-profiling](./cpu-profiling.md) · [allocation-profiling](./allocation-profiling.md) · [memory-profiling](./memory-profiling.md) · [thread-analysis](./thread-analysis.md)  
5. Practice: [benchmarking](./benchmarking.md) · [experiments/](./experiments/) · [incidents](./incidents.md) · [interview](./interview.md)

## One-line PE rule

**A flame graph without a hypothesis is sightseeing; an optimization without a baseline is folklore.**

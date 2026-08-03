# Experiments Index — Hands-on Lab

Run locally. Record JDK, OS, CPU count, and exact commands. **No universal “faster” claims.**

| # | Experiment | Teaches |
|---|------------|---------|
| 01 | [JMH basics](./01-jmh-basics.md) | Warmup, forks, Blackhole |
| 02 | [CPU hotspot](./02-cpu-hotspot.md) | JFR/async-profiler attribution |
| 03 | [Allocation churn](./03-allocation-churn.md) | Alloc profile → GC pressure |
| 04 | [Lock contention](./04-lock-contention.md) | jstack + JFR monitors |
| 05 | [Tail latency vs GC](./05-tail-latency-gc.md) | Correlate p99 with pauses |
| 06 | [Heap dump retention](./06-heap-dump-retention.md) | Dump path-to-root |

## Lab report template

```text
Question:
Workload:
Environment (JDK/flags/machine):
Baseline result:
Hypothesis:
Change:
New result:
Conclusion (scoped to this workload):
Artifacts:
```

### Related

[../scientific-method.md](../scientific-method.md) · [../benchmarking.md](../benchmarking.md)

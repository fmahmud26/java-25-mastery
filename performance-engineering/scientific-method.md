# Scientific Method for Java Performance

## The loop

```text
1. Measure     — capture baseline under a defined workload
2. Hypothesize — name the bottleneck mechanism
3. Experiment  — change ONE variable
4. Analyze     — compare against baseline with the same metric
5. Optimize    — keep only changes that win on the agreed metric
6. Re-measure  — confirm on the same workload; watch regressions
```

## Measure

Define before running:

| Dimension | Example |
|-----------|---------|
| Workload | “10k RPS checkout, p50 payload 2KB, steady 10 min” |
| Environment | JDK 25, G1, 4 CPU, 8GB cgroup, warm JVM |
| Metric | p99 latency, success rate, CPU, alloc/s |
| Method | JFR 60s + load generator stats |

Without these, “faster” is undefined.

## Hypothesize

Good: “p99 spikes correlate with Full GC; live set too large for heap.”  
Bad: “Need ZGC” / “Need virtual threads” (solutions before mechanism).

## Experiment

- One change: flag, algorithm, pool size, collector, …  
- Hold workload and machine quietness constant.  
- Prefer A/B or before/after with ≥3 runs when noise is high.

## Analyze

- Effect size vs noise (don’t crown a 1% win on a noisy laptop).  
- Check **secondary metrics** (throughput up but error rate up ⇒ not a win).  
- Separate app CPU vs GC CPU vs Compiler threads.

## Optimize

Ship the smallest change that improves the **agreed** metric without breaking SLOs on others.

## Re-measure

- Same harness.  
- Cold vs warm stated explicitly.  
- Record JDK, flags, commit SHA, load script version.

## Anti-patterns

| Anti-pattern | Why it fails |
|--------------|--------------|
| `System.nanoTime` in `main` as truth | JIT/DCE/warmup lies |
| Tuning 12 GC flags at once | No attribution |
| Microbenchmark ≠ production | Different allocation/JIT/GC |
| Declaring victory on mean only | Tails hide pain |

### Related

[metrics-vocabulary.md](./metrics-vocabulary.md) · [experiments/README.md](./experiments/README.md) · [incidents.md](./incidents.md)

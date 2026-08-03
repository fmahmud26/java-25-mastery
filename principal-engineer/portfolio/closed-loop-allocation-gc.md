# Closed Loop — Allocation Pressure → GC Evidence

Portfolio proof that performance talk is not slogans: measure → change → re-measure on **this** JDK.

## Run (owned)

```bash
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk-amd64   # if needed
export PATH="$JAVA_HOME/bin:$PATH"
cd experiments/allocation-rate-pressure && bash run.sh
```

## Observed (2026-08-03, JDK 25.0.3, Linux 7.0.0-28-generic x86_64)

From [allocation-rate-pressure/EXPERIMENT.md](../experiments/allocation-rate-pressure/EXPERIMENT.md):

- Allocating path completed **42,213,131** iterations in the timed window; reuse path **30,230,967** (same wall budget — throughput shape differs by allocation churn).
- GC log showed multiple young collections (through GC(43) in the captured run) with short pauses (~0.36–0.69 ms in that log) under the allocating mode.

Exact pasted lines live in the experiment file — do not restate folklore beyond that run.

## Decision loop (PE)

1. **Hypothesis:** allocation churn drives GC frequency more than “need ZGC.”  
2. **Experiment:** allocating vs reuse buffers.  
3. **Analyze:** young GC count / pause lines correlate with churn mode.  
4. **Optimize:** cut alloc on hot path before collector shopping.  
5. **Refuse:** blind `-XX:+UseZGC` as step 1 ([refusals.md](./refusals.md) §5).

## Interview line

“I don’t tune GC from p99 alone — I show allocation-rate evidence from a lab on JDK 25, then decide.”

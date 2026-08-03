# Experiment: JIT warmup — cold vs hot (JVM / JIT)

## Hypothesis

A compute-heavy method runs slower on the **first** iterations (interpreted / C1) than after sufficient warmup when C2 (or top tier) optimizes it — median times drop after warmup on the same JVM process.

## Setup

JDK 25; no Graal native. Same method timed in batches before/after warmup loops. Teaching microbench; tiered compilation default.

## Code

```bash
./run.sh
```

## Expected behavior

Early batches slower / higher variance; later batches faster median. Absolute speedups vary.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: each sample = 50000 crunch() calls; early (cold-ish) median=1.226ms min=0.841ms max=2.897ms; after warmup median=0.787ms min=0.783ms max=0.790ms
- Production implication: Warmup cut median from 1.226ms to 0.787ms and collapsed max–min spread, so quote steady-state (post-warmup) latency—not first-batch times—for capacity and SLOs.

```text
Each sample = time for 50000 crunch() calls (not JMH)
early (cold-ish) median=1.226ms min=0.841ms max=2.897ms
after warmup median=0.787ms min=0.783ms max=0.790ms
```

## Explanation

HotSpot tiers: interpret → C1 → C2 with profiling. Steady-state performance ≠ first-request performance (also relevant to serverless cold starts, though that’s broader).

## Production implication

Warm critical paths before taking traffic (or accept cold latency). Benchmarks without warmup lie. Canaries need warmup too.

## Interview takeaway

“I never quote first-iteration times as steady-state JVM performance.”

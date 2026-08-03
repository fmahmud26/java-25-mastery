# Experiment: `System.nanoTime` measurement pitfalls (Performance)

## Hypothesis

A naive “benchmark” that times one call or skips warmup will report **noisy, non-reproducible** numbers; empty timed regions and JVM/GC effects can dominate. Printing min/median/max across runs shows variance — teaching why JMH exists.

## Setup

JDK 25; deliberately bad measurement vs slightly better (warmup + multiple samples). No JMH — the point is humility.

## Code

```bash
./run.sh
```

## Expected behavior

Single-shot times jump around; medians of many runs more stable but still not publication-grade. Program prints warnings.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: single-shot 1263/1092/331/320/320 ns; empty timed region 70/50/50/60/50 ns; warmed batch median=213832 ns min=36777 max=410172; sink=2095690712
- Production implication: Single-shots spanned 320–1263 ns and batch min–max spread (36777–410172) shows nanoTime alone is too noisy for ship decisions—use JMH/JFR/prod metrics instead.

```text
WARNING: This demonstrates measurement noise. Not a real benchmark. Not JMH.
-- single-shot (bad) --
shot 0: 1263 ns
shot 1: 1092 ns
shot 2: 331 ns
shot 3: 320 ns
shot 4: 320 ns
-- empty timed region (timer noise) --
empty 0: 70 ns
empty 1: 50 ns
empty 2: 50 ns
empty 3: 60 ns
empty 4: 50 ns
-- warmed batch medians (still not JMH) --
batch median=213832 ns min=36777 max=410172 (spread shows noise)
sink=2095690712
```

## Explanation

Timers have granularity; JIT changes code underneath; GC pauses skew samples; dead code elimination can erase work without blackholes. JMH addresses these; these labs do not.

## Production implication

Don’t ship optimizations based on one `nanoTime` delta. Use JMH/JFR/prod metrics for decisions.

## Interview takeaway

“I refuse to present unmeasured or single-shot microbench claims as facts — here’s how measurement goes wrong.”

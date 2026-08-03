# Experiment: Parallel streams when slower

## Hypothesis

For a **tiny** workload (small `n`, cheap per-element work), `parallel()` is often **slower** than sequential due to fork-join overhead. For a large, CPU-heavy, associative reduction, parallel can be faster — measure both; don’t assume parallel is always better.

## Setup

JDK 25; compare sequential vs parallel on small vs large CPU-ish work. Teaching microbench with warmup.

## Code

```bash
./run.sh
```

## Expected behavior

Small/cheap: parallel ≥ sequential time (often slower). Large/heavy: parallel may win on multi-core. Record both on your machine.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: tiny n=2000 cheap seq median=0.01ms par=0.17ms; large n=20_000_000 cheap seq=5.17ms par=0.88ms; large n=2_000_000 heavier seq=0.61ms par=0.32ms
- Production implication: Parallel was slower on tiny n (0.17 vs 0.01ms) but faster on large/cheap and heavier workloads—default to sequential streams and parallel only after measurement.

```text
Teaching microbench (not JMH). Warmup...
tiny n=2_000 cheap
  sequential median=0.01ms
  parallel   median=0.17ms
large n=20_000_000 cheap
  sequential median=5.17ms
  parallel   median=0.88ms
large n=2_000_000 heavier
  sequential median=0.61ms
  parallel   median=0.32ms
```

## Explanation

Parallel streams use the common ForkJoinPool. Splitting/merging costs dominate tiny tasks. Also: parallel is the wrong tool for blocking I/O (prefer VT).

## Production implication

Default to sequential streams; parallel only after measurement and for pure CPU over large data. Never parallelize with shared mutable state.

## Interview takeaway

“Parallel streams aren’t free parallelism — I can show a case where they lose.”

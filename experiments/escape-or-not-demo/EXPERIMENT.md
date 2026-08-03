# Experiment: Escape analysis — stack-eligible vs escaping objects (Memory / JIT)

## Hypothesis

A micro-workload that allocates objects **not escaping** a loop can be optimized more aggressively by the JIT (scalar replacement / fewer observable allocations) than the same shape where objects are stored in a heap list — visible as higher iteration counts and/or lower allocation pressure after warmup. Results depend on JIT; verify with logs/JFR, don’t claim EA always fires.

## Setup

JDK 25; warmup then measure; optional `-XX:+PrintEscapeAnalysis` is diagnostic and noisy — prefer comparing iteration counts after warmup. Teaching only.

## Code

```bash
./run.sh
```

## Expected behavior

After warmup, non-escaping variant often completes **more** iterations in a fixed window than escaping variant. If not, say so — EA is not guaranteed for every pattern.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: measured window ms=1500; non-escaping batches=15269244 sink=0; escaping-list batches=1571547 sink=65280; ratio nonEsc/esc iters ≈ 9.72
- Production implication: Non-escaping completed ~9.7× more batches in the same 1500ms window—write clear code and measure allocation with JFR rather than rewriting for escape analysis.

```text
Warmup...
Measured window ms=1500 (teaching; not JMH)
non-escaping: batches=15269244 sink=0
escaping-list: batches=1571547 sink=65280
ratio nonEsc/esc iters ≈ 9.72 (local only)
```

## Explanation

Escape analysis proves an object doesn’t escape the compiling method/thread; JIT may scalar-replace fields. Storing into a heap structure forces real allocations.

## Production implication

Don’t rewrite business code “for EA.” Write clear code; use JFR if allocation dominates. Understanding EA explains surprising JIT wins.

## Interview takeaway

“Escape analysis may remove allocations; I verify with tools, I don’t assume it.”

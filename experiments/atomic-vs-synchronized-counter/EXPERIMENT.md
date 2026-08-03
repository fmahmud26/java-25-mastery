# Experiment: AtomicInteger vs synchronized counter

## Hypothesis

For a single hot counter, `AtomicInteger.incrementAndGet` outperforms `synchronized` increment under multi-threaded contention, because CAS avoids heavyweight monitor paths in the uncontended/light-contended case (and often under moderate contention). Exact crossover depends on hardware.

## Setup

JDK 25; fixed duration; teaching harness.

## Code

```bash
./run.sh
```

## Expected behavior

AtomicInteger ops/s typically ≥ synchronized for this simple increment. Verify locally.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: threads=8 seconds=2; AtomicInteger ops/s≈50601390; synchronized ops/s≈9507277
- Production implication: Under this 8-thread contention, AtomicInteger delivered ~5.3× the ops/s of synchronized increments, so use atomics (or LongAdder) for hot counters instead of synchronizing whole critical sections.

```text
threads=8 seconds=2 (not JMH)
AtomicInteger ops/s≈50601390
synchronized ops/s≈9507277
```

## Explanation

Atomics use CAS loops. Monitors can inflate under contention. For simple counters prefer atomics/LongAdder; for compound invariants prefer locks or lock-free structures carefully.

## Production implication

Don’t synchronize entire services to bump a counter. Use atomics/metrics libraries.

## Interview takeaway

“Atomics for simple state; locks for invariants spanning multiple fields.”

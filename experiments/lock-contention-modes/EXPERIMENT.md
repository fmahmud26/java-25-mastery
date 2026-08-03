# Experiment: Lock contention modes

## Hypothesis

Under high update contention on one counter, `synchronized` / `ReentrantLock` serialize throughput, while `LongAdder` (striped atomics) sustains higher ops/s. Uncontended locks are cheap enough that differences may be small — measure both contending and disjoint cases if extending.

## Setup

JDK 25; fixed-duration multi-VT updates; compare synchronized, ReentrantLock, LongAdder. Teaching harness.

## Code

```bash
./run.sh
./run.sh 8 2
```

## Expected behavior

Under contention, LongAdder ops/s ≫ lock-based counters. Confirm locally.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: threads=8 seconds=2; synchronized ops/s≈11369364; ReentrantLock ops/s≈14995969; LongAdder ops/s≈255564709
- Production implication: LongAdder delivered ~22× synchronized and ~17× ReentrantLock ops/s for hot counters—prefer LongAdder for contended metrics, locks for complex invariants.

```text
threads=8 seconds=2 (not JMH)
synchronized ops/s≈11369364
ReentrantLock ops/s≈14995969
LongAdder ops/s≈255564709
```

## Explanation

Locks force single-threaded critical sections. LongAdder spreads hot spots across cells, summing on read — great for metrics, not for needing instantaneous exact reads every time.

## Production implication

Hot metrics → LongAdder. Mutual exclusion for complex invariants → locks/atomics CAS with clear ordering. Don’t lock around I/O.

## Interview takeaway

“I match synchronization tool to contention and correctness needs.”

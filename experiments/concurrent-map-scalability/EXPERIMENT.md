# Experiment: ConcurrentHashMap vs synchronized HashMap scalability

## Hypothesis

Under multi-threaded `compute`/`merge` style updates, `ConcurrentHashMap` sustains higher throughput than a `Collections.synchronizedMap(new HashMap<>())` because CHM uses finer-grained concurrency while synchronized maps lock the entire map.

## Setup

JDK 25; `T` virtual or platform threads updating disjoint/overlapping keys; count ops in fixed duration window. Teaching harness — not JMH.

## Code

```bash
./run.sh
./run.sh 8 3
```

Args: `threads seconds`

## Expected behavior

CHM ops/s **higher** than synchronized HashMap as threads increase (especially with some key contention). Confirm on your machine.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: threads=8 seconds=2; ConcurrentHashMap ops/s≈62467137; synchronized HashMap ops/s≈8255314
- Production implication: Under this 8-thread contention, ConcurrentHashMap delivered ~7.6× the ops/s of synchronized HashMap—use CHM (or striped structures) for shared concurrent maps.

```text
threads=8 seconds=2 (teaching harness, not JMH)
ConcurrentHashMap: ops/s≈62467137 (local only)
synchronized HashMap: ops/s≈8255314 (local only)
```

## Explanation

Synchronized wrapper serializes all access. CHM spreads contention across bins/nodes. Correctness still requires using CHM atomic methods — not check-then-act externally.

## Production implication

Shared caches/counters → CHM or striped structures. Synchronized `HashMap` is a hidden global lock.

## Interview takeaway

“I choose ConcurrentHashMap for concurrent maps and can explain why synchronized HashMap doesn’t scale.”

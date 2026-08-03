# Experiment: CompletableFuture fan-out / fan-in

## Hypothesis

Independent blocking calls composed with `CompletableFuture` + virtual-thread executor complete in roughly **max(latency)** wall time, whereas sequential awaits sum latencies.

## Setup

JDK 25; three staged sleeps composed async vs sequential. Teaching demo — wall times only local.

## Code

```bash
./run.sh
```

## Expected behavior

Async fan-out wall ≈ longest branch; sequential ≈ sum. Confirm locally.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: sequential wallMs=360; async fan-out wallMs=136
- Production implication: Fan-out finished in 136ms vs 360ms sequential (~2.6×)—overlap independent async I/O stages with budgets/timeouts.

```text
sequential wallMs=360
async fan-out wallMs=136 (teaching; not JMH)
```

## Explanation

CF schedules independent stages concurrently when executor allows. Sequential `get` on each stage cannot overlap.

## Production implication

Fan-out independent I/O with budgets/timeouts; don’t parallelize CPU-bound blindly. Handle exceptions with `handle`/`exceptionally`.

## Interview takeaway

“I use CF for concurrent I/O composition and always set timeouts.”

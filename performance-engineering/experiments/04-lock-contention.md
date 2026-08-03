# Experiment 04 — Lock Contention

## Question

What happens to p99 when all requests share one `synchronized` lock around a small critical section vs striped locks?

## Workload

N threads (or virtual threads) hitting a counter/map. Ramp concurrency 1→#CPUs→4×CPUs.

## Measure

```bash
jcmd <pid> Thread.print   # during ramp
jcmd <pid> JFR.start name=lock settings=profile duration=30s filename=/tmp/lock.jfr
```

Record throughput and latency histogram per concurrency level.

## Hypothesize

Global lock → BLOCKED stacks + flat throughput curve past a point.

## Experiment

Replace with `ConcurrentHashMap` / striped locks / `LongAdder` as appropriate → same ramp.

## Analyze

Plot RPS vs concurrency; annotate when MonitorBlocked dominates.

## Claim

Only for this micro-contention workload; real apps need end-to-end re-validation.

### Related

[../contention.md](../contention.md) · [../jstack.md](../jstack.md)

# Experiment 05 — Tail Latency vs GC

## Question

Do observed p99 spikes line up with GC pauses under allocation load?

## Workload

Service allocating steadily (e.g. build and discard medium graphs) at fixed RPS for 10–15 minutes. Heap intentionally modest so GC is visible — document `-Xmx`.

## Measure

- Load tool latency CSV with timestamps  
- `-Xlog:gc*:file=gc.log:uptime,level,tags`  
- Optional JFR  

## Hypothesize

Spikes align with STW pauses within ~pause duration.

## Analyze

If spikes **don’t** align, hypothesize contention/IO/TTSP next — don’t “tune GC” blindly.

## Experiment

Increase heap **or** cut allocation (one change) → re-run → compare p99 and pause distribution.

## Claim

“Under W with Xmx=__, p99 spikes correlated with GC pauses of ~__ms; after __, p99 __→__.”

### Related

[../tail-latency.md](../tail-latency.md) · [../gc-pressure.md](../gc-pressure.md)

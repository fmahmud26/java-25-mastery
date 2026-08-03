# Experiment: Allocation rate pressure (Memory / GC)

## Hypothesis

A loop that allocates many short-lived objects will show higher young-GC activity and lower effective throughput than an equivalent loop that reuses a buffer / avoids per-iteration allocation — visible via GC logs and completed iterations in a fixed time.

## Setup

- JDK 25
- Run **twice**: allocating vs reuse
- Enable GC logging: flags in `run.sh`
- Compare iterations completed and GC summary lines — **not** a formal latency SLO study

## Code

```bash
./run.sh
```

## Expected behavior

Allocating mode: fewer iterations and/or more GC pause lines than reuse mode (directional). Exact GC counts vary by collector/heap.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: allocating iters=42213131 sink=1187847; reuse-buffer iters=30230967 sink=592337061; GC log reached GC(43) Pause Young (G1) with sample pauses 0.685ms / 0.371ms / 0.361ms; heap exit used 69596K of 131072K committed
- Production implication: Even when raw iteration counts favor the allocating path here, young-GC activity through GC(43) shows allocation still drives GC work—profile hot paths with GC/JFR before optimizing on throughput alone.

```text
Teaching demo with GC logs — not a published benchmark.
allocating: iters=42213131 sink=1187847
reuse-buffer: iters=30230967 sink=592337061
--- gc.log (tail) ---
GC(41) Pause Young (Normal) (G1 Evacuation Pause) 76M->1M(128M) 0.685ms
GC(42) Pause Young (Normal) (G1 Evacuation Pause) 77M->1M(128M) 0.371ms
GC(43) Pause Young (Normal) (G1 Evacuation Pause) 77M->1M(128M) 0.361ms
Heap: garbage-first total reserved 131072K, committed 131072K, used 69596K
```

## Explanation

Short-lived objects fill eden → minor GC. Allocation is cheap individually but rate × size drives GC work. Reuse cuts allocate/GC pressure.

## Production implication

Hot paths: avoid per-request heavy allocation (string concat in loops, boxed churn). Profile with JFR allocation samples — don’t guess.

## Interview takeaway

“I correlate allocation rate with GC behavior and verify with GC/JFR logs, not folklore.”

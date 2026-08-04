# Allocation Profiling

Find where **bytes/objects** are allocated — primary lever for GC pressure and many tail-latency issues.

## Mental Model

```text
Allocation rate ↑ → young GC frequency ↑ → CPU + pause noise ↑ → p99 risk
Retained live set ↑ → old-gen / Full GC risk (different problem)
```

Do not confuse **churn** (allocate & die) with **leak** (allocate & retain).

## Measure

```bash
jcmd <pid> JFR.start name=alloc settings=profile duration=60s filename=/tmp/alloc.jfr
# async-profiler: asprof -e alloc -d 60 -f /tmp/alloc.html <pid>
```

In JMC: allocation pressure, TLAB events, top allocated types. Pair with GC logs.

## Hypothesize

Per-request `byte[]`, boxing in hot loops, string concat, JSON trees, logging, defensive copies in libraries.

## Production Scenario — pretty logs in hot path

Alloc profile: 40% bytes in log layout. Log level left at DEBUG in prod. Fix level + parameterized logging; young GC rate drops; p99 improves without “GC tuning.”

## Experiment

Reuse buffers / stream / avoid boxing → measure allocation rate + young GC frequency + p99 under **same** RPS.

## When Not to Micro-Optimize Alloc

Off the critical path; allocation rate already low; GC time fraction tiny vs dependency latency.

## Claim template

“Under W, JFR alloc profiling showed 62% bytes in `ResponseRenderer`; after change, alloc rate −40%, young GC/min −35%, p99 −12ms; throughput unchanged.”

### Related

[gc-pressure.md](./gc-pressure.md) · [cpu-profiling.md](./cpu-profiling.md) · [tools/async-profiler.md](./tools/async-profiler.md) · [experiments/03-allocation-churn.md](./experiments/03-allocation-churn.md)

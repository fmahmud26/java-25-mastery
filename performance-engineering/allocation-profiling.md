# Allocation Profiling

Find where **bytes/objects** are allocated — primary lever for GC pressure.

## Measure

```bash
jcmd <pid> JFR.start name=alloc settings=profile duration=60s filename=/tmp/alloc.jfr
# async-profiler: asprof -e alloc -d 60 -f /tmp/alloc.html <pid>
```

In JMC: allocation pressure, TLAB events, top allocated types.

## Hypothesize

Per-request `byte[]`, boxing in hot loops, string concat, JSON trees, logging.

## Experiment

Reuse buffers / stream / avoid boxing → measure allocation rate + young GC frequency + p99 under **same** RPS.

## Claim template

“Under W, JFR alloc profiling showed 62% bytes in `ResponseRenderer`; after change, alloc rate −40%, young GC/min −35%, p99 −12ms; throughput unchanged.”

### Related

[gc-pressure.md](./gc-pressure.md) · [cpu-profiling.md](./cpu-profiling.md) · [experiments/03-allocation-churn.md](./experiments/03-allocation-churn.md)

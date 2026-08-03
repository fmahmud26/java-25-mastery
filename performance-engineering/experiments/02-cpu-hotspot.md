# Experiment 02 — CPU Hotspot

## Question

Which method owns CPU under a CPU-bound workload?

## Workload

Loop computing something expensive (e.g. repeated hashing or JSON serialize in a tight server stub) for 2+ minutes. One JVM, fixed heap.

## Measure

```bash
# terminal A: run workload
# terminal B:
jcmd <pid> JFR.start name=cpu settings=profile duration=60s filename=/tmp/cpu.jfr
```

Optional: async-profiler CPU SVG.

## Hypothesize

Name the method you *think* is hot before opening the recording.

## Analyze

JMC hot methods — match or revise hypothesis. Check GC/compiler share of CPU.

## Optimize (optional)

Replace algorithm → re-record → compare CPU% and ops/s **on this workload only**.

## Re-measure

Same duration, warm JVM, same inputs.

### Related

[../cpu-profiling.md](../cpu-profiling.md) · [../jfr.md](../jfr.md)

# Profiling

**Profiling** = observe where resources are consumed under a real or synthetic load — not a substitute for a controlled benchmark when comparing two implementations.

## Mental Model

```text
Symptom (slow / hot / huge)
  → pick resource (CPU, alloc, lock, wall, heap)
    → record with appropriate tool
    → attribute to code / GC / runtime
    → hypothesize → experiment
```

Always anchor to a **user/SLO symptom**. Flame graphs without a hypothesis are sightseeing.

## Modes

| Mode | Question | Start with |
|------|----------|------------|
| CPU | Who burns cycles? | [cpu-profiling.md](./cpu-profiling.md), JFR, [async-profiler](./tools/async-profiler.md) |
| Allocation | Who allocates? | [allocation-profiling.md](./allocation-profiling.md) |
| Memory / retention | Who retains? | [heap-dumps.md](./heap-dumps.md), [memory-profiling.md](./memory-profiling.md) |
| Threads | Blocked / running / deadlocked? | [jstack.md](./jstack.md), [thread-analysis.md](./thread-analysis.md) |
| GC | Pauses / pressure? | [gc-pressure.md](./gc-pressure.md), GC logs + JFR |
| Locks | Contention? | [contention.md](./contention.md), JFR Java Monitor |
| Wall vs CPU | Waiting or computing? | async-profiler `wall` vs `cpu` |

## Tool Choice (quick)

| Need | Prefer |
|------|--------|
| Production timeline (GC+locks+I/O) | [JFR](./jfr.md) |
| Fast CPU/alloc flame graph | [async-profiler](./tools/async-profiler.md) |
| Micro compare A/B | [JMH](./tools/jmh.md) — not a prod profiler |

Unified loop: [jvm-observability.md](./jvm-observability.md).

## Production constraints

- Prefer **JFR** (low overhead, event-rich) for production baselines.  
- Keep recordings short or throttled; treat `.jfr` / dumps as sensitive.  
- Sampling ≠ exact call counts — interpret statistically.  
- Profile the instance that shows the symptom (right pod/region).  

## Production Scenario — wrong mode

CPU profile clean; latency terrible. Wall profile / traces show pool wait. Optimizing JSON parse would have been a waste.

## Never say

“We profiled and X is faster.”  
Say: “Profile showed Y hot; after change Z, under workload W, p99 dropped from … to … (JFR+load test).”

### Related

[jfr.md](./jfr.md) · [scientific-method.md](./scientific-method.md) · [tools/README.md](./tools/README.md)

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

## Modes

| Mode | Question | Start with |
|------|----------|------------|
| CPU | Who burns cycles? | [cpu-profiling.md](./cpu-profiling.md), JFR, async-profiler |
| Allocation | Who allocates? | [allocation-profiling.md](./allocation-profiling.md) |
| Memory / retention | Who retains? | [heap-dumps.md](./heap-dumps.md), [memory-profiling.md](./memory-profiling.md) |
| Threads | Blocked / running / deadlocked? | [jstack.md](./jstack.md), [thread-analysis.md](./thread-analysis.md) |
| GC | Pauses / pressure? | [gc-pressure.md](./gc-pressure.md), GC logs + JFR |
| Locks | Contention? | [contention.md](./contention.md), JFR Java Monitor |

## Production constraints

- Prefer **JFR** (low overhead, event-rich) for production.  
- Keep recordings short or throttled; treat `.jfr` / dumps as sensitive.  
- Sampling ≠ exact call counts — interpret statistically.

## Never say

“We profiled and X is faster.”  
Say: “Profile showed Y hot; after change Z, under workload W, p99 dropped from … to … (JFR+load test).”

### Related

[jfr.md](./jfr.md) · [scientific-method.md](./scientific-method.md) · [tools/README.md](./tools/README.md)

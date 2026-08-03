# Safepoints

A **safepoint** is a program state where the JVM can safely inspect/mutate thread execution state (roots, frames) — required for GC, deoptimization, certain thread dumps, biased-lock revocation (historical), etc.

## Mental Model

```text
JVM needs global quiescence for some operations:
  “Everyone pause at a known-safe place.”
Threads reach a safepoint poll → stop → VM operation runs → resume
```

Not every pause is a GC pause — but many GC pauses use safepoints (collector-dependent; ZGC minimizes global STW).

## Technical Mechanism

- Compiled code polls safepoint checks (e.g. at branches / returns / loop backs — exact policy evolves).  
- **Time-to-safepoint (TTSP)**: how long until all threads arrive.  
- One thread in a huge **uncounted loop** without polls → safepoint lag → system-wide latency.

```bash
-Xlog:safepoint=info
# or historical: -XX:+PrintSafepointStatistics
jcmd <pid> Thread.print
JFR: Safepoint / GC pause events
```

## JVM Internals

| VM operation examples | Needs safepoint? |
|----------------------|------------------|
| Many GC phases | Often yes (varies by GC) |
| Deoptimization | Yes |
| Biased locking revocation (old) | Yes |
| Some JFR / dump ops | Yes |

**Safepoint ≠ only GC.** Interview distinction matters.

## Production Implications

- Latency spikes with low GC time → check safepoint duration / TTSP.  
- JNI / long native without proper transitions can delay reaching safepoints.  
- Counted loops get polls; pathological generated code can still hurt.

## Incident — latency spike

p99 spikes; GC pause logs small; safepoint log shows long “sync” time → thread stuck in tight loop or native. See [incidents.md](./incidents.md).

## Interview / PE

Define safepoint. TTSP meaning? Why can a non-GC safepoint stall traffic? ZGC vs G1 regarding pauses (high level)?

### Related

[deoptimization.md](./deoptimization.md) · [jvm-execution.md](./jvm-execution.md) · [diagnostic-tools.md](./diagnostic-tools.md)

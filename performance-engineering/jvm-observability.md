# JVM Observability — Unified Investigation Loop

Connect **runtime signals** (JFR, GC logs, dumps) with **service signals** (RED metrics, traces, logs) so incidents are evidence-driven.

## Mental Model

```text
User symptom (SLO burn)
  → App: RED + traces (which dependency / endpoint?)
  → JVM: CPU, alloc, GC pauses, safepoints, locks, threads
  → Host: cgroup CPU/mem, disk, network
  → Hypothesis → targeted recording/dump → fix → re-measure
```

App observability without JVM context guesses “slow code.” JVM profiles without request context guess “hot method” that is not on the critical path.

## Signal Map

| Question | App layer | JVM layer |
|----------|-----------|-----------|
| Is it broken? | Error rate, SLO burn | Process up? OOMs? |
| Where is time? | Trace spans | JFR + safepoint / GC logs |
| CPU? | Container CPU throttling | JFR/async-profiler CPU |
| Memory? | — | Heap histo, dump, metaspace |
| Threads stuck? | Pool wait metrics | `jcmd Thread.print` / JFR monitors |
| GC? | Pause-correlated p99 | `-Xlog:gc*` + JFR GC events |

## Production Playbook (first 15 minutes)

1. Confirm blast radius (endpoint, region, dependency).  
2. Check golden signals + dependency RED.  
3. If p99 spikes: overlay GC pause / safepoint time.  
4. If CPU high: 60s JFR `profile` or async-profiler CPU.  
5. If threads / pool wait: thread dump ×3 + pool metrics.  
6. If heap growth: live set after GC; dump only with retention hypothesis.  

Details: [jfr.md](./jfr.md) · [profiling.md](./profiling.md) · [../garbage-collection/diagnostics.md](../garbage-collection/diagnostics.md)

## JVM-Specific SLIs Worth Owning

| SLI | Why |
|-----|-----|
| GC pause p99 / time fraction | Tail correlation |
| Allocation rate | Predicts GC storms |
| Heap after-GC | Leak vs churn |
| Thread / VT count + pinned count | Loom issues |
| DB pool active/wait | Classic saturation |
| Safepoint “ttsp” | Hidden stalls |

## Continuous Profiling Culture

Keep light JFR or continuous profiler in prod with retention budgets. Pull flame graphs **when** metrics say to — not as wallpaper.

## Failure: Metrics Without Owners

Dashboards nobody pages on → silent decline. Pair each SLI with an owner and a runbook link ([principal observability](../principal-engineer/topics/observability.md)).

## Principal Perspective

Observability is how you **buy down MTTR**. Budget cardinality and profiling overhead like any other production feature.

### Related

[../system-design/fundamentals/observability.md](../system-design/fundamentals/observability.md) · [tools/async-profiler.md](./tools/async-profiler.md) · [low-latency-architecture.md](./low-latency-architecture.md)

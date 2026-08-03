# High GC — young gen thrash under “innocent” JSON enrichment

## Incident

`search-api` p99 jumps from 40 ms to 350 ms during a marketing campaign. CPU is elevated but not pinned. Ops reports “GC is angry.” Allocation rate charts look like a sawtooth. A config change enabled richer response facets (`includeExplain=true` defaulted on for the campaign cohort).

## Symptoms

- Young GC count skyrockets; minor pauses individually short but frequent
- Application threads spend noticeable time in GC / TTSP
- Allocation rate multi‑GB/s on a 2‑core pod (illustrative scale)
- Old gen mostly stable — not a classic leak pattern
- Disabling the campaign cohort flag restores latency within minutes

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25**, G1 (`-XX:MaxGCPauseMillis=200`) |
| Heap | `-Xms2g -Xmx2g` (illustrative) |
| QPS | ~2.5k RPS (illustrative) |
| Payload | Search hits 20; facets + per-hit explain trees when enabled |
| GC logging | Unified logging to file (`-Xlog:gc*:file=gc.log:time,uptime,level,tags`) |

## Metrics

```
jvm.gc.young.count / min     8 → 90
jvm.gc.pause.time_sum / min  40ms → 900ms
allocation.rate              ~400 MiB/s → ~2.2 GiB/s
jvm.memory.heap.after_gc     stable ~900Mi
http.p99                     40ms → 350ms
```

Illustrative: high allocation + frequent young GC + stable post-GC heap ⇒ allocation churn, not leak.

## Logs

```
[2026-08-03T16:01:02.100+0000][gc,start] GC(41882) Pause Young (Normal) (G1 Evacuation Pause)
[2026-08-03T16:01:02.104+0000][gc       ] GC(41882) Pause Young 8M->3M(2048M) 3.812ms
[2026-08-03T16:01:02.140+0000][gc,start] GC(41883) Pause Young (Normal) (G1 Evacuation Pause)
... many young GCs per second ...
2026-08-03T16:01:03.221Z DEBUG ExplainRenderer - nodes=842 hitId=H-99102
2026-08-03T16:01:03.225Z DEBUG ExplainRenderer - nodes=901 hitId=H-99103
```

Debug explain logs appear only when the campaign flag path is on.

## Initial Hypotheses

1. Massive temporary object churn from explain-tree / JSON serialization
2. Accidental string concatenation or `StringBuilder` misuse in hot path
3. Autoboxing / stream pipelines allocating per element excessively
4. Humongous allocations / region fragmentation (less likely if young-only)
5. True old-gen leak mislabeled as “high GC” (metrics lean against)

## Questions

- How do you separate “too much allocation” from “heap too small”?
- What is your first tool: GC log summary, JFR allocation profile, or heap dump?
- What assumption hides in “pauses are only 4 ms so GC is fine”?
- If traffic ×100 with explain off, do you still expect this incident?
- Would increasing heap fix the user-visible problem? When is that a bandage?

Cross-links: [garbage-collection/](../garbage-collection/), [performance-engineering/gc-metrics.md](../performance-engineering/gc-metrics.md), [principal-engineer/scenarios/gc-pause-growth.md](../principal-engineer/scenarios/gc-pause-growth.md).

## Investigation

1. **Characterize GC**  
   Parse `gc.log` (or JFR GC): young vs full, pause sum vs count, heap before/after. Stable after-GC ⇒ churn.

2. **Allocation profile**  
   `jcmd <pid> JFR.start settings=profile ...` focusing on **Object Allocation Sample** / TLAB events. Identify top allocating frames (`ExplainRenderer`, Jackson `JsonGenerator`, `ArrayList.grow`).

3. **Diff the flag**  
   Compare flame graphs / alloc profiles with `includeExplain` on vs off under same QPS.

4. **Inspect object shapes**  
   Explain trees with hundreds of nodes × 20 hits × 2.5k RPS ⇒ enormous short-lived graphs. Check whether intermediate `Map`/`List` copies happen during DTO mapping.

5. **Rule out leak**  
   `GC.class_histogram` before/after load window; no monotonic retained growth.

6. **Sizing sanity**  
   `jcmd GC.heap_info`; young region sizing may be suboptimal but root cause is alloc rate — don’t stop at “tune NewRatio.”

7. **Optional JMH**  
   Microbench explain rendering allocations (`-prof gc`) to quantify bytes/op ([performance-engineering/tools/jmh.md](../performance-engineering/tools/jmh.md)).

## Root Cause

Campaign defaulted `includeExplain=true`, building a deep per-hit explanation tree (hundreds of node objects + strings) and serializing it on every search response. Allocation rate overwhelmed young gen; frequent GC increased tail latency even though individual pauses looked “healthy.” Not a memory leak — **allocation amplification** on the hot path.

## Resolution

- Default explain **off**; require explicit opt-in / internal tools only.
- Cap explain nodes; stream/compact format; reuse buffers where safe.
- If product needs facets, precompute or cache explain for top queries — don’t rebuild per request at campaign QPS.

## Prevention

- Alloc-rate and young-GC-time burn alerts tied to search SLO
- Load tests with production-like facet flags
- Review any “debug richness in the response” for bytes/request × RPS
- Prefer structured sampling over full trees in online path

## Principal Engineer Discussion

- When do you tune G1 vs fix the allocator (almost always fix the allocator first)?
- How do you budget **bytes per request** as a first-class SRE signal?
- Trade-off: richer UX explain vs multi-tenant latency SLOs during campaigns.
- Would ZGC change the story? Lower pause ≠ free CPU; allocation still costs. See [cheat-sheets/gc.md](../cheat-sheets/gc.md).

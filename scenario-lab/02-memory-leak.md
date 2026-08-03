# Memory Leak — heap climbs until OOM after canary

## Incident

Tuesday morning. After enabling a new “session affinity debug” filter on `identity-gateway`, heap used grows ~80–120 MiB/hour on canary pods and never returns after full GC. Non-canary pods are flat. Overnight, canary hits `OutOfMemoryError: Java heap space` and restarts. Traffic to canary is only ~5% of region.

## Symptoms

- Old generation occupancy stairs upward across hours
- Full GC frequency increases; after each full GC, retained heap is higher than the previous baseline
- Metaspace also trends up slowly on the same pods
- No obvious unbounded queue length in business metrics
- Restarts “fix” memory until the climb resumes

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25**, G1 |
| Heap | `-Xmx4g` (illustrative) |
| QPS | Canary ~300 RPS (illustrative) |
| Session TTL | 30 minutes (config) |
| Change | `SessionDebugFilter` + in-memory “recent principal” ring intended for troubleshooting |

## Metrics

```
jvm.memory.used.after_full_gc   1.2G → 1.9G → 2.7G → 3.4G (over ~6h)
jvm.gc.overhead                 3% → 18%
metaspace.used                  +40Mi over same window
http.inflight                   stable
cache.estimated_size (Caffeine app cache)  flat
```

Illustrative: retained set growth after full GC is the smoking metric pattern for a leak or unbounded cache — not a short-lived allocation spike.

## Logs

```
2026-08-03T02:14:11.002Z INFO  SessionDebugFilter - captured principal=user_91822 attrs=14
2026-08-03T03:01:44.118Z WARN  gc - Pause Full (G1 Compaction Pause) 812ms heapBefore=3.6G heapAfter=2.9G
2026-08-03T04:22:09.550Z WARN  gc - Pause Full (G1 Compaction Pause) 1044ms heapBefore=3.9G heapAfter=3.3G
2026-08-03T05:48:01.003Z ERROR Tomcat - OutOfMemoryError: Java heap space
2026-08-03T05:48:01.110Z INFO  SessionDebugFilter - captured principal=user_22011 attrs=9
```

Note: filter still logging captures moments before OOM — retention path is live.

## Initial Hypotheses

1. Unbounded in-memory cache / map (debug “recent principals”) without eviction
2. `ThreadLocal` not removed on pooled threads (request attrs)
3. Classloader / redeploy leak (more metaspace-shaped; possible co-traveler)
4. Listener or shutdown hook retaining request graphs
5. Heap dump will show Dominator `byte[]` from HTTP bodies incorrectly buffered

## Questions

- Why is “heap after full GC keeps rising” stronger evidence than “heap used is high under load”?
- What do you ask for first: live histo, heap dump, or allocation profile?
- What assumption do you make if metaspace grows alongside heap?
- What if QPS were 100× on canary — how fast to OOM, and does that change the fix?
- How do you prove the leak is canary-only code vs a latent JVM issue? See [jvm-internals/](../jvm-internals/), [performance-engineering/memory-profiling.md](../performance-engineering/memory-profiling.md).

## Investigation

1. **Confirm leak vs pressure**  
   Chart heap **after** full GC (or `jcmd GC.run` carefully in non-prod). Rising baseline ⇒ retention. Flat after GC ⇒ allocation pressure / sizing. See [principal-engineer/scenarios/memory-growth.md](../principal-engineer/scenarios/memory-growth.md).

2. **Live histo before dump**  
   `jcmd <pid> GC.class_histogram` (or `jmap -histo:live`). Look for growing `ConcurrentHashMap$Node`, `SessionDebug*`, `Principal`, `byte[]`.

3. **Heap dump on canary**  
   `jcmd <pid> GC.heap_dump /tmp/canary.hprof`  
   Analyze in Eclipse MAT / VisualVM: Dominators, “Path to GC Roots” for the largest clusters. Tooling: [performance-engineering/tools/jcmd.md](../performance-engineering/tools/jcmd.md), [performance-engineering/tools/jmap.md](../performance-engineering/tools/jmap.md).

4. **Correlate with the change**  
   Search dump / code for `SessionDebugFilter` static maps. Check whether eviction/TTL was wired.

5. **ThreadLocal check**  
   If histo shows many `ThreadLocalMap$Entry`, inspect filter/MDC usage on Tomcat pools ([concurrency/](../concurrency/)).

6. **Metaspace side-path**  
   If classes accumulate, check dynamic proxies / Groovy / bytecode generation from the debug feature — may be secondary.

7. **Allocation profile (optional)**  
   JFR Allocation Profiling distinguishes “allocates a lot” from “retains a lot.” Leak is retention; both can coexist.

## Root Cause

`SessionDebugFilter` stored every authenticated `Principal` plus request attribute map in a **static `ConcurrentHashMap<String, DebugCapture>`** keyed by session id, intended as a “last 1 000” ring. The ring bound was never applied in 4.2 canary: the map grew without eviction. Sessions that expired in Redis still remained in the JVM map → retained heap climbed until OOM. Metaspace creep came from optional debug serializers generating short-lived proxy classes (secondary).

## Resolution

- Disable filter via flag; restart canaries (clears static map).
- Ship fix: Caffeine/Guava cache with `maximumSize` + TTL aligned to session TTL; never static unbounded maps.
- Remove capturing of full attribute maps; store ids + counters only.

## Prevention

- Code review rule: no static collections without bounds in request path
- Canary alert: `heap_after_gc` slope over 1h
- Soak test with session churn before enabling debug features
- Prefer sampling (1/N) over “capture all” in production diagnostics

## Principal Engineer Discussion

- When is an in-process debug capture acceptable vs shipping to a side channel (Kafka/OLAP)?
- How do you design “break-glass” diagnostics that cannot OOM a pod by construction?
- Heap dump in production: privacy (PII in sessions), disk, STW cost — what’s your policy?
- Compare soft/weak refs vs explicit bounded cache — which failure modes do soft refs hide? (See [garbage-collection/](../garbage-collection/).)

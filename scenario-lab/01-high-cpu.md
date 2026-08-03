# High CPU — checkout API pins one core-set

## Incident

Friday 14:12 UTC. Checkout API (`payments-edge`) on three Kubernetes pods shows host CPU near saturation. Error rate is only mildly elevated; p99 latency climbs from ~80 ms to ~1.4 s. On-call sees “CPU throttling” alerts on the HPA target. A release of `catalog-pricing-lib` 4.2.1 landed 40 minutes earlier. No DB failover, no known traffic spike in the CDN dashboards.

## Symptoms

- Process CPU ~95–100% of the container’s CPU limit for >15 minutes
- Runnable thread count elevated; load average matches core count
- GC pause time **not** dominating wall clock (young GC still <5 ms typical)
- Flame graphs (if taken) show a hot method under a pricing package — but you have not taken one yet
- Downstream payment gateway latency unchanged

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** (LTS), G1, container-aware |
| Heap | `-Xms2g -Xmx2g` (illustrative) |
| CPU limit | 2 cores / pod (illustrative) |
| QPS | ~1.2k RPS steady, ~1.4k at peak (illustrative) |
| Runtime | Spring Boot 3.x on virtual-thread-capable Tomcat; mix of platform worker threads for CPU work |
| Recent change | `catalog-pricing-lib` 4.2.1 — “improved promo matching” |

## Metrics

```
cpu.usage          0.97 of limit (sustained)
jvm.threads.runnable   ~40–60 (was ~8–12)
http.server.requests p50  72ms → 180ms
http.server.requests p99  85ms → 1400ms
jvm.gc.pause.p99       ~4ms (flat)
jvm.memory.used        stable ~1.1Gi
db.pool.active         normal (4–8 / 30)
```

Illustrative: numbers constrain “CPU-bound app work,” not GC thrash or pool wait.

## Logs

```
2026-08-03T14:12:08.221Z WARN  [http-nio-8080-exec-17] PricingEngine - promoMatch elapsedMs=412 path=/v1/checkout skuCount=1
2026-08-03T14:12:09.018Z WARN  [http-nio-8080-exec-03] PricingEngine - promoMatch elapsedMs=388 path=/v1/checkout skuCount=1
2026-08-03T14:12:11.440Z INFO  [async-metrics] cpu.throttle_seconds_total delta=2.1 window=60s
2026-08-03T14:13:02.991Z WARN  [http-nio-8080-exec-22] PricingEngine - promoMatch elapsedMs=901 path=/v1/checkout skuCount=1
```

No `OutOfMemoryError`. No deadlock messages. Occasional slow-promo warnings correlate with CPU alerts.

## Initial Hypotheses

1. Infinite or near-tight loop / busy retry in new pricing code
2. Pathological regex or combinatorial promo matching on certain SKUs
3. Excessive hashing / `equals` on large maps (bad key design)
4. Thread explosion / context-switch storm (many platform threads)
5. GC or allocation thrash misread as “CPU” (possible but metrics lean against it)

Do not mark a winner yet — defend each with what evidence would confirm or kill it.

## Questions

- What would you check first: host/container CPU quota, GC CPU, or application hot methods? Why that order?
- What assumption are you making when you trust “GC pause p99 is flat”?
- How would you distinguish “one hot method” from “many threads each a little hot”?
- What if traffic were 100× — would this fail the same way, or would another bottleneck appear first?
- If virtual threads were used on this path, would CPU saturation improve? Why or why not? (See [virtual-threads/](../virtual-threads/).)

## Investigation

Work in the order a careful engineer would — lead with evidence, not a spoiler.

1. **Confirm the box**  
   `kubectl top pod`, container CPU throttle metrics, cgroup limits. Rule out noisy neighbor vs this JVM.

2. **GC vs application CPU**  
   `jcmd <pid> GC.heap_info`, GC logs / JFR GC events. If pause and allocation rate are calm while CPU is high → application compute, not GC. Cross-check [garbage-collection/](../garbage-collection/) and [performance-engineering/gc-metrics.md](../performance-engineering/gc-metrics.md).

3. **Thread picture**  
   `jcmd <pid> Thread.print` or `jstack <pid>` (see [performance-engineering/tools/jstack.md](../performance-engineering/tools/jstack.md)). Count `RUNNABLE` threads; sample stacks 3× a few seconds apart. Look for the same frames recurring.

4. **Profile, don’t guess**  
   Start a short JFR:  
   `jcmd <pid> JFR.start name=cpu settings=profile duration=60s filename=/tmp/cpu.jfr`  
   Open in JMC / load Method Profiling. Prefer async-profiler CPU mode if installed. Docs: [performance-engineering/tools/java-flight-recorder.md](../performance-engineering/tools/java-flight-recorder.md), [performance-engineering/cpu-profiling.md](../performance-engineering/cpu-profiling.md).

5. **Narrow the hot frame**  
   Expect concentration under `PricingEngine` / promo matching. Inspect the new library diff: regex with nested quantifiers? Cartesian product over promo rules? Logging `toString()` on huge graphs?

6. **Reproduce with a single SKU**  
   From slow-log `sku` / promo ids, hit a staging replica with the same payload under a profiler. Confirm CPU scales with that input, not with QPS alone.

7. **Kill competing hypotheses**  
   - Pool waits high? Metrics say no.  
   - Millions of threads? Thread count elevated but not thousands.  
   - JIT deopt storm? Check JFR Compilation / Deoptimization events if hot method keeps changing.

Related shorter card: [principal-engineer/scenarios/cpu-saturation.md](../principal-engineer/scenarios/cpu-saturation.md).

## Root Cause

`catalog-pricing-lib` 4.2.1 replaced a finite-state promo matcher with a **catastrophic-backtracking regex** (nested `*`-quantifiers) applied to a free-text “campaign tag” field. Certain legitimate tags (long runs of similar characters) make `Matcher` explore an exponential state space on the request thread. CPU burns on few cores; latency follows; GC stays quiet because little is allocated relative to CPU time.

## Resolution

- **Immediate:** roll back library to 4.2.0 or feature-flag regex path; blocklist offending campaign tags at the edge.
- **Hotfix:** replace regex with an explicit parser / Aho–Corasick / precompiled DFA with hard length limits; fail closed on tags above N chars.
- **Validate:** load test the previously pathological tag; CPU should stay proportional, not explode.

## Prevention

- Input-length and complexity guards on any regex over user/merchant text
- CPU SLO burn alerts + per-endpoint `elapsedMs` histograms (already partially present)
- Library release checklist: JMH microbench on adversarial strings ([performance-engineering/tools/jmh.md](../performance-engineering/tools/jmh.md))
- Canary that compares CPU per RPS before full rollout

## Principal Engineer Discussion

- Is “regex in the request path” ever acceptable for merchant-controlled strings? What review bar?
- Should pricing run on a **bounded CPU pool** with timeouts so one SKU cannot starve checkout? Trade-off: complexity vs blast radius.
- Virtual threads do **not** add cores — moving this work to VT would still saturate the same 2-core limit. When *would* VT help a “high CPU” ticket? (Usually: it wouldn’t; see [principal-engineer/scenarios/virtual-threads-no-gain.md](../principal-engineer/scenarios/virtual-threads-no-gain.md).)
- How do you design promo matching so worst-case CPU is O(n) in tag length with a published constant?

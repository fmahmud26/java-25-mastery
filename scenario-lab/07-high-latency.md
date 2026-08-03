# High Latency — p99 cliff after dependency timeout tweak

## Incident

`storefront-bff` p99 moves from ~50 ms to ~2 s starting 11:05. Average is only mildly worse. A platform ticket earlier raised the HTTP client connect/read timeout to **2 s** “to reduce false timeouts” against `inventory-service`, which had been flapping. Error rate is lower; users still complain the site feels stuck.

## Symptoms

- Latency cliff at p99/p999; p50 nearly unchanged
- Timeouts to inventory drop; success responses sometimes slow
- Thread pools / connection pools show elevated wait
- Dependency `inventory` p99 itself ~1.5–1.8 s during the window
- No GC leak pattern; CPU moderate

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| BFF timeout | connect 200 ms → 2 s; read 200 ms → 2 s (illustrative change) |
| BFF pool | limited HTTP client / servlet threads |
| QPS | ~1.5k RPS (illustrative) |

## Metrics

```
http.server.p50          35ms → 45ms
http.server.p99          50ms → 2100ms
inventory.client.p99     180ms → 1900ms
inventory.client.timeout_rate  4% → 0.2%
bff.worker.queue         grows during inventory blips
```

Illustrative: longer timeouts convert failures into **queued waits**, inflating tail latency.

## Logs

```
2026-08-03T11:04:58.001Z WARN  InventoryClient - timeout after 200ms order=...
2026-08-03T11:05:10.220Z INFO  config - InventoryClient timeouts updated read=2000ms
2026-08-03T11:06:01.440Z WARN  InventoryClient - slow success status=200 elapsed=1877ms
2026-08-03T11:06:02.110Z WARN  BffWorker - queueDelayMs=920 path=/api/pdp
```

## Initial Hypotheses

1. Timeout increase causes threads to block longer on a sick dependency (latency inheritance)
2. Inventory genuinely regressed (code/GC/DB)
3. Client connection pool exhaustion amplifying waits
4. Retry storms multiplying load
5. GC pauses on BFF (check; likely secondary)

## Questions

- Why did **reducing** timeouts (error rate) make the product feel worse?
- What would you check first: BFF traces, inventory SLOs, or BFF GC?
- What assumption does “success with 200” make about user experience?
- What if traffic ×100 during inventory slowness — fail-fast vs wait?
- How do bulkheads and deadlines relate? ([system-design/](../system-design/), [principal-engineer/scenarios/latency-cliff.md](../principal-engineer/scenarios/latency-cliff.md))

## Investigation

1. **Distributed traces**  
   Break p99 into queue delay, inventory client time, local work. Expect inventory + queue.

2. **Dependency health**  
   Inventory GC, DB, CPU — confirm sick service vs BFF-only.

3. **Client pool / threads**  
   `jcmd Thread.print`: many threads in `socketRead` up to 2 s. Pool wait metrics.

4. **Retry policy**  
   Ensure retries aren’t stacking (3 × 2 s).

5. **Compare configs**  
   Diff timeout change vs latency cliff timestamp — causal candidate.

6. **JFR on BFF**  
   Rule out local CPU/GC as primary ([performance-engineering/](../performance-engineering/)).

7. **Load experiment**  
   In staging, inject 1.5 s inventory delay with 200 ms vs 2 s timeouts; observe BFF p99 and error rate trade-off.

## Root Cause

Inventory was slow. Short timeouts failed fast (errors, but workers freed). Raising timeouts to 2 s made most calls **eventually succeed** after ~2 s, holding BFF workers and connections, creating queue delay for everyone else → **tail latency cliff**. Lower timeout errors looked “healthier” on success-rate dashboards while UX collapsed.

## Resolution

- Restore aggressive deadlines for interactive paths; use longer timeouts only on async paths.
- Bulkhead inventory calls; shed load; fix inventory root (separate incident).
- Return degraded PDP (cached stock) rather than blocking 2 s.
- Align product SLO: prefer fast failure + retry UX over multi-second blank spinners.

## Prevention

- Alert on p99, not only error rate
- Chaos: dependency delay injection in CI/staging
- Explicit budget: `timeout ≤ user-facing SLO × fraction`
- Separate “batch” and “interactive” clients with different deadlines

## Principal Engineer Discussion

- Dashboard malpractice: optimizing success rate while burning latency SLO.
- Default timeouts: how do you set them from first principles?
- Hedged requests, caches, stale-while-revalidate — when worth complexity?
- Ownership: who gets paged — BFF or inventory — when latency inherits?

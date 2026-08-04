# Latency

Time to complete one operation — **which summary statistic matters** must be explicit.

## Mental Model

```text
mean hides pain
histogram / percentiles tell the truth
request budget = queueing + compute + GC + locks + downstream
```

| Metric | Use |
|--------|-----|
| p50 | Typical experience |
| p95/p99 | SLO territory |
| max | Spikes / GC / safepoints — noisy but useful in incidents |
| average only | Insufficient for SLAs |

## How to Measure

- Load tool latency histograms (state open- vs closed-loop)  
- In-process timers + JFR for correlation  
- JMH `Mode.SampleTime` / `AverageTime` (**micro only** — not service SLO proof)  
- Traces for multi-hop budgets  

Coordinate clocks: load-generator latency includes network; in-process timers do not.

## Production Scenario — “p50 is fine”

Checkout p50 40ms, p99 1.2s. Marketing sees “average looks good.” Support sees timeouts. Overlay GC + dependency spans: PSP p99 dominates.

**Fix:** timeout budget + bulkhead + cache fraud result; stop arguing about means.

## Hypotheses

- Slow dependency  
- GC / safepoint pause  
- Lock wait / pool wait  
- Warmup / deopt  
- Oversized payloads / alloc storms  
- Queueing (Little’s Law) under overload  

## Experiment sketch

Fixed RPS (not max ramp) for latency SLOs — open-loop vs closed-loop load differs; document which. Change one variable; compare percentile histograms.

## When Not to Optimize Latency

Already inside SLO with headroom; batch/admin paths; network RTT dominates and cannot be cached.

## Claim template

“At 2k RPS steady (closed-loop, 2KB payloads), in-process p99 handler time fell from 38ms to 22ms after query fix; client p99 41→27ms; GC p99 pause unchanged (~8ms).”

## Principal Perspective

Name the **percentile and budget** before proposing code changes. Architecture notes: [low-latency-architecture.md](./low-latency-architecture.md).

### Related

[tail-latency.md](./tail-latency.md) · [throughput.md](./throughput.md) · [gc-pressure.md](./gc-pressure.md) · [jvm-observability.md](./jvm-observability.md)

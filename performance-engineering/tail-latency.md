# Tail Latency

High-percentile latency (p99, p99.9, sometimes max) — usually what pages on-call.

## Mental Model

```text
Most requests fine (p50)
Rare requests terrible (p99+)  ← users remember these
Causes often: GC, safepoint, lock convoy, slow outlier downstream, cold code
```

## Why tails dominate SLOs

At 1k RPS, p99 means ~10 slow requests/s. “Average 5ms” with “p99 2s” fails the product. Fan-out: one slow dependency can dominate the parent span.

## How to Measure

- Latency histogram with enough samples (short tests lie about p99.9)  
- Correlate spike timestamps with GC/safepoint/JFR MonitorWait  
- Trace outliers (request IDs) when possible  
- Capture a 60s JFR window covering the spike ([jvm-observability](./jvm-observability.md))  

## Common mechanisms

| Mechanism | Evidence |
|-----------|----------|
| GC STW | GC log pause ≈ spike |
| Time-to-safepoint | Safepoint “sync” long |
| Contention | JFR monitor events; BLOCKED stacks |
| Downstream tail | Client spans; dependency p99 |
| Allocation storm | Young GC storm + CPU |
| Huge requests | Size-correlated latency |
| Cold / deopt | JIT activity after deploy or traffic shift |

## Production Scenario — synchronized spike train

Every ~30s p99 jumps. GC log quiet. JFR: monitor wait on a global metrics lock flushed on a timer.

**Fix:** remove lock from hot path; async metrics; p99 smooths.

## Experiment sketch

Inject background GC or lock; watch p99 move; remove; confirm tails return. Never optimize mean alone for a p99 SLO.

## When Not to Chase p99.9

Insufficient traffic to estimate; cost exceeds business value; max is dominated by rare multi-second admin tasks on the same timer.

## Claim template

“p99 (load tool, 15min@3k RPS) 900ms→120ms after eliminating Full GC (heap 2→4GiB + cache bound); p50 unchanged ~25ms.”

## Principal Perspective

Tail latency is an **architecture** problem (see [low-latency-architecture.md](./low-latency-architecture.md)) as much as a code problem. Budget and evidence first.

### Related

[latency.md](./latency.md) · [incidents.md](./incidents.md) · [gc-pressure.md](./gc-pressure.md) · [../scenario-lab/07-high-latency.md](../scenario-lab/07-high-latency.md)

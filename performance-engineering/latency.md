# Latency

Time to complete one operation — **which summary statistic matters** must be explicit.

## Mental Model

```text
mean hides pain
histogram / percentiles tell the truth
```

| Metric | Use |
|--------|-----|
| p50 | Typical experience |
| p95/p99 | SLO territory |
| max | Spikes / GC / safepoints — noisy but useful in incidents |
| average only | Insufficient for SLAs |

## How to Measure

- Load tool latency histograms  
- JFR + application timers  
- JMH `Mode.SampleTime` / `AverageTime` (micro only)

Coordinate clocks: load-generator latency includes network; in-process timers don’t.

## Hypotheses

- Slow dependency  
- GC / safepoint pause  
- Lock wait  
- Warmup / deopt  
- Oversized payloads / alloc storms  

## Experiment sketch

Fixed RPS (not max ramp) for latency SLOs — open-loop vs closed-loop load differs; document which.

## Claim template

“At 2k RPS steady (closed-loop, 2KB payloads), in-process p99 handler time fell from 38ms to 22ms after query fix; client p99 41→27ms; GC p99 pause unchanged (~8ms).”

### Related

[tail-latency.md](./tail-latency.md) · [throughput.md](./throughput.md) · [gc-pressure.md](./gc-pressure.md)

# Experiment 03 — Allocation Churn

## Question

How does per-iteration `byte[]` allocation affect GC frequency and throughput in a tight loop service?

## Workload

Variant A: allocate `new byte[16_384]` each request.  
Variant B: reuse a thread-local/buffer of same size.  
Fixed RPS or JMH throughput mode — pick one and stick to it.

## Measure

- JFR alloc profile 60s each variant  
- GC log young GC count  
- Throughput or p99 (whichever you chose)

## Hypothesize

A has higher alloc rate and more young GCs; B reduces GC pressure. Effect on p99 depends on pause sizes.

## Analyze

Do not claim “reuse is always faster” — state measured delta for **this** buffer size and rate.

## Re-measure

Confirm B doesn’t break correctness (data bleed between requests).

### Related

[../allocation-profiling.md](../allocation-profiling.md) · [../gc-pressure.md](../gc-pressure.md)

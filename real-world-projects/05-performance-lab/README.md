# 05 — Concurrency Performance Lab

**Tier:** Measurement · **Demonstrates:** performance, virtual threads vs platform, honest benchmarking

## Problem

Engineers claim “virtual threads make everything faster.” Build a **lab** that measures throughput/latency for the same workload under platform threads, virtual threads, and CompletableFuture pipelines — and teaches when VT does **not** help.

## Requirements

**Functional**
- Workloads: blocking sleep (I/O-ish), pure CPU burn, mixed  
- Modes: platform pool, VT per task, CF on pool  
- Report: throughput, p50/p95 latency, threads snapshot  

**Non-functional**
- Warmup + measured iterations  
- Document microbench limits (prefer JMH for publishable numbers)  
- Reproducible CLI flags  

## Architecture

```text
PerformanceLab
  → WorkloadBench (run mode × workload)
  → RuntimeSnapshot (thread counts, memory)
  → BenchResult (record)
```

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| Manual harness first | Pedagogy | Only JMH — steeper for first lab |
| VT vs fixed pool | Show carrier behavior | Single mode demo |
| Optional JMH note | Production truth | Claiming nanos accuracy from `System.nanoTime` loops alone |

## Design decisions

- Separate **I/O-bound** vs **CPU-bound** expectations.  
- Pinning demo extension: `synchronized` + blocking inside VT (document).  
- Never present results without warmup disclaimer.

## Implementation plan

1. Workload interfaces  
2. Executors for each mode  
3. Latency reservoir (hdr-ish simple buckets OK)  
4. CLI + printed table  
5. README section interpreting sample results  

## Failure scenarios

| Misread | Correction |
|---------|------------|
| CPU workload “VT slower/same” | Expected — VT not more cores |
| Unbounded VT + DB | Pool exhaustion — link to PE scenarios |
| No warmup | JIT skew |

## Testing strategy

- Sanity: blocking workload VT throughput ≫ small platform pool  
- CPU: VT ≈ platform within noise  
- Snapshot: platform mode thread count rises with pool; VT carriers bounded  

## Performance considerations

- Microbench noise: CPU frequency, GC, turbo  
- For publishable: JMH `-f` forks, `-wi` warmup  
- Allocation rate can dominate — watch GC  

## Scaling strategy

- Lab informs capacity models for services 03/06/07  
- Next: JFR recordings compared side-by-side  

## Interview discussion

“I measure, I don’t slogan. Virtual threads shine on blocking concurrency; they don’t create CPU. Here’s a lab that shows both.”

**Follow-ups:** What is pinning? How do you size DB pools under VT?

## Run

```bash
chmod +x run.sh && ./run.sh
```

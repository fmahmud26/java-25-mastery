# ADR-001 — Virtual Threads Adoption (Blocking I/O Paths)

- **Status:** Accepted for portfolio services with blocking I/O  
- **Date:** 2026-08-03  
- **JDK:** 25.0.3  

## Context

Request handlers block on HTTP/DB-style waits. Platform pools either exhaust under concurrency or are oversized. Temptation: “turn on VT everywhere.”

## Decision

Adopt `Executors.newVirtualThreadPerTaskExecutor()` for **blocking I/O** request/fan-out paths. Keep **CPU-bound** stages on a sized platform pool. Size Hikari/HTTP pools from dependency capacity, not VT count. Do **not** enable Structured Concurrency in production while preview on Java 25.

## Evidence (measured here)

| Lab | Result (this machine) |
|-----|------------------------|
| [virtual-vs-platform-blocking](../../experiments/virtual-vs-platform-blocking/) | 2000×10ms sleep: platform-16 **1266 ms** vs VT **35 ms** |
| [platform-thread-footprint](../../experiments/platform-thread-footprint/) | 2000 starts: platform startWallMs=96 vs VT=48 |
| [nanotime-measurement-pitfalls](../../experiments/nanotime-measurement-pitfalls/) | Single-shot nanoTime variance — do not claim % without JMH/prod metrics |

## Alternatives rejected

| Option | Why rejected |
|--------|----------------|
| Raise platform pool to thousands | RSS + context switch; still ≠ infinite DB |
| Reactive rewrite | Cost; unnecessary if blocking + VT fits |
| Pool of N virtual threads | Wrong model |
| Fleet VT without pool caps | Known pool-wait cliff |

## Consequences

- Must add admission control / semaphores matching DB pool.  
- Educate: JEP 491 — don’t lead with synchronized-pin lore on 25; watch residual native/file pinning via JFR.  
- Rollback: feature-flag executor choice.

## Success / abort metrics

- Success: p99 stable or better; pool wait within budget; no error-budget burn.  
- Abort: CPU pegged on VT paths with flat I/O wait; pool wait ↑; residual pin storms unexplained.

Related: [../scenarios/virtual-threads-no-gain.md](../scenarios/virtual-threads-no-gain.md) · [../refusals.md](../refusals.md) §2

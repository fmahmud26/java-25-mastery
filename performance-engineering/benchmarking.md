# Benchmarking

Controlled comparison of implementations or configs under a **named workload**.

## Micro vs macro

| | Micro (JMH) | Macro (load test) |
|--|-------------|-------------------|
| Scope | Method/algorithm | Service + deps |
| Strength | JIT-aware, low noise if done right | Real queues, GC, I/O |
| Weakness | Easy to overfit / miss system effects | Noisy; needs steady env |

Use **both** when the question spans layers.

## Rules

| Do | Don’t |
|----|-------|
| Warm up; fork; consume results | One `nanoTime` loop in `main` |
| State workload & JDK flags | Compare across unclean machines as gospel |
| Report distribution | Publish only “avg ns” |
| Fix GC/heap when comparing algorithms | Accidentally change `-Xmx` mid-study |

## JMH

See [tools/jmh.md](./tools/jmh.md) and [experiments/01-jmh-basics.md](./experiments/01-jmh-basics.md).

## Macro

k6 / Gatling / JMeter — fixed RPS for latency; ramp for capacity curves. Pair with JFR during the plateau.

## Claiming results

Always: workload, env, metric, tool, warm/cold, run count. Never “library A is faster” as a universal statement.

### Related

[scientific-method.md](./scientific-method.md) · [throughput.md](./throughput.md) · [latency.md](./latency.md)

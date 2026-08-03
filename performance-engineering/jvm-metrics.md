# JVM Metrics

Export and dashboard the minimum set that supports the scientific loop.

## Suggested series

| Metric | Why |
|--------|-----|
| Heap used / after-GC estimate | Retention vs churn |
| GC pause time / count | Tails |
| CPU user/system | Capacity |
| Alloc rate (derived) | GC pressure |
| Threads | Pools / leaks |
| App p50/p99 / RPS / errors | User truth |

## Rule

Optimize against **app SLO metrics**; use JVM metrics to explain them — not the other way around.

### Related

[metrics-vocabulary.md](./metrics-vocabulary.md) · [scientific-method.md](./scientific-method.md)

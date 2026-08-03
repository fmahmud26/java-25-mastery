# Heap Sizing (Performance Lens)

Size for **live set + allocation headroom + collector needs**, then measure p99 and GC under peak workload.

| Practice | Note |
|----------|------|
| `-Xms` ≈ `-Xmx` | Avoid resize noise during tests |
| Don’t shrink heap to “force GC” | Distorts latency |
| Container limit > heap | Native/metaspace/stacks |

Change heap as a **single** experiment; report before/after pause + app latency.

### Related

[gc-pressure.md](./gc-pressure.md) · [experiments/05-tail-latency-gc.md](./experiments/05-tail-latency-gc.md)

**Canonical depth:** [../garbage-collection/heap-sizing.md](../garbage-collection/heap-sizing.md)

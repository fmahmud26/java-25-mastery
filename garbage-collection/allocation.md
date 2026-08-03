# Allocation (and GC)

How allocation interacts with GC frequency and pauses.

## Mental Model

```text
new → (usually) TLAB in Eden → Eden full → young GC
High allocation rate ⇒ more frequent GC (even if objects die instantly)
```

## Technical Mechanism

| Path | Notes |
|------|-------|
| TLAB bump-pointer | Fast thread-local allocation |
| Eden shared slow path | After TLAB refill |
| Large / humongous | May skip normal Eden path (e.g. G1) |
| Escape analysis | Some `new` never hit the heap (JIT) — don’t rely on it |

See also memory-management object-allocation notes; here the GC angle is **rate → collection frequency**.

## Production Implications

- Short-lived garbage is cheap *per object* but a **storm** still burns GC CPU and young pauses.  
- Measure allocation rate with JFR / GC logs ([allocation-rate.md](./allocation-rate.md)).  
- Fix pointless churn (e.g. per-request huge buffers) before exotic GC flags.

## Trade-offs

Faster allocators don’t remove GC work — they move pressure to the collector. Throughput collectors tolerate more STW; latency collectors spend concurrent CPU/barriers instead.

## Interview / PE

Why can GC be busy when the live set is small? TLAB purpose?

### Related

[young-generation.md](./young-generation.md) · [allocation-rate.md](./allocation-rate.md) · [minor-gc.md](./minor-gc.md)

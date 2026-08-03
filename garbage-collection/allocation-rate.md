# Allocation Rate

Bytes (or objects) allocated per unit time — primary driver of **GC frequency**.

## Mental Model

```text
High allocation rate + short lifetimes = frequent young GC (often OK if pauses small)
High allocation rate + promotion / live growth = old pressure / Full GC risk
```

## How to Measure

| Tool | What you get |
|------|----------------|
| GC logs | Young GC interval ≈ Eden fill time under steady alloc |
| JFR | Allocation samples, TLAB events, object allocation profiling |
| async-profiler alloc | Flame graph of allocation sites |
| Metrics | Derived from Eden occupancy / GC counters |

```bash
java -Xlog:gc*:file=gc.log:uptime,level,tags ...
# JFR profile under load — see diagnostics.md
```

## Production Implications

- Optimizing allocation sites (buffers, boxing, JSON trees) often beats exotic GC flags.  
- “Frequent GC” with **tiny pauses** and stable live set may be healthy.  
- Frequent GC with **rising old occupancy** is not.

## Incident Link

[incidents.md](./incidents.md) — high allocation rate / frequent GC.

## Interview / PE

How infer allocation rate from young GC cadence? When is high alloc OK?

### Related

[allocation.md](./allocation.md) · [minor-gc.md](./minor-gc.md) · [diagnostics.md](./diagnostics.md)

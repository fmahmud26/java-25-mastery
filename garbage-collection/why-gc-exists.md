# Why GC Exists

Automatic reclamation of unreachable objects — no manual `free()` in ordinary Java.

## Mental Model

```text
allocate → use → unreachable → GC reclaims → memory reused
```

## Benefits

| Benefit | Detail |
|---------|--------|
| Safety | Avoids most dangling-pointer / double-free classes of bugs |
| Productivity | Domain logic over manual arenas (mostly) |
| Pluggable policy | Throughput vs latency collectors |

## Costs

- CPU for GC work (STW and/or concurrent)  
- Possible application latency from pauses or barriers  
- Tuning and observability burden under load  
- Footprint: heaps need headroom for collector strategies  

## What GC Does *Not* Do

GC does **not** free objects that are still strongly reachable. Caches, listeners, and `ThreadLocal` retention are application problems — see memory-management materials.

## Production Implications

Treat GC as a **runtime subsystem** with metrics (pause, frequency, allocation rate, occupancy). “Enable GC” is not a feature flag — it is always on; you choose *which* collector and *how large* the heap is.

### Related

[gc-fundamentals.md](./gc-fundamentals.md) · [trade-offs.md](./trade-offs.md)

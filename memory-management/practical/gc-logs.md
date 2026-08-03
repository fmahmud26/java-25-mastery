# Practical: GC Logs

GC logs show pause times, heap before/after, and collector behavior.

**Canonical depth:** [garbage-collection/gc-logs.md](../../garbage-collection/gc-logs.md) · PE decision: [principal-engineer/scenarios/gc-pause-growth.md](../../principal-engineer/scenarios/gc-pause-growth.md)

## Enable (Unified Logging)

```bash
java -Xlog:gc*:file=gc.log:time,uptime,level,tags \
     -Xlog:gc+heap=debug \
     -jar app.jar
```

## What to watch

| Signal | Meaning | First move |
|--------|---------|------------|
| Rising old-gen after Full/mixed GCs | Retention or undersized heap | After-GC chart → dump if climbing |
| Long pauses | Latency risk | Measure alloc rate + live set **before** collector shopping |
| Frequent young GC, small live set | High **allocation rate** | Cut churn; don’t jump to ZGC first |
| Allocation failure → Full GC | Pressure / fragmentation | Prove with logs/JFR; then tune or bakeoff |

## Workflow

1. Confirm GC algorithm (`jcmd <pid> GC.heap_info`, flags)  
2. Plot heap after GC over time — should sawtooth, not climb forever  
3. Correlate with traffic (allocation rate)  
4. Fix leak/churn; only then consider G1 tune or ZGC/Shenandoah bakeoff  
5. If heap climbs despite GC → [memory-leak](./memory-leak.md)

Related: [../heap.md](../heap.md) · [../../cheat-sheets/gc.md](../../cheat-sheets/gc.md)

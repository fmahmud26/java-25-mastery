# GC Pressure

How much allocation and collection activity stresses the JVM — distinct from “GC is broken.”

## Mental Model

```text
allocation rate ↑  →  young GC frequency ↑
live set ↑         →  old occupancy / mixed/full work ↑
pauses / concurrent CPU  →  latency & throughput impact
```

## Metrics to watch

| Signal | Meaning |
|--------|---------|
| Allocation rate | Bytes/objects per second |
| GC frequency & pause distribution | Tail risk |
| After-GC occupancy | Retention / sizing |
| GC CPU % | Cycles stolen from mutators |
| Promotion / humongous | Old-gen / fragmentation stress |

## How to Measure

```bash
-Xlog:gc*:file=gc.log:uptime,level,tags
jcmd <pid> JFR.start settings=profile ...
jcmd <pid> GC.heap_info
```

JFR GC + allocation events; compare with app p99 timeline.

## Hypothesize carefully

| Observation | Hypothesis direction |
|-------------|----------------------|
| Frequent tiny young GC, stable live set | High churn — maybe OK if pauses tiny |
| Rising after-GC heap | Leak / unbounded cache |
| Rare multi-second pause | Full GC / TTSP / large evacuate |

## Optimize (after proof)

Reduce alloc; bound retention; right-size heap; only then consider collector change — with bakeoff data.

## Claim template

“Under W, allocation rate 1.8→0.7 GB/s after buffer reuse; young GC/min 120→40; p99 60→35ms; live set unchanged.”

### Related

[allocation-profiling.md](./allocation-profiling.md) · [gc-metrics.md](./gc-metrics.md) · [tail-latency.md](./tail-latency.md)

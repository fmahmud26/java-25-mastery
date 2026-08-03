# GC Metrics (Lab View)

Operational signals for [gc-pressure.md](./gc-pressure.md).

## Collect

```bash
-Xlog:gc*:file=gc.log:uptime,level,tags
jstat -gcutil <pid> 1000
jcmd <pid> GC.heap_info
```

JFR GC events for phase-level timing.

## Read

| Signal | Read as |
|--------|---------|
| Pause p99 | Tail contributor? |
| Frequency | Alloc rate / sizing |
| Before→after heap | Reclaim effectiveness |
| Full GC count | Emergency — investigate |

Never tune GC flags without a baseline log + app latency correlation.

### Related

[gc-pressure.md](./gc-pressure.md) · [jvm-metrics.md](./jvm-metrics.md)

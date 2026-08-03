# GC Logs

Unified logging is the production baseline for GC investigation on modern JDKs.

## Mental Model

```text
Every collection leaves a breadcrumb: when, which, how long, heap before/after
```

## Enable (examples)

```bash
java -Xlog:gc*:file=/var/log/app/gc.log:uptime,level,tags:filecount=10,filesize=100M ...

# useful extras during incidents
java -Xlog:gc*,safepoint=info,gc+heap=debug:file=gc.log:uptime,level,tags ...
```

Exact subtags vary by collector; start with `gc*` and narrow.

## What to Read

| Field / idea | Why |
|--------------|-----|
| Pause duration | Latency correlation |
| Collection type | Young / mixed / full / concurrent phases |
| Heap before → after | Reclaim effectiveness |
| Cause / trigger | Allocation failure, System.gc, etc. |
| Humongous / evacuation failure | G1 stress signals |
| Cadence | Allocation rate proxy |

## Pair With

- Application latency charts (same timeline)  
- JFR for phase detail  
- Heap dump if after-GC occupancy climbs ([diagnostics.md](./diagnostics.md))

## Anti-Patterns

- Debug GC logging forever in prod without rotation  
- Tuning from one scary line without rates/trends  
- Ignoring safepoint logs when GC pauses look small

## Interview / PE

Which flags do you enable first? How correlate a latency spike to a log line?

### Related

[pause-time.md](./pause-time.md) · [diagnostics.md](./diagnostics.md) · [incidents.md](./incidents.md)

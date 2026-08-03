# Heap Dumps

Point-in-time snapshot of objects — for **retention / leak / live-set** analysis, not CPU.

## Measure

```bash
jcmd <pid> GC.heap_dump /tmp/heap.hprof
# -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/dumps
```

Prefer dumping when after-GC occupancy is high but instance still alive.

## Analyze

Eclipse MAT / VisualVM / YourKit:

1. Dominator tree  
2. Path to GC roots  
3. Histogram (`byte[]`, map nodes, sessions)  

## Hypothesize

Unbounded cache, listener leak, ThreadLocal — prove with path-to-root.

## Experiment

Fix retention → run same load → dump again → retained size down; confirm latency/GC pressure.

## Safety

Contains secrets/PII — access control, encryption at rest, retention policy.

### Related

[memory-profiling.md](./memory-profiling.md) · [jcmd.md](./jcmd.md) · [gc-pressure.md](./gc-pressure.md)

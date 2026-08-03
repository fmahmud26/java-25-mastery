# Practical: Heap Dump

Snapshot of live objects — primary tool for retention / leak analysis.

## Capture

```bash
jcmd <pid> GC.heap_dump /tmp/heap.hprof
# jmap -dump:format=b,file=/tmp/heap.hprof <pid>

java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp ...
```

Tips: dump under load when baseline is high; ensure disk space; treat dumps as **sensitive** (secrets, PII).

## Analysis Workflow (MAT-style)

1. Open `.hprof` in **Eclipse MAT** / VisualVM / YourKit  
2. **Leak Suspects** report — starting hypotheses  
3. **Dominator Tree** — who retains the most bytes?  
4. **Path to GC Roots** — why isn’t it collected?  
5. **Histogram** — top classes (`byte[]`, `int[]`, map nodes)  
6. Optional: compare two dumps (growth classes)

## What to Look For

| Finding | Suspect |
|---------|---------|
| One fat branch | Unbounded cache / static collection |
| `Thread` → `ThreadLocalMap` | ThreadLocal leak |
| Listener `ArrayList` growth | Listener leak |
| Duplicate huge `byte[]` | Large alloc / buffering |
| Retained `ClassLoader` | Redeploy / metaspace leak companion |
| Session maps | User session retention |

## Tie to Investigation Loop

Symptoms → Metrics → **Dump** → Analysis → RC → Fix → Prevention — [../investigation.md](../investigation.md)

### Related

[memory-leak.md](./memory-leak.md) · [../incidents.md](../incidents.md) · [../outofmemoryerror.md](../outofmemoryerror.md)

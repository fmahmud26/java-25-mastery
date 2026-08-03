# Practical Diagnostics

Production JVM memory & runtime triage on **Java 25**.

| Scenario | Guide |
|----------|--------|
| Investigation method | [../investigation.md](../investigation.md) |
| Incidents | [../incidents.md](../incidents.md) |
| Heap Dump | [heap-dump.md](./heap-dump.md) |
| Memory Leak | [memory-leak.md](./memory-leak.md) |
| GC Logs | [gc-logs.md](./gc-logs.md) |
| Thread Dump | [thread-dump.md](./thread-dump.md) |
| High CPU | [high-cpu.md](./high-cpu.md) |
| Deadlock | [deadlock.md](./deadlock.md) |

## Toolkit

| Tool | Use |
|------|-----|
| `jcmd`, `jstat` | Live heap / GC / metaspace |
| Eclipse MAT / VisualVM | Heap dump UI |
| JFR / JMC | Allocation, GC, latency timeline |
| `async-profiler` | CPU / alloc flame graphs |
| NMT (`-XX:NativeMemoryTracking=summary`) | Native footprint |

```bash
jcmd <pid> GC.heap_info
jcmd <pid> GC.heap_dump /tmp/heap.hprof
jcmd <pid> VM.metaspace
jcmd <pid> Thread.print
```

### Related

[../README.md](../README.md) · [../principal-engineer.md](../principal-engineer.md)

# jcmd

Unified JDK diagnostic command interface — prefer over remembering many legacy tools.

## Measure (common)

```bash
jcmd <pid> help
jcmd <pid> VM.version
jcmd <pid> Thread.print
jcmd <pid> GC.heap_info
jcmd <pid> GC.class_histogram
jcmd <pid> GC.heap_dump /tmp/heap.hprof
jcmd <pid> JFR.start name=x settings=profile duration=60s filename=/tmp/x.jfr
jcmd <pid> VM.native_memory summary   # if -XX:NativeMemoryTracking=summary
jcmd <pid> VM.flags
```

## When to use

| Need | Command family |
|------|----------------|
| Threads | `Thread.print` |
| Heap live set clue | `GC.heap_info`, histogram |
| Dump | `GC.heap_dump` |
| Profile | `JFR.*` |

## Safety

Heap dumps are heavy and sensitive; take on a peer instance when possible. Don’t run experimental disruptive commands in prod without runbook.

### Related

[tools/jcmd.md](./tools/jcmd.md) · [jstack.md](./jstack.md) · [heap-dumps.md](./heap-dumps.md)

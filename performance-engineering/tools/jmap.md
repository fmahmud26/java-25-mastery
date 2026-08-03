# Tool: jmap

Legacy-ish heap histo/dump. Prefer:

```bash
jcmd <pid> GC.heap_dump /tmp/heap.hprof
jcmd <pid> GC.class_histogram
```

`jmap -dump:...` still appears in older runbooks.

Related: [../heap-dumps.md](../heap-dumps.md)

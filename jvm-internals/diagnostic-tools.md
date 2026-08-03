# Diagnostic Tools (JVM Internals)

Principal-level triage kit. Prefer **Unified Logging** (`-Xlog`) + **JFR** + **jcmd** on Java 25.

## Mental Model

```text
Symptom → pick layer
  CPU     → profiler / thread dump
  Memory  → heap histogram / NMT / metaspace logs
  Latency → JFR + GC/safepoint logs
  Classes → class+load / loader leaks
  JIT     → compilation / deopt logs
```

## Core Tools

| Tool | Use |
|------|-----|
| `jcmd` | Thread.print, GC.heap_info, VM.native_memory, JFR, compiler |
| `jstack` / `jcmd Thread.print` | Stack traces, deadlocks |
| `jmap` / `jcmd GC.heap_dump` | Heap dumps |
| `jstat` | GC / survivor / metaspace samples |
| **JFR** | Low-overhead timeline: GC, allocation, safepoints, I/O |
| **async-profiler** | CPU / alloc / wall-clock flame graphs |
| `-Xlog:…` | GC, safepoint, class load, JIT |
| `javap` | Bytecode / constant pool |
| `jinfo` | Flags (careful live changes) |

## High-Value Commands

```bash
jcmd <pid> Thread.print
jcmd <pid> GC.heap_info
jcmd <pid> GC.class_histogram
jcmd <pid> VM.native_memory summary   # enable -XX:NativeMemoryTracking=summary
jcmd <pid> VM.metaspace
jcmd <pid> Compiler.CodeHeap_Analytics

java -Xlog:gc*,safepoint,class+load=info:file=jvm.log:uptime,level,tags

# JFR
java -XX:StartFlightRecording=duration=60s,filename=app.jfr …
jcmd <pid> JFR.start name=prod settings=profile
jcmd <pid> JFR.dump name=prod filename=/tmp/prod.jfr
```

## Mapping Symptoms → Tools

| Symptom | Start here |
|---------|------------|
| High CPU | async-profiler CPU; `Thread.print`; check GC/Compiler threads |
| Memory pressure | heap dump / histogram; NMT; metaspace; direct buffers |
| Latency spike | JFR; `-Xlog:safepoint,gc`; TTSP |
| Class-loading | `-Xlog:class+load`; histogram of loaders; Metaspace |
| JIT weirdness | PrintCompilation / jit logs; code cache; deopt tracing |
| Large alloc | JFR Allocation / TLAB events; humongous (G1) logs |

## Production Hygiene

- Always-on: GC + safepoint logs to files with rotation.  
- On-demand: JFR profile for 30–120s under incident.  
- Heap dumps are PII-sensitive — control access.  
- Don’t enable ultra-verbose JIT traces permanently.

## Interview / PE

Given “p99 spike,” list first three commands. Heap OOM vs metaspace OOM tooling difference? What is NMT for?

### Related

[incidents.md](./incidents.md) · [safepoints.md](./safepoints.md) · [metaspace.md](./metaspace.md)

# JVM — Implementation

Idiomatic observation / flags (not writing a JVM).

```java
// Inspect runtime (useful in diagnostics, not business logic)
Runtime rt = Runtime.getRuntime();
long heapUsed = rt.totalMemory() - rt.freeMemory();
int cpus = rt.availableProcessors();

// Class loader identity (leak / duplicate-class interviews)
ClassLoader cl = MyService.class.getClassLoader();
Module module = MyService.class.getModule();
```

## Flags you should recognize (Java 25 / HotSpot)

| Flag / tool | Role |
|-------------|------|
| `-Xms` / `-Xmx` | Heap min / max |
| `-XX:+UseG1GC` / `-XX:+UseZGC` | Collector choice |
| `-XX:MaxMetaspaceSize=` | Cap metaspace |
| `jcmd <pid> VM.flags` | Effective flags |
| `jcmd <pid> VM.classloader_stats` | Loader pressure |
| `jhsdb` / `jcmd GC.heap_info` | Heap snapshot-ish info |
| JFR / async-profiler | CPU, allocation, safepoints |

```bash
java -Xms512m -Xmx2g -XX:+UseG1GC -XX:StartFlightRecording=filename=app.jfr,dumponexit=true -jar app.jar
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Reproduce OOM | `-Xmx` small + heap dump on OOM |
| See JIT decisions | `-XX:+PrintCompilation` (dev) / JFR Compiler |
| Classpath hell | modules + unique loader per plugin carefully |
| Native leaks | NMT (`-XX:NativeMemoryTracking=summary`) |

Related: [jvm-execution.md](../../jvm-internals/jvm-execution.md), [heap.md](../../jvm-internals/heap.md).

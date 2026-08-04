# JVM — Cheat Sheet

**Sources:** [../jvm-internals/README.md](../jvm-internals/README.md) · [java-interview-questions/jvm](../java-interview-questions/jvm/) · [interview-prep/jvm](../interview-prep/jvm/) · [experiments/jit-warmup-cold-vs-hot](../experiments/jit-warmup-cold-vs-hot/)

## Runtime areas (recall)

| Area | Holds | Failure |
|------|-------|---------|
| Heap | Objects | `OutOfMemoryError: Java heap space` |
| Stack | Frames / locals | `StackOverflowError` |
| Metaspace | Class metadata | Metaspace OOM / loader leaks |
| Native / direct | Off-heap buffers | Direct buffer / native OOM |

Depth: [jvm-internals](../jvm-internals/) · memory cross-link [memory cheat](./memory.md)

## Execution

```text
Bytecode → Interpreter → C1 → C2 (tiered)
Profiles change → possible deoptimization
```

| Topic | Source |
|-------|--------|
| JIT / warmup | [jvm bank q01](../java-interview-questions/jvm/q01-jit-warmup-sla.md) · [experiment](../experiments/jit-warmup-cold-vs-hot/) |
| Deopt | [q05](../java-interview-questions/jvm/q05-deoptimization.md) |
| Classloaders / leak | [q02](../java-interview-questions/jvm/q02-classloader-leak.md) |
| invokevirtual / IC | [q04](../java-interview-questions/jvm/q04-bytecode-invoke.md) |

## Interview triggers

| Symptom | First thought |
|---------|----------------|
| Slow first 10–15 min after deploy | Warmup / tiered compilation |
| Metaspace grows on hot redeploy | Classloader leak |
| Latency spikes + deopt events | Profile change / uncommon path hot |

## Tools (point to chapters)

JFR / `jcmd` / thread dumps / heap dumps / async-profiler — see [performance cheat](./performance.md), [jvm-observability](../performance-engineering/jvm-observability.md), and [jvm-internals](../jvm-internals/).

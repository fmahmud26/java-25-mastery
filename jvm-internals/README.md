# JVM Internals — Java 25 Deep Guide

How HotSpot runs your program: load bytecode, manage memory, interpret, JIT-compile, and pause safely at safepoints. This folder is **advanced interview + production diagnostics** depth — not a GC deep-dive (see GC materials elsewhere) and not a Java language tutorial.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Mental map

```text
.class bytes
    → Class Loaders (bootstrap / platform / application / custom)
    → Runtime Data Areas (heap, stacks, metaspace, PC, native stacks)
    → Execution Engine (interpreter → tiered JIT → native)
    → Safepoints / deopt / GC coordination
```

## Study path

1. Big picture: [jvm-architecture](./jvm-architecture.md) → [runtime-data-areas](./runtime-data-areas.md) → [jvm-execution](./jvm-execution.md)  
2. Bytes & load: [bytecode](./bytecode.md) → [class-loading](./class-loading.md) → [class-loaders](./class-loaders.md)  
3. Memory: [heap](./heap.md) · [stack](./stack.md) · [metaspace](./metaspace.md) · [pc-register](./pc-register.md) · [native-method-stack](./native-method-stack.md)  
4. Speed: [interpreter](./interpreter.md) → [jit-compiler](./jit-compiler.md) → [tiered-compilation](./tiered-compilation.md) → [escape-analysis](./escape-analysis.md) → [deoptimization](./deoptimization.md) → [safepoints](./safepoints.md)  
5. Ops: [diagnostic-tools](./diagnostic-tools.md) · [incidents](./incidents.md) · [interview](./interview.md)

## One-line PE rule

**Measure before blaming the JIT or GC — dumps, JFR, and `-Xlog` tell you whether the bottleneck is allocation, class loading, compilation, or safepoint time.**

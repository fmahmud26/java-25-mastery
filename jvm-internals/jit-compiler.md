# JIT Compiler

**Just-In-Time** compilation: hot bytecode → native machine code (`nmethod`) installed in the **code cache**.

## Mental Model

```text
Profile says “hot”
    → compile (C1 and/or C2)
    → install native code
    → next calls jump to nmethod
    → if assumptions break → deoptimize
```

## Technical Mechanism

| Compiler | Role |
|----------|------|
| **C1** (client) | Fast compile, lighter opts, profiling tiers |
| **C2** (server) | Expensive compile, peak opts |
| **Graal** (optional) | Alternative JIT on some builds / GraalVM |

Triggers: invocation counts, loop back-edges (OnStackReplace / OSR for hot loops mid-method).

## JVM Internals — favorite optimizations

- Inlining (critical — enables everything else)  
- Escape analysis → scalar replacement / lock elision  
- Loop unrolling, range-check elimination  
- Lock coarsening / elimination  
- Speculative type checks / uncommon traps  
- Intrinsics (`System.arraycopy`, `Math`, crypto, …)

Compilation runs on **compiler threads** (can show CPU); failed/deferred compile if code cache full.

## Production Implications

```bash
java -XX:+PrintCompilation …          # classic (still useful)
java -Xlog:jit+compilation=info …     # unified logging style
jcmd <pid> Compiler.CodeHeap_Analytics
```

- Warm-up policies for latency SLOs  
- Avoid huge methods / megamorphic call sites that block inlining  
- `-XX:ReservedCodeCacheSize=` if many dynamic proxies / generated code

## Incident — high CPU / JIT

CPU spike after traffic shift: compiler threads busy; or deopt loop recompiling. See [incidents.md](./incidents.md), [deoptimization.md](./deoptimization.md).

## Interview / PE

C1 vs C2? What is an intrinsic? Why is inlining the “queen” optimization? Code cache full — symptoms?

### Related

[tiered-compilation.md](./tiered-compilation.md) · [escape-analysis.md](./escape-analysis.md) · [deoptimization.md](./deoptimization.md) · [interpreter.md](./interpreter.md)

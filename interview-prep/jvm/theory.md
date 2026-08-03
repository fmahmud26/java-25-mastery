# JVM — Theory

## Mental model

```text
.java → javac → .class (bytecode)
              → Class Loader → Runtime Data Areas → Execution Engine
                                                    (Interpreter + JIT + GC)
```

HotSpot JVM: load bytecode, verify/link, interpret hot paths → JIT compile, manage heap with GC.

## Runtime data areas

| Area | Shared? | Holds |
|------|---------|-------|
| **Heap** | yes | Objects, arrays |
| **Metaspace** | yes | Class metadata (native off-heap) |
| **Java stacks** | per thread | Frames: locals, operand stack, return address |
| **PC register** | per thread | Current bytecode address |
| **Native method stacks** | per thread | JNI / native frames |

## Class loading (3 loaders + phases)

| Loader | Loads |
|--------|-------|
| Bootstrap | Core (`java.base`, etc.) — native |
| Platform | Other JDK modules |
| Application | classpath / module path |

Phases: **load → link** (verify, prepare, resolve) **→ initialize** (clinit). Parent-delegation: ask parent before loading (modules change visibility, not the interview sketch).

## Execution

- **Interpreter** — start fast, profile.
- **C1/C2 (or Graal) JIT** — compile hot methods; deoptimize when assumptions break.
- **Escape analysis** — if object never escapes method/thread, stack-allocate / scalar-replace / elide locks (optimization, not a language guarantee).

Related chapter: [jvm-architecture.md](../../jvm-internals/jvm-architecture.md), [class-loading.md](../../jvm-internals/class-loading.md).

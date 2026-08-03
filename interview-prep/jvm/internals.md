# JVM — Internals

Focus: **HotSpot memory + execution** (whiteboard depth).

```text
Class file → Bootstrap/Platform/App loader
          → Method area / Metaspace (klass, constants, bytecode)
          → Heap (instances)
Thread T:  [PC] [Java stack frames…] [native stack]
Execution: interpret → profile → C1 → C2 → (maybe deopt)
```

## Must-explain pieces

| Piece | Point |
|-------|-------|
| Parent delegation | Child asks parent first; avoids core class spoofing |
| Linking | Verify bytecode; prepare static fields; resolve symbolic refs |
| Heap vs stack | Objects on heap (usually); primitives/refs in frames |
| Metaspace | Class metadata off Java heap; OOM: Metaspace ≠ Java heap OOM |
| Safepoint | Threads pause at known points for GC / deopt / bias revoke |
| JIT tiers | Interpreter → C1 (fast compile) → C2 (heavy opts) |
| Escape analysis | Non-escaping allocs → scalar replace / stack / lock elision |
| Deoptimization | Speculative opts invalidated → fall back to interpreter/C1 |

## Bytecode sketch

```text
iload_1
iload_2
iadd
istore_3     // locals + operand stack machine
invokevirtual / invokedynamic  // call sites; indy for lambdas
```

`invokedynamic` + call sites: lambdas / string concat — linkage once, then fast.

## Escape analysis (say this clearly)

Not “objects live on the stack by default.” The JIT *may* eliminate heap allocation when analysis proves non-escape. Invisible in Java source; visible as fewer TLAB allocs in profiles.

Whiteboard until you can draw loaders + heap/stack/metaspace + interpret/JIT without notes.

Related: [bytecode.md](../../jvm-internals/bytecode.md), [jit-compiler.md](../../jvm-internals/jit-compiler.md), [metaspace.md](../../jvm-internals/metaspace.md), [stack.md](../../jvm-internals/stack.md).

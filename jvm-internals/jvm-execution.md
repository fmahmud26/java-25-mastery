# JVM Execution (End-to-End)

How a method runs on HotSpot from first call to steady state.

## Mental Model

```text
Load/link/init class
  → interpret + profile
  → tiered JIT (C1→C2)
  → run native
  → GC / safepoints as needed
  → deopt if speculation fails
```

## Technical Mechanism

```text
1. Class loaded / linked / initialized
2. First calls → interpreter
3. Profiling (types, branches, counts)
4. Hot → JIT → nmethod in code cache
5. GC reclaims heap; safepoints coordinate
6. Deopt → interpreter / recompile when wrong
```

## Invoke Paths

| Path | Meaning |
|------|---------|
| Interpreted | Bytecode templates |
| Compiled | Jump to nmethod |
| OSR | Loop replaced mid-method |
| Intrinsic | Hand-crafted native for known methods |
| Native | JNI / FFM downcall |

## JVM Internals

Execution engine = interpreter + compilers + GC coordination + runtime support (exceptions, monitors, invokedynamic linkage).

```bash
java -XX:+PrintCompilation YourApp
java -Xlog:class+load=info,safepoint=info,gc=info YourApp
```

## Production Implications

- Cold start ≠ steady state — capacity plans must say which.  
- “CPU high” triage: app threads vs GC vs **Compiler** threads vs safepoint stuck.  
- Tools: [diagnostic-tools.md](./diagnostic-tools.md).

## Interview / PE

Narrate execution of `public static void main` through first hot loop. Where does bytecode live vs native code?

### Related

[bytecode.md](./bytecode.md) · [interpreter.md](./interpreter.md) · [jit-compiler.md](./jit-compiler.md) · [safepoints.md](./safepoints.md)

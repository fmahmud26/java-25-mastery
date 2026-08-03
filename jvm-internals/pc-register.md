# Program Counter (PC Register)

Per-thread register holding the address / bytecode index of the **current instruction** for that thread’s Java method.

## Mental Model

```text
Each thread: “Where am I in the bytecode right now?”
```

## Technical Mechanism

| Trait | Detail |
|-------|--------|
| Per-thread | Yes |
| Java method | Points at current bytecode (bci) / instruction |
| Native method | Undefined for Java PC (thread is in native code) |
| Role | Multiplexing threads; interpreter state; debug / stack walks |

## JVM Internals

Smallest conceptual runtime data area — required by the JVM spec model. JIT-compiled code uses native IP; at safepoints the VM maps back to Java frames / bci for GC roots and stack traces.

## Production Implications

Rarely tuned. Appears in interviews and when explaining how stack traces / async samples map to bytecode lines (`-XX:+DebugNonSafepoints` / debug info).

## Interview / PE

What does the PC hold? Per-thread or shared? Value during JNI?

### Related

[stack.md](./stack.md) · [jvm-execution.md](./jvm-execution.md) · [native-method-stack.md](./native-method-stack.md) · [runtime-data-areas.md](./runtime-data-areas.md)

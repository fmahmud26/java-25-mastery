# Native Method Stack

Per-thread stack used while executing **native** code (JNI, and under the hood many FFM downcalls).

## Mental Model

```text
Java frames (JVM stack)  ↔  C/C++ frames (native method stack)
```

Some HotSpot builds use a combined stack layout; the **interview model** keeps them distinct per the JVM spec.

## Technical Mechanism

| Trait | Detail |
|-------|--------|
| Per-thread | Yes |
| Contents | Native frames, spilled registers, JNI local handles region, etc. |
| Failure | Overflow → `StackOverflowError` or hard native crash (SIGSEGV) |
| Modern API | **FFM** preferred over raw JNI for many cases — still native frames |

## JVM Internals

- Crossing JNI is expensive (transitions, safepoint checks, handle management).  
- Bugs in native code corrupt memory outside Java’s type safety.  
- `-Xss` influences stack size available to the thread (platform-dependent interaction with native).

## Production Implications

- Crash dumps with native frames → need `gdb` / native symbols, not only `jstack`.  
- Prefer FFM / pure Java when possible.  
- Huge thread counts still cost native stack reservation.

## Interview / PE

Why separate from Java stack? JNI vs FFM? Can native bugs cause heap corruption?

### Related

[stack.md](./stack.md) · [pc-register.md](./pc-register.md) · [jvm-architecture.md](./jvm-architecture.md)

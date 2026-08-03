# Deoptimization

When speculative JIT assumptions become false, HotSpot **throws away** that compiled code path and continues in a safer mode (interpreter / less optimized code) — **deopt**.

## Mental Model

```text
JIT: “I’ll bet receivers are always ArrayList”
Reality: suddenly a LinkedList arrives
    → uncommon trap → deoptimize → correct slow path → maybe recompile
```

Speed with a seatbelt.

## Technical Mechanism — common triggers

| Trigger | Example |
|---------|---------|
| Class hierarchy change | New subclass loaded → CHA invalidated |
| Type profile change | Megamorphic call site |
| Uncommon trap hit | Rare branch becomes common |
| Speculation failed | Null / bounds / class checks |
| Explicit | Breakpoints, some JVMTI, certain code cache changes |

```text
nmethod running → trap → unpack frames to interpreter state → continue
```

## JVM Internals

- Enables aggressive opts (inlining assumptions, CHA, speculative inlining).  
- Deopt is **correctness-preserving**, not a bug by itself.  
- **Deopt storms**: repeated compile → deopt → compile burns CPU and causes latency.  
- Appears in logs: `-XX:+TraceDeoptimization` / unified JIT logging (build-dependent).

## Production Implications

- Loading classes late (plugins) can invalidate compiled code — latency spike.  
- Polymorphic hot sites: redesign (type profiles, sealed hierarchies, sharper types).  
- Don’t panic at occasional deopt; panic at sustained high deopt rate + CPU.

## Incident — latency spike / JIT behavior

After enabling a plugin JAR, p99 spikes and compiler CPU rises → hierarchy/CHA deopts. See [incidents.md](./incidents.md).

## Interview / PE

Why allow wrong speculation? What is an uncommon trap? How does deopt relate to tiered compilation?

### Related

[jit-compiler.md](./jit-compiler.md) · [safepoints.md](./safepoints.md) · [tiered-compilation.md](./tiered-compilation.md)

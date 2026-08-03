# Stack (Java / JVM Stack)

Each thread has a **JVM stack** of **frames**. A frame is one method activation.

## Mental Model

```text
call foo()  →  push frame
return      →  pop frame

Frame ≈ locals + operand stack + link to constant pool / return info
```

```text
Thread stack
├── frame: main
├── frame: service
└── frame: repo.find  ← current (PC points into this method)
         locals: this, id, ResultSet ref ──► heap
```

## Technical Mechanism

```java
void foo() {
    int x = 1;               // primitive local in frame
    Object o = new Object(); // reference in frame; object on heap
    bar(o);
}
```

| Trait | Detail |
|-------|--------|
| Per-thread | Not shared (no data races on another thread’s locals) |
| Speed | Push/pop — cheap |
| Error | `StackOverflowError` |
| Size | `-Xss` (and OS limits) |

## JVM Internals

- Interpreter and JIT both use frame layouts (JIT may optimize heavily).  
- Stacks are **GC roots** — live refs keep heap objects alive.  
- Deep recursion or huge local arrays in frames → SOE.  
- Virtual threads: stacks are heap-chunked continuations — still logical frames; carriers have platform stacks.

## Production Implications

- Thousands of platform threads × large `-Xss` ⇒ significant RSS.  
- SOE in production: infinite recursion, regex/parser stack blowups, or reflective proxy chains.  
- Don’t confuse stack overflow with heap OOM.

## Incident — latency / crashes

Native + Java stack interplay on JNI-heavy paths; see [native-method-stack.md](./native-method-stack.md).

## Interview / PE

What is in a frame? Heap vs stack for `new`? Why per-thread? What does `-Xss` change?

### Related

[heap.md](./heap.md) · [pc-register.md](./pc-register.md) · [runtime-data-areas.md](./runtime-data-areas.md)

**Canonical stack/frames depth:** [../memory-management/stack.md](../memory-management/stack.md) · [../memory-management/stack-frames.md](../memory-management/stack-frames.md)

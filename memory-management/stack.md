# Stack (Java Memory)

Per-thread memory for **method frames** — not where Java objects normally live.

## Mental Model

```text
Thread A stack              Thread B stack
┌─────────────┐             ┌─────────────┐
│ frame: bar  │             │ frame: run  │
│ frame: foo  │             │ …           │
│ frame: main │             └─────────────┘
└─────────────┘
     refs ──► heap objects (shared)
```

Stack = call state. Heap = objects. Confusing them fails interviews and incidents.

## Technical Mechanism

Each **frame** holds:

| Part | Contents |
|------|----------|
| Local variable table | Primitives + references |
| Operand stack | Bytecode expression evaluation |
| Frame data | Return address, constant pool link, … |

```java
void foo(int a) {
    int b = a + 1;           // local in frame
    String s = "x";          // reference in frame → heap String
    bar(s);                  // new frame pushed
}                            // frames popped on return
```

| Concern | Detail |
|---------|--------|
| Isolation | Not shared across threads |
| Error | [StackOverflowError](./stackoverflowerror.md) |
| Size | `-Xss` (platform threads); VT stacks are chunked/grow differently |
| GC | Live frame refs are **GC roots** |

## JVM Internals

- Deep recursion or huge frames ⇒ SOE; not `OutOfMemoryError: Java heap space`.  
- Thousands of platform threads × large `-Xss` ⇒ significant **RSS** outside `-Xmx`.  
- JIT may optimize frames heavily; logical model still holds for debugging.  
- See also [stack-frames.md](./stack-frames.md).

## Production Implications

- Stack growth ≠ heap leak.  
- SOE stack traces show recursion; fix algorithm before blindly raising `-Xss`.  
- Thread pools: count × stack size is a capacity planning input.

## Interview / PE

What lives on the stack vs heap? Why are stacks GC roots? When is `-Xss` the right fix?

### Related

[heap.md](./heap.md) · [stackoverflowerror.md](./stackoverflowerror.md) · [object-retention.md](./object-retention.md)

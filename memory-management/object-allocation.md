# Object Allocation

How `new` becomes bytes on the heap — and when it doesn’t.

## Mental Model

```text
new User(...)
  → (usually) thread-local TLAB bump-pointer in Eden
  → object header + fields (+ padding)
  → reference returned to caller
```

Fast path is intentionally cheap; GC pays later for short-lived garbage.

## Technical Mechanism

```java
User u = new User("Ada");      // heap object + ref in frame/field
byte[] buf = new byte[1024];   // array on heap
```

| Path | When |
|------|------|
| TLAB / Eden | Common small/medium objects |
| Shared Eden / slow path | TLAB refill / contention |
| Humongous / large | Very large arrays/objects (e.g. G1) |
| Scalar replacement | JIT escape analysis — may skip heap |
| Off-heap | `ByteBuffer.allocateDirect`, FFM arenas — **not** Java heap |

```java
void hot() {
    Point p = new Point(1, 2); // may vanish after JIT if non-escaping
    use(p.x(), p.y());
}
```

## JVM Internals

- TLAB reduces atomic contention on the heap top.  
- Allocation rate drives GC frequency — “GC problem” is often “allocate too much.”  
- Large object allocation can stall / fragment — see [incidents.md](./incidents.md).  
- Header cost × object count matters at scale ([object-headers.md](./object-headers.md)).

## Production Implications

- Prefer streaming over `byte[]` of whole files.  
- Bound request body sizes.  
- Measure allocation with JFR / async-profiler **alloc** mode, not guesses.

## Interview / PE

What is a TLAB? Can objects be allocated on the stack? (Only via EA/scalar replacement — not a language rule.) Humongous risk?

### Related

[heap.md](./heap.md) · [object-lifecycle.md](./object-lifecycle.md) · [object-headers.md](./object-headers.md)

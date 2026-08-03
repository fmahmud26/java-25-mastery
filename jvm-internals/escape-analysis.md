# Escape Analysis

JIT analysis: does an object **escape** the allocating method or thread? If not, the compiler may optimize allocation and locking.

## Mental Model

```text
new Foo() inside a method
  → does Foo’s reference leave the method? (return, store in field, pass to other code that stores it, …)
  → does it get shared across threads?
If No → “non-escaping” → optimize aggressively
```

## Technical Mechanism — what the JIT *may* do

| Optimization | Meaning |
|--------------|---------|
| **Scalar replacement** | Object exploded into fields/registers; **no heap allocation** |
| **Stack allocation-ish** | Interview shorthand — HotSpot mainly scalar-replaces, not classic “alloc on Java stack” |
| **Lock elision** | `synchronized` on a proven non-escaping object can be removed |

```java
// Often EA-friendly: local, never published
Point add(Point a, Point b) {
    Point p = new Point(); // may be scalar-replaced
    p.x = a.x + b.x;
    p.y = a.y + b.y;
    return /* primitives only? */ ; // if you return p, it escapes!
}
```

If you `return p` or store into a static/heap field, it **escapes**.

## JVM Internals

- Part of C2 (and related) optimization pipeline — depends on inlining to see the full graph.  
- Not a Java language feature — **no guarantee**; never write logic that requires EA.  
- Allocation removal shows up as fewer `T.LAB` allocations in profilers for microbenchmarks.

## Production Implications

- Small temporary objects in hot loops can be “free” after warm-up — still measure.  
- Megamorphic / boundary-heavy code prevents inlining ⇒ EA never sees the object.  
- Microbenchmarks without warm-up lie about allocation.

## Incident — large object allocation

EA won’t save you: `new byte[50_000_000]` always escapes/huge — heap/humongous path. See [incidents.md](./incidents.md).

## Interview / PE

Define escape. Scalar replacement vs “stack allocation” precision. Can EA remove locks? Is it guaranteed?

### Related

[jit-compiler.md](./jit-compiler.md) · [heap.md](./heap.md) · [deoptimization.md](./deoptimization.md)

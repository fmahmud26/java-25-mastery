# Object Headers

Every HotSpot object starts with a **header** before instance fields — fixed overhead × object count.

## Mental Model

```text
[ header ][ fields … ][ alignment padding ]
```

Many tiny objects → header tax dominates.

## Classic Layout (interview model)

| Part | Role |
|------|------|
| Mark word | Lock state, GC age, identity hash (layout varies) |
| Class pointer | Type metadata (often compressed) |
| Array length | Arrays only |

## Java 25 — Compact Object Headers (JEP 519)

Product feature shrinking header size on supported 64-bit builds.

```bash
java -XX:+UseCompactObjectHeaders …
```

- **Not default** in Java 25 (default expected in a later JDK via follow-on work).  
- No application code changes.  
- Helps heaps with huge object counts; still measure.

## Production Implications

- Object churn + header cost → RAM and GC pressure.  
- Prefer fewer objects / primitive-friendly designs when counts explode (`byte[]` vs millions of boxed nodes).  
- Escape analysis can eliminate some objects entirely ([object-allocation.md](./object-allocation.md)).

## Interview / PE

What’s in a header? Why do compact headers matter? Are they on by default in 25?

### Related

[object-allocation.md](./object-allocation.md) · [heap.md](./heap.md)

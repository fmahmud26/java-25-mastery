# References (Reachability)

GC decides liveness from **GC roots** through reference chains. Strength matters.

## Mental Model

```text
GC roots: thread stacks, static fields, JNI refs, …
    └─ Strong  → must keep object
    └─ Soft    → keep until memory pressure (cache-ish)
    └─ Weak    → don’t keep for its own sake
    └─ Phantom → post-mortem cleanup signal (get() is null)
```

## Technical Mechanism

| Type | API | Cleared when |
|------|-----|----------------|
| Strong | ordinary `T` | Never “cleared” — object dies when unreachable |
| Soft | `SoftReference` | Under memory pressure (impl-defined timing) |
| Weak | `WeakReference` | When only weakly reachable (typically next GC cycle) |
| Phantom | `PhantomReference` | After becoming phantom-reachable; for queues/cleanup |

```java
Object strong = new byte[1024];
WeakReference<Object> weak = new WeakReference<>(strong);
strong = null;   // only weak left → collectible
```

## JVM Internals

Reachability strengths (spec-level intuition): strongly → softly → weakly → phantom → unreachable. Reference queues notify when refs are cleared / enqueued.

## Production Implications

- Most leaks = accidental **strong** retention.  
- Soft caches without size bounds still surprise under load (timing non-deterministic).  
- Prefer Caffeine/Guava with explicit eviction + metrics over raw `SoftReference` maps.  
- Prefer `Cleaner` over finalizers / hand-rolled phantoms for native cleanup.

## Interview / PE

Order of reachability? Soft vs weak for caches? Why phantom `get()` is null?

### Related

[strong-references.md](./strong-references.md) · [weak-references.md](./weak-references.md) · [soft-references.md](./soft-references.md) · [phantom-references.md](./phantom-references.md)

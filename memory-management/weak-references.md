# Weak References

`WeakReference<T>` — does **not** keep the referent alive for its own sake.

## Mental Model

```text
Only weak (and weaker) refs left → object eligible for collection.
Useful for “don’t pin this just because I pointed at it.”
```

## Technical Mechanism

```java
WeakReference<User> ref = new WeakReference<>(user);
User u = ref.get(); // may be null after GC

WeakHashMap<Key, Value> map = new WeakHashMap<>();
// keys are weak — entry can vanish after key collected
// values are strong — value can pin key indirectly if value→key
```

## Patterns

| Use | Caution |
|-----|---------|
| `WeakHashMap` as weak-key map | Values must not strongly reference keys |
| Canonical maps / class-related caches | Avoid pinning loaders |
| Weak listeners | Semantics must tolerate disappearance |

Pair with `ReferenceQueue` when you need cleanup when cleared.

## Soft vs Weak

| Soft | Weak |
|------|------|
| Cache “keep while memory OK” | Identity / non-pinning maps |
| Cleared under pressure | Cleared more eagerly when weakly reachable |

## Production Implications

Weak refs are not a substitute for bounded caches with hit-rate SLOs. `WeakHashMap` as a general cache is often the wrong tool.

## Interview / PE

When is an object weakly reachable? WeakHashMap value→key trap? Soft vs weak one-liner?

### Related

[soft-references.md](./soft-references.md) · [references.md](./references.md) · [object-retention.md](./object-retention.md)

# Strong References

The default reference: locals, fields, array elements, statics.

## Mental Model

```text
If a strong path from a GC root exists → object stays alive.
```

## Technical Mechanism

```java
List<byte[]> cache = new ArrayList<>();
cache.add(new byte[1_000_000]); // strong — lives until removed and list unreachable
```

| Where strong refs hide | Example |
|------------------------|---------|
| Static fields | `static Map CACHE` |
| Collections | Caches, session maps |
| Listeners | Event bus registrations |
| Inner classes | Hidden outer `this` |
| ThreadLocals | Values in `ThreadLocalMap` |
| Threads / pools | Runnable capturing large state |
| Caches | Unbounded |

## Production Implications

Almost every Java “memory leak” is **unintended strong reachability**. Weak/soft don’t fix a static `HashMap` that still holds strong values incorrectly designed.

## Incident Hooks

Large cache, listener leak, ThreadLocal leak — [incidents.md](./incidents.md).

## Interview / PE

Define strong reachability. Give three leak shapes that are “just strong refs.”

### Related

[memory-leaks.md](./memory-leaks.md) · [object-retention.md](./object-retention.md) · [weak-references.md](./weak-references.md)

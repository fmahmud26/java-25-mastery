# Phantom References

`PhantomReference<T>` — for **post-mortem** cleanup. `get()` always returns `null`.

## Mental Model

```text
Object becomes phantom-reachable
  → phantom enqueued on ReferenceQueue
  → your cleaner thread frees native peers / files
  → then clear the reference
```

You never resurrect the object through a phantom.

## Technical Mechanism

```java
ReferenceQueue<Object> queue = new ReferenceQueue<>();
Object obj = new byte[1024];
PhantomReference<Object> pr = new PhantomReference<>(obj, queue);
obj = null;
// after GC makes it phantom-reachable:
Reference<?> r = queue.remove(); // or poll
r.clear();
```

## Prefer Cleaner

```java
Cleaner cleaner = Cleaner.create();
cleaner.register(owner, () -> nativeFree(handle));
```

| Avoid | Prefer |
|-------|--------|
| `finalize()` | `Cleaner` / try-with-resources |
| Resurrection tricks | Explicit `close()` |

`finalize` is deprecated for removal and hostile to performance/GC.

## Production Implications

Use phantoms/Cleaner for **native** memory or file descriptors when `AutoCloseable` isn’t enough — still make `close()` the primary path.

## Interview / PE

Why is `get()` null? Phantom vs finalizer? Cleaner relationship?

### Related

[references.md](./references.md) · [object-lifecycle.md](./object-lifecycle.md)

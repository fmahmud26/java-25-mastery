# VarHandle — Modern JMM Access Modes

`java.lang.invoke.VarHandle` is the supported way to perform **plain, opaque, acquire, release, and volatile** accesses — and CAS — on fields, array elements, and off-heap layouts. Prefer it (or `Atomic*`) over legacy `Unsafe` for new concurrency infrastructure.

## Mental Model

```text
VarHandle = typed “handle” to a variable + memory semantics for each access
volatile mode ≈ volatile field
acquire/release ≈ lighter ordered pair (library-building tool)
plain ≈ ordinary field (no sync)
```

## Access Modes (interview table)

| Mode | Intent |
|------|--------|
| `get` / `set` (plain) | No ordering/visibility guarantees beyond ordinary fields |
| `getOpaque` / `setOpaque` | Atomicity without full acquire/release (advanced) |
| `getAcquire` / `setRelease` | One-way ordering — publish/consume patterns |
| `getVolatile` / `setVolatile` | Full volatile semantics |
| `compareAndSet` / `compareAndExchange` | CAS with mode-specific variants |

Exact guarantees match the JLS VarHandle spec — quote modes, don’t invent barrier names.

## Code

```java
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

final class Flag {
    private boolean ready;
    private static final VarHandle READY;

    static {
        try {
            READY = MethodHandles.lookup().findVarHandle(Flag.class, "ready", boolean.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    void publish() {
        READY.setRelease(this, true); // release store
    }

    boolean isReady() {
        return (boolean) READY.getAcquire(this); // acquire load
    }
}
```

For most applications, a `volatile boolean` or `AtomicBoolean` is clearer. VarHandle shines in **reusable concurrent structures**.

## Production Scenario — custom ring buffer

Library author uses release-store of an index and acquire-load on the consumer side so payload writes before the index publish are visible — without full volatile on every payload field.

## Failure Scenario

Using plain `get`/`set` on a VarHandle and assuming volatile behavior → classic visibility bugs with a modern API costume.

## When Not to Use

Business services: prefer `volatile`, `Atomic*`, CHM, queues. VarHandle is an infrastructure tool; misuse raises review cost.

## Relation to Atomic* and Unsafe

| API | Role |
|-----|------|
| `AtomicInteger` etc. | Safe, clear, optimized |
| `VarHandle` | Same engine, more modes/targets |
| `Unsafe` | Legacy; avoid in new code |

## Interview Questions

- Why VarHandle over Unsafe?  
- Acquire/release vs volatile?  
- Does VarHandle make ++ atomic without CAS? (No.)  

## Principal Perspective

Expect Staff+ library authors to know modes. Expect product Principals to **forbid** casual VarHandle in domain code without a reviewable HB story and tests (`jcstress` mindset).

### Related

[java-memory-model.md](./java-memory-model.md) · [happens-before.md](./happens-before.md) · [volatile.md](./volatile.md) · [cas.md](./cas.md) · [ordering.md](./ordering.md)

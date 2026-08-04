# Java Memory Model (JMM)

The contract for **what values a thread may see** when reading shared fields — visibility, atomicity of certain ops, and allowed reorderings.

## Mental Model

```text
Without sync: Thread A writes may be invisible / reordered from Thread B’s view
With happens-before: writes become visible in a defined way
```

There is no “global clock” guaranteeing immediate visibility of plain fields.

## Core Concept

The JMM (JSR-133 lineage, still the Java SE memory model) defines:

1. Which reorderings compilers/CPUs may perform.  
2. When a write becomes visible to another thread (**happens-before**).  
3. Which operations are atomic (e.g. references, `int`; `long`/`double` plain writes historically special — prefer `volatile`/`AtomicLong`).  

## How It Works Internally (L1→L3)

| Level | View |
|-------|------|
| L1 | Sync makes writes “show up” for other threads |
| L2 | HB edges from monitors, volatiles, atomics, thread lifecycle |
| L3 | Compilers emit barriers; CPUs use store buffers / cache protocols; JMM allows optimizations that preserve single-thread semantics |

Final fields have **initialization safety**: properly constructed immutables published safely are visible without extra sync on the fields themselves (still need safe publication of the *reference*).

## Code

```java
// Broken — no HB between ready write and read
boolean ready = false;
int payload;

void publisher() {
    payload = 42;
    ready = true; // plain store
}

void consumer() {
    if (ready) {
        use(payload); // may see ready=true but payload=0
    }
}

// Fixed — volatile ready or synchronized both sides
```

## Production Scenario — caches

Publishing an immutable cache snapshot: store in `volatile` field or `AtomicReference` so readers see a fully built map, not a torn publish.

## Production Scenario — feature flag

Flip `volatile boolean enabled`. Readers see the new value without locking. Compound “check flag + update map” still needs a stronger protocol.

## Failure Scenario

“It works on my laptop” single-core / lucky timing; fails under multi-core prod — classic JMM bug. Stress tests / `jcstress`-style litmus thinking catch these; ordinary unit tests often do not.

## Debugging Strategy

Hard to see in dumps. Reproduce under load; ask “what HB edge publishes this write?” Review DCL, lazy statics, and unsafe publication.

## Performance

Correct sync is cheaper than heisenbugs. Over-synchronization hurts throughput — use concurrent structures. Prefer [VarHandle](./varhandles.md) / `Atomic*` over inventing barrier folklore.

## Trade-offs

JMM is subtle; prefer high-level constructs (CHM, queues, atomics) that encode HB for you. Hand-rolled lock-free needs Principal-level justification.

## When Not to “Optimize with volatile”

Painting every field volatile “to be safe” — barriers everywhere, still wrong for compound actions.

## Interview Questions

- What problem does JMM solve?  
- Can a thread see stale values of a non-volatile field?  
- Final field safety?  
- Mutual exclusion vs visibility?  

## Principal-Level Discussion

Staff+ candidates must separate **mutual exclusion** from **visibility**. Designing lock-free structures requires JMM + CAS literacy; most product code should not invent that.

### Related

[happens-before.md](./happens-before.md) · [visibility.md](./visibility.md) · [volatile.md](./volatile.md) · [ordering.md](./ordering.md) · [varhandles.md](./varhandles.md) · [cas.md](./cas.md)

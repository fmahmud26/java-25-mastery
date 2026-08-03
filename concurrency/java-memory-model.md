# Java Memory Model (JMM)

The contract for **what values a thread may see** when reading shared fields — visibility, atomicity of certain ops, and allowed reorderings.

## Mental Model

```text
Without sync: Thread A writes may be invisible / reordered from Thread B’s view
With happens-before: writes become visible in a defined way
```

There is no “global clock” guaranteeing immediate visibility of plain fields.

## Internal Mechanics

Compilers/CPUs reorder for speed. Each thread has an effective view of memory. Synchronization actions (monitor unlock/lock, volatile write/read, certain atomics) create **happens-before** edges. Final fields have special initialization safety.

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

## Failure Scenario

“It works on my laptop” single-core / lucky timing; fails under multi-core prod — classic JMM bug.

## Debugging Strategy

Hard to see in dumps. Reproduce with stress tests/`jcstress` mindset; look for missing volatile/sync on flags.

## Performance

Correct sync is cheaper than heisenbugs. Over-synchronization hurts throughput — use concurrent structures.

## Trade-offs

JMM is subtle; prefer high-level constructs (CHM, queues, atomics) that encode HB for you.

## Interview Questions

- What problem does JMM solve?  
- Can a thread see stale values of a non-volatile field?  
- Final field safety?

## Principal-Level Discussion

Staff+ candidates must separate **mutual exclusion** from **visibility**. Designing lock-free structures requires JMM + CAS literacy; most product code should not invent that.

### Related

[happens-before.md](./happens-before.md) · [visibility.md](./visibility.md) · [volatile.md](./volatile.md) · [ordering.md](./ordering.md)

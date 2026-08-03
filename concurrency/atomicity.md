# Atomicity

An action appears indivisible — no thread observes a partial update.

## Mental Model

```text
count++  =  read + add + write  → NOT atomic on ints
long/double plain writes → not atomic on all platforms historically; use volatile/atomic
```

## Internal Mechanics

JMM guarantees atomicity for reads/writes of references and most primitives except that `long`/`double` may be split without volatile. Compound actions need locks or atomics.

## Code

```java
// Not atomic
balanceCents += amount;

// Atomic single variable
AtomicLong balance = new AtomicLong();
balance.addAndGet(amount);

// Multi-field invariant → lock
synchronized (this) {
    if (stock >= n) stock -= n;
}
```

## Production Scenario — inventory

`if (stock >= n) stock -= n` without sync → oversell under concurrent orders.

## Failure Scenario

Two payments both pass “sufficient funds” check — lost update.

## Debugging Strategy

Add metrics for negative stock; concurrency stress tests; review compound checks.

## Performance

Atomics for single counters; locks for invariants spanning fields; DB transactions for durable inventory.

## Trade-offs

In-memory atomic ≠ durable. Payments/inventory often need DB constraints.

## Interview Questions

- Why isn’t `++` atomic?  
- When is `volatile` insufficient?

## Principal-Level Discussion

Define the **invariant** first; pick the weakest tool that preserves it. Don’t use atomics for multi-field business rules.

### Related

[cas.md](./cas.md) · [atomic-variables.md](./atomic-variables.md) · [race-condition.md](./race-condition.md)

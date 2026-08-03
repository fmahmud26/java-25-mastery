# Race Conditions

Correctness depends on uncontrollable timing — lost updates, check-then-act bugs.

## Mental Model

```text
Thread A and B interleave on shared state → invariant broken
```

## Internal Mechanics

Not just “visibility” — logical interleaving of compound actions. Fix with atomicity (locks/CAS/DB constraints) + HB.

## Code

```java
// Race — inventory
if (stock >= qty) {          // check
    stock -= qty;            // act — another thread interleaved
}

// Fix
synchronized (lock) {
    if (stock >= qty) stock -= qty;
}
// or AtomicInteger CAS loop / DB UPDATE … WHERE stock >= qty
```

## Production Scenario — payments / inventory / orders

Double spend; oversell; duplicate order fulfillment without idempotency key.

## Failure Scenario

Intermittent prod-only failures — classic race. “Works in QA” low concurrency.

## Debugging Strategy

Stress tests; add unique constraints; logging with thread ids; ThreadSanitizer-like thinking; review check-then-act.

## Performance

Correct sync may reduce throughput — still cheaper than refunds.

## Trade-offs

Pessimistic locks vs optimistic CAS vs DB.

## Interview Questions

- Define race condition.  
- Example check-then-act.  
- How to fix inventory race?

## Principal-Level Discussion

Idempotency and DB constraints are part of race defense for payments — in-memory sync is insufficient multi-node.

### Related

[atomicity.md](./atomicity.md) · [deadlock.md](./deadlock.md) · [cas.md](./cas.md)

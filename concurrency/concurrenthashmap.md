# ConcurrentHashMap

Concurrent map — safe concurrent readers/writers; no nulls; weakly consistent iterators.

## Mental Model

```text
HashMap + concurrency ≠ CHM
per-bin / CAS design → scalable maps
compute/merge atomic per key
```

## Internal Mechanics

Java 8+ bin model with CAS and tree bins; concurrent resize. Guarantees: concurrent ops safe; iterators weakly consistent (no CME freeze). Aggregate ops atomic **per key**, not multi-key transactions.

## Code

```java
ConcurrentHashMap<String, LongAdder> hits = new ConcurrentHashMap<>();
hits.computeIfAbsent(userId, _ -> new LongAdder()).increment();

ConcurrentHashMap<String, PaymentSession> sessions = new ConcurrentHashMap<>();
sessions.compute(paymentId, (id, prev) -> {
    if (prev != null && prev.isTerminal()) return prev; // idempotent
    return PaymentSession.start(id);
});
```

## Production Scenario — payments / caches / high-traffic APIs

Idempotency keys, session cache, rate-limit counters (single JVM). Multi-node → Redis/DB.

## Failure Scenario

Compound check outside compute → race. Unbounded growth → OOM. Assuming iteration snapshot.

## Debugging Strategy

Size metrics; hot-key latency (one key contended); heap histogram for CHM entries.

## Performance

Excellent scaling until hot keys. Stripe keys or shard. Don’t use for huge multi-key atomic business txns.

## Trade-offs

CHM vs synchronized Map vs DB. Immutable map publish for rare updates.

## Interview Questions

- Why no nulls?  
- compute atomicity scope?  
- Iterator semantics?  
- HashMap concurrent use?

## Principal-Level Discussion

CHM is the default concurrent map — still an **in-process** cache. Define TTL/bounds; don’t treat as source of truth for money.

### Related

[cas.md](./cas.md) · [blockingqueue.md](./blockingqueue.md) · [race-condition.md](./race-condition.md)

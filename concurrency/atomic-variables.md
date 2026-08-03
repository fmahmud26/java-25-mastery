# Atomic Classes

`java.util.concurrent.atomic` — `AtomicInteger`, `AtomicLong`, `AtomicReference`, `AtomicBoolean`, field updaters, `LongAdder`, etc.

## Mental Model

```text
AtomicX = volatile-like visibility + CAS-based updates
LongAdder = striped stats under high write contention
```

## Internal Mechanics

Built on CAS/VarHandle. `incrementAndGet`, `compareAndSet`, `getAndUpdate`. `LongAdder` spreads contention across cells.

## Code

```java
AtomicLong paymentSeq = new AtomicLong();
long id = paymentSeq.incrementAndGet();

AtomicReference<Snapshot> snap = new AtomicReference<>(Snapshot.empty());
snap.updateAndGet(old -> old.withPut(key, val));

LongAdder hits = new LongAdder();
hits.increment();
long sum = hits.sum();
```

## Production Scenario — high-traffic APIs

Request counters, rate-limit windows (with care), idempotency version stamps.

## Failure Scenario

Using AtomicInteger for “check then act” across two atomics without care → still races. Treating Atomic as transactional multi-object DB.

## Debugging Strategy

Incorrect totals → lost updates elsewhere; or summing LongAdder mid-write (document approximate).

## Performance

AtomicLong OK; under extreme write contention for metrics prefer LongAdder. Hot single Atomic = contention hotspot.

## Trade-offs

Atomic vs synchronized vs LongAdder vs DB sequence.

## Interview Questions

- AtomicInteger vs volatile int?  
- When LongAdder?  
- AtomicReference for immutable snapshots?

## Principal-Level Discussion

Atomics for **single-variable** state machines and counters. Business aggregates spanning many keys → CHM compute or DB.

### Related

[cas.md](./cas.md) · [volatile.md](./volatile.md) · [atomicity.md](./atomicity.md)

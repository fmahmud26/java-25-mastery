# CopyOnWriteArrayList

Thread-safe list that copies array on each mutation — snapshot iterators.

## Mental Model

```text
reads lock-free on volatile array
writes copy-on-write (expensive)
```

## Internal Mechanics

Mutations synchronized copy; iterators immutable snapshot — no CME.

## Code

```java
CopyOnWriteArrayList<Consumer<PaymentEvent>> listeners = new CopyOnWriteArrayList<>();
listeners.add(ledger::onPayment);
for (var l : listeners) l.accept(event);
```

## Production Scenario — caches of listeners

Rare register, frequent dispatch.

## Failure Scenario

High write rate → GC thrash / CPU. Using as general list under writes.

## Debugging / Performance / Trade-offs

Write cost O(n). Prefer CHM or immutable publish for catalogs.

## Interview Questions

- When COW vs CHM?  
- Iterator semantics?

### Related

[concurrenthashmap.md](./concurrenthashmap.md) · [readwritelock.md](./readwritelock.md)

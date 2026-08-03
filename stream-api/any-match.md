# Short-circuit terminals — match & find

## anyMatch / allMatch / noneMatch

**What:** Boolean terminals; short-circuit when answer known.  
**Prod:** `txs.stream().anyMatch(Tx::flagged)` fraud gate.  
**Mistake:** `filter+.findFirst.isPresent` instead of `anyMatch`.

## findFirst / findAny

**What:** `Optional` element; `findFirst` respects encounter order; `findAny` parallel-friendly.  
**Prod:** First ERROR line; any matching config.  
**Mistake:** `findFirst` on parallel when order doesn’t matter — use `findAny`.

```java
boolean risky = txs.stream().anyMatch(t -> t.cents() > threshold);
Optional<Customer> c = customers.stream().filter(Customer::active).findFirst();
```

### Related

[terminal-operations.md](./terminal-operations.md) · [parallel-streams.md](./parallel-streams.md)

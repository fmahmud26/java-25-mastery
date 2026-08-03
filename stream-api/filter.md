# filter

Keep elements matching a `Predicate`; drop the rest.

## What Happens

```text
Stream<T> ──filter(p)──► Stream<T>     p.test(t) == true kept
```

Stateless (normally); lazy; order of survivors preserved in ordered streams.

## Why Useful

Business rules: paid only, active customers, ERROR logs, high-value txs.

## Production Example — transactions & customers

```java
List<Tx> settled = txs.stream()
        .filter(Tx::settled)
        .filter(t -> t.cents() >= minCents)
        .toList();

long activeEu = customers.stream()
        .filter(Customer::active)
        .filter(c -> "EU".equals(c.region()))
        .count();
```

Compose predicates for reuse:

```java
Predicate<Order> reportable = Order::paid.and(o -> !o.testOrder());
orders.stream().filter(reportable).toList();
```

## Performance Implications

Cheap predicate = cheap filter. Put **selective** filters early to reduce work for later heavy `map`/`sorted`. Parallel filter needs thread-safe predicates (pure!).

## Common Mistake

Filter with side effects; filtering after expensive map when you could filter first; using `filter` + `findFirst` when `anyMatch` would do for existence checks.

### Related

[map.md](./map.md) · [any-match.md](./any-match.md) · [lazy-evaluation.md](./lazy-evaluation.md)

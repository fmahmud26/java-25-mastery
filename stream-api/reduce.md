# reduce

Fold a stream into a single value with an associative combining function.

## What Happens

```text
identity (optional) + accumulator (+ combiner for parallel)
(a, b) → a ⊕ b ⊕ c ⊕ …
```

Forms: `reduce(op)` → `Optional`; `reduce(identity, op)`; three-arg form for map-reduce style.

## Why Useful

Custom aggregates when collectors/sum don’t fit — domain `Money` totals, max by comparator, merging stats objects.

## Production Example — transactions / reporting

```java
Money total = txs.stream()
        .filter(Tx::settled)
        .map(Tx::amount)
        .reduce(Money.zero("USD"), Money::plus);

Optional<Tx> largest = txs.stream()
        .reduce((a, b) -> a.cents() >= b.cents() ? a : b);

// Prefer when available:
long totalCents = txs.stream().mapToLong(Tx::cents).sum();
```

## Performance Implications

Parallel reduce requires **associative**, stateless operators — otherwise wrong answers. Identity must be true identity (`0` for sum, not `null`). `collect` often clearer/faster for mutable aggregations.

## Common Mistake

```java
// Broken parallel — mutating shared list in reducer
reduce(new ArrayList<>(), (list, x) -> { list.add(x); return list; }, ...)
```

Use `collect(toList())` instead. Non-associative ops under `parallel()`.

### Related

[collect.md](./collect.md) · [binary-operator](../functional-programming/binary-operator.md) · [parallel-streams.md](./parallel-streams.md)

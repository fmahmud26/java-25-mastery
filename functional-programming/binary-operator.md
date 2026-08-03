# BinaryOperator\<T>

`BiFunction<T,T,T>` — combine two values of the same type (reducers, merges).

## Mental Model

```text
(T, T) ──apply──► T
reduce / max / merge
```

## Imperative vs Functional

```java
Money total = Money.zero("USD");
for (Money m : lines) total = total.plus(m);

Money total = lines.stream().reduce(Money.zero("USD"), Money::plus);
// Money::plus as BinaryOperator<Money>
```

## Production Example

```java
BinaryOperator<Money> sum = Money::plus;
BinaryOperator<Money> max = BinaryOperator.maxBy(Comparator.comparingLong(Money::cents));

Money orderTotal = lineAmounts.stream().reduce(Money.zero(ccy), sum);

map.merge(sku, qty, Integer::sum); // BinaryOperator via IntBinary… / autobox
ConcurrentHashMap<String, Money> totals = ...;
totals.merge(sku, delta, Money::plus);
```

`BinaryOperator.minBy` / `maxBy`.

## When Better / Worse

| Better | Worse |
|--------|-------|
| Associative reducers | Non-associative ops in parallel reduce — wrong results |
| Map merge functions | Complex multi-field merge buried in lambda — use named method |

## Performance & Readability

Parallel reduce requires associative, stateless operators. Prefer named methods (`Money::plus`) for clarity.

## Common Mistake

Parallel `reduce` with shared mutable accumulator — broken. Use immutable combine or `collect`.

## Interview / PE

- BinaryOperator vs BiFunction?  
- Why associativity for parallel reduce?  
- **PE:** CHM merge for rate-limit counters — operator choice?

### Related

[bi-function.md](./bi-function.md) · [function.md](./function.md) · [immutability.md](./immutability.md)

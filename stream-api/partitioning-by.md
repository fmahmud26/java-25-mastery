# partitioningBy

Specialized grouping with a **boolean** classifier → `Map<Boolean, List<T>>` (always both keys).

## What Happens

```text
predicate true  → map.get(true)
predicate false → map.get(false)
```

Optional downstream collector on each side.

## Why Useful

Binary splits: risky vs safe txs, active vs inactive customers, even/odd buckets, paid vs unpaid.

## Production Example

```java
Map<Boolean, List<Tx>> byRisk = txs.stream()
        .collect(Collectors.partitioningBy(t -> t.cents() >= riskThreshold));

Map<Boolean, Long> countByFlag = txs.stream()
        .collect(Collectors.partitioningBy(Tx::flagged, Collectors.counting()));

List<Tx> highRisk = byRisk.get(true);
List<Tx> normal = byRisk.get(false);
```

## Performance Implications

Same as grouping with 2 keys — simple and clear. Downstream avoids keeping full lists when you only need counts/sums.

## Common Mistake

Using `groupingBy` with boolean keys for the same job — `partitioningBy` is clearer and always includes both `true` and `false` entries (lists may be empty).

### Related

[grouping-by.md](./grouping-by.md) · [collect.md](./collect.md)

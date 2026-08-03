# distinct

Stateful deduplication using `equals` / `hashCode`.

## What Happens

First occurrence kept (ordered streams); later duplicates dropped.

## Why Useful

Unique SKUs, customer ids, log fingerprints.

## Production Example

```java
List<String> skus = orders.stream()
        .flatMap(o -> o.lines().stream())
        .map(OrderLine::sku)
        .distinct()
        .toList();
```

## Performance Implications

Maintains a seen set — memory O(cardinality).

## Common Mistake

Broken `equals`/`hashCode`; distinct on mutable keys.

### Related

[stateful-operations.md](./stateful-operations.md) · [flatmap.md](./flatmap.md)

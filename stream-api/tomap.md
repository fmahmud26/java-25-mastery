# toMap

Collect entries into a `Map` with key and value mappers (+ optional merge and map supplier).

## What Happens

```text
keyMapper(t) → K
valueMapper(t) → V
duplicate keys → IllegalStateException unless merge function provided
```

## Why Useful

Customer/order indexes, caches from lists, config overlays, last-wins or sum-on-collision policies.

## Production Example

```java
Map<String, Customer> byId = customers.stream()
        .collect(Collectors.toMap(
                c -> c.id().value(),
                Function.identity()));

// last order wins per customer
Map<String, Order> latestByCustomer = orders.stream()
        .collect(Collectors.toMap(
                o -> o.customerId().value(),
                Function.identity(),
                (a, b) -> a.createdAt().isAfter(b.createdAt()) ? a : b));

Map<String, Long> qtyBySku = lines.stream()
        .collect(Collectors.toMap(
                OrderLine::sku,
                OrderLine::quantity,
                Long::sum,
                LinkedHashMap::new));
```

## Performance Implications

Hash map build — average O(n). Duplicate-heavy data without merge → crash. `toConcurrentMap` for parallel-friendly builds.

## Common Mistake

Forgetting merge on duplicate keys; using `toMap` when `groupingBy` is the real need (1→many); null keys/values (NPE).

### Related

[grouping-by.md](./grouping-by.md) · [collect.md](./collect.md)

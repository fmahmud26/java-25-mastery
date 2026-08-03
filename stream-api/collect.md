# collect

Mutable reduction into a container via a `Collector` (supplier, accumulator, combiner, finisher).

## What Happens

```text
Stream<T> ──collect(collector)──► R
```

JDK collectors: lists/sets/maps, joining, grouping, partitioning, summarizing. Java 16+ [toList()](./tolist.md) for simple unmodifiable lists.

## Why Useful

The workhorse for reporting: group orders, index customers, join CSV, build maps for lookups.

## Production Example

```java
List<Order> paid = orders.stream().filter(Order::paid).toList();

Map<String, List<Order>> byCustomer = orders.stream()
        .collect(Collectors.groupingBy(o -> o.customerId().value()));

String csvIds = orders.stream()
        .map(o -> o.id().value())
        .collect(Collectors.joining(","));

Map<String, Customer> index = customers.stream()
        .collect(Collectors.toMap(c -> c.id().value(), Function.identity()));
```

## Performance Implications

Collectors allocate result structures — expected. Downstream collectors (`groupingBy` + `summingLong`) do one pass. Parallel collect needs concurrent-friendly collectors (`groupingByConcurrent`) when appropriate.

## Common Mistake

`collect(Collectors.toList())` then treating as immutable; using `reduce` to build lists; forgetting merge functions in `toMap` for duplicate keys → `IllegalStateException`.

### Related

[grouping-by.md](./grouping-by.md) · [tomap.md](./tomap.md) · [joining.md](./joining.md) · [tolist.md](./tolist.md)

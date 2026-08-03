# groupingBy

Classifier → `Map<K, List<T>>` (or custom map/downstream collector).

## What Happens

```text
each element → key = classifier.apply(e)
map accumulates lists (or downstream reduction) per key
```

Overloads: classifier; classifier + downstream; classifier + map factory + downstream.

## Why Useful

Analytics/reporting: orders by status, txs by merchant, logs by service, customers by region.

## Production Example

```java
Map<Status, List<Order>> byStatus = orders.stream()
        .collect(Collectors.groupingBy(Order::status));

Map<String, Long> revenueByCustomer = orders.stream()
        .filter(Order::paid)
        .collect(Collectors.groupingBy(
                o -> o.customerId().value(),
                Collectors.summingLong(Order::totalCents)));

Map<String, Optional<Tx>> maxTxByMerchant = txs.stream()
        .collect(Collectors.groupingBy(
                Tx::merchantId,
                Collectors.maxBy(Comparator.comparingLong(Tx::cents))));

// top employee-style report
Map<Department, Employee> topByDept = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingInt(Employee::salary)),
                        Optional::orElseThrow)));
```

## Performance Implications

One pass, but holds grouped lists in memory — huge cardinality keys → heavy maps. Downstream aggregations (`summingLong`, `counting`) avoid storing every element when you only need summaries. Parallel: consider `groupingByConcurrent`.

## Common Mistake

Grouping then streaming each list for a sum (multi-pass) instead of downstream `summingLong`; null classifier keys (NPE); assuming map is sorted (use `TreeMap` supplier if needed).

### Related

[partitioning-by.md](./partitioning-by.md) · [collect.md](./collect.md) · [tomap.md](./tomap.md)

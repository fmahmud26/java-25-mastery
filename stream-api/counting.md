# Downstream collectors — counting / summarizing / mapping

## counting

```java
Map<String, Long> ordersPerCustomer = orders.stream()
        .collect(Collectors.groupingBy(o -> o.customerId().value(), Collectors.counting()));
```

## summarizing

```java
LongSummaryStatistics stats = orders.stream()
        .collect(Collectors.summarizingLong(Order::totalCents));
// stats.getSum(), getAverage(), getMax()…
```

## mapping

```java
Map<Status, List<String>> idsByStatus = orders.stream()
        .collect(Collectors.groupingBy(
                Order::status,
                Collectors.mapping(o -> o.id().value(), Collectors.toList())));
```

### Related

[grouping-by.md](./grouping-by.md) · [collect.md](./collect.md) · [primitive-streams.md](./primitive-streams.md)

# count / min / max

## count

```java
long errors = lines.filter(l -> l.contains("ERROR")).count();
```

Prefer `Collectors.counting()` as downstream of grouping.

## min / max

```java
Optional<Order> maxOrder = orders.stream()
        .max(Comparator.comparingLong(Order::totalCents));
```

Primitive: `mapToLong(...).max()`.

### Related

[reduce.md](./reduce.md) · [terminal-operations.md](./terminal-operations.md)

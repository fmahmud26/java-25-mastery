# mapping (downstream)

`Collectors.mapping(mapper, downstream)` — transform before nested collect.

```java
Map<String, Set<String>> skusByCustomer = orders.stream()
        .collect(Collectors.groupingBy(
                o -> o.customerId().value(),
                Collectors.flatMapping(
                        o -> o.lines().stream().map(OrderLine::sku),
                        Collectors.toSet())));
```

### Related

[grouping-by.md](./grouping-by.md) · [counting.md](./counting.md)

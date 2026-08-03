# toList (Java 16+)

`stream.toList()` — terminal collecting to an **unmodifiable** `List`.

## What Happens

Shorthand for a reliable immutable list result (not `Collectors.toList()` which is mutable).

## Why Useful

Default end of most read-only pipelines — DTOs, filtered orders, report rows.

## Production Example

```java
List<String> skus = orders.stream()
        .flatMap(o -> o.lines().stream())
        .map(OrderLine::sku)
        .distinct()
        .toList();
// skus.add("x"); // UnsupportedOperationException
```

## Performance Implications

Similar to collect-to-list; prefer over manual `forEach` + `ArrayList`.

## Common Mistake

Expecting mutability; using `Collectors.toList()` when unmodifiable was intended (or vice versa).

### Related

[collect.md](./collect.md)

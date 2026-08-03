# joining

Concatenate `CharSequence` elements into one `String` with optional delimiter/prefix/suffix.

## What Happens

```text
Stream<CharSequence> ──joining(",")──► "a,b,c"
```

Uses `StringBuilder` internally via collector.

## Why Useful

CSV-ish reports, SQL `IN` clause stubs (still parameterize!), display lists, log summaries.

## Production Example — reporting

```java
String orderIds = orders.stream()
        .filter(Order::paid)
        .map(o -> o.id().value())
        .collect(Collectors.joining(","));

String reportLine = customers.stream()
        .map(Customer::email)
        .collect(Collectors.joining(";", "emails=[", "]"));
```

## Performance Implications

Fine for typical report sizes. For huge joins, consider writing to a `Writer`/file incrementally instead of one giant string.

## Common Mistake

Building SQL with joined raw ids (injection risk); joining after `map(Object::toString)` on huge objects without limiting.

### Related

[collect.md](./collect.md) · [map.md](./map.md)

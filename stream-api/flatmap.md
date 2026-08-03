# flatMap

One-to-many: each element maps to a **stream** of values; results are concatenated into one stream.

## What Happens

```text
Stream<T> ──flatMap(f)──► Stream<R>     f: T → Stream<R>
```

Also `flatMapToInt` / `ToLong` / `ToDouble`. Replaces nested loops over parent→children.

## Why Useful

Order → lines; customer → accounts; log line → parsed tokens; `Optional` flattening patterns.

## Production Example — orders / analytics

```java
// All SKUs purchased (orders → lines)
List<String> skus = orders.stream()
        .filter(Order::paid)
        .flatMap(o -> o.lines().stream())
        .map(OrderLine::sku)
        .distinct()
        .toList();

long unitsSold = orders.stream()
        .flatMap(o -> o.lines().stream())
        .mapToLong(OrderLine::quantity)
        .sum();

// Optional flatMap style (Stream of optional ids)
List<String> present = streamOfOptionalIds
        .flatMap(Optional::stream)  // Java 9+
        .toList();
```

## Performance Implications

Creates many short-lived streams — usually fine. Avoid flatMapping into huge explosions without downstream `limit`. Prefer primitive flatMaps for numeric fan-out.

## Common Mistake

```java
.map(o -> o.lines().stream())  // Stream<Stream<Line>> — wrong
.flatMap(o -> o.lines().stream()) // Stream<Line> — right
```

Using `map` when you needed flatten; side effects inside flatMap functions.

### Related

[map.md](./map.md) · [optional-and-streams.md](./optional-and-streams.md) · [reduce.md](./reduce.md)

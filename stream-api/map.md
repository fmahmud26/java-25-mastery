# map

One-to-one transform: each element becomes exactly one new element.

## What Happens

```text
Stream<T> ──map(f)──► Stream<R>     f: T → R
```

Applies `f` lazily per element when pulled by a terminal. No flattening (see [flatmap.md](./flatmap.md)).

## Why Useful

DTO projection, extracting fields, unit conversion — core of reporting pipelines.

## Production Example — orders → responses

```java
List<OrderResponse> body = orders.stream()
        .filter(o -> o.status() == Status.PAID)
        .map(o -> new OrderResponse(
                o.id().value(),
                o.customerId().value(),
                o.totalCents()))
        .toList();

List<String> customerIds = orders.stream()
        .map(Order::customerId)
        .map(CustomerId::value)
        .distinct()
        .toList();
```

## Performance Implications

Cheap if `f` is cheap. Allocates new objects per element (DTOs). Prefer `mapToLong`/`mapToInt` when mapping to primitives to avoid boxing ([primitive-streams.md](./primitive-streams.md)).

## Common Mistake

Side effects inside `map` (DB write, logging business events). Use a service call / `forEach` after a pure map. Nested collections → need `flatMap`, not `map`.

### Related

[filter.md](./filter.md) · [flatmap.md](./flatmap.md) · [side-effects.md](./side-effects.md)

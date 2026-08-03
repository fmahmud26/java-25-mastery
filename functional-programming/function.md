# Function\<T,R>

`R apply(T t)` — transform one value to another. Foundation of `map`.

## Mental Model

```text
T ──apply──► R
andThen / compose for pipelines
```

## Imperative vs Functional

```java
List<String> ids = new ArrayList<>();
for (Order o : orders) ids.add(o.id().value());

List<String> ids = orders.stream().map(o -> o.id().value()).toList();
List<String> ids = orders.stream().map(Order::id).map(OrderId::value).toList();
```

## Production Example

```java
public final class OrderMapper {
    public static final Function<Order, OrderResponse> TO_RESPONSE = o ->
            new OrderResponse(o.id().value(), o.totalCents(), o.status().name());

    public static final Function<OrderEntity, Order> TO_DOMAIN = e ->
            new Order(new OrderId(e.getId()), e.getTotalCents(), Status.valueOf(e.getStatus()));
}

OrderResponse dto = Optional.of(order).map(OrderMapper.TO_RESPONSE).orElseThrow();

Function<String, String> normalizeSku =
        ((Function<String, String>) String::strip)
                .andThen(String::toUpperCase);
```

## Composition

`andThen` = left-to-right; `compose` = before. See [function-composition.md](./function-composition.md).  
`Function.identity()`. Primitive: `ToLongFunction`, `IntFunction`, …

## When Better / Worse

| Better | Worse |
|--------|-------|
| Pure DTO mappers, normalization | Function that hits DB/network mid-pipeline silently |
| Injectable converters | God `Function` with 50-line lambda |

## Performance & Readability

Extract named `Function` constants or methods for reuse. Hot path: avoid boxing with specialized functions.

## Common Mistake

Using `Function` where `UnaryOperator` / `Consumer` fits better — wrong intent.

## Interview / PE

- `andThen` vs `compose`?  
- **PE:** MapStruct/codegen vs hand-written `Function` mappers — when each?

### Related

[unary-operator.md](./unary-operator.md) · [function-composition.md](./function-composition.md) · [immutability.md](./immutability.md)

# API Design

Public types are **long-lived contracts** — design for evolution and honesty.

## Before

```java
Order doIt(String a, String b, boolean flag, boolean flag2, Map<String,Object> extras);
// unclear, boolean trap, bag of holding
```

## After

```java
public interface OrderService {
    OrderId place(PlaceOrder command);
    Optional<OrderView> find(OrderId id);
    List<OrderView> listOpen(); // unmodifiable
}

public record PlaceOrder(
        CustomerId customerId,
        List<LineItem> items,
        ShippingMethod shipping
) {
    public PlaceOrder {
        items = List.copyOf(items);
        if (items.isEmpty()) throw new IllegalArgumentException("items");
    }
}
```

## Guidelines

- Smallest useful surface; hide modules/internals  
- Command/query objects > long param lists  
- Don’t return mutable internals  
- Null policy explicit (`Optional` sparingly — see [optional.md](./optional.md))  
- Breaking changes need versioning / compatibility window  

## Trade-offs

Flexibility (maps/JSON blobs) vs safety (typed commands). Public libraries need more caution than internal services.

## PE Decision

Review API PRs like public cloud APIs when multiple teams consume them.

### Related

[error-handling.md](./error-handling.md) · [records.md](./records.md) · [sealed-classes.md](./sealed-classes.md)

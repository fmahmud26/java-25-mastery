# Records

## Purpose

Concise, immutable **data carriers** with correct equals/hashCode/toString.

## Before

```java
public final class LineItem {
    private final String sku;
    private final int qty;
    // ctor, getters, equals, hashCode, toString × 40 lines
}
```

## After

```java
public record LineItem(String sku, int qty) {
    public LineItem {
        Objects.requireNonNull(sku);
        if (qty <= 0) throw new IllegalArgumentException("qty");
    }
}
```

## Use for

Commands, events, DTOs, value objects, pattern-matching payloads.

## Don’t use for

JPA entities (mutable identity), types needing inheritance hierarchies (prefer sealed + records as permitted).

## Trade-offs

Great for values; awkward when you need gradual mutability or bean conventions without accessors config.

## PE Decision

Default to records for new DTOs/values; migrate Lombok data classes when touching files.

### Related

[immutability.md](./immutability.md) · [sealed-classes.md](./sealed-classes.md) · [api-design.md](./api-design.md)

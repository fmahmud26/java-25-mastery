# Builder

## Problem

Construct a complex object with many optional parts/fields without telescoping constructors or invalid intermediate states.

## Forces

- Many optional parameters  
- Need readable construction  
- Want immutability of the final product  
- Validation once at build time  

## Naive solution

```java
new Order(userId, items, null, null, null, true, false, "UPS", null, coupon);
// or 8 overloaded constructors
```

Unreadable; easy to swap args; object can be half-mutated.

## Pattern

Step-by-step construction API (`builder`) that produces the final object (often a record/immutable).

## Implementation

```java
public record Order(
        String customerId,
        List<LineItem> items,
        Address shipping,
        String coupon,
        boolean giftWrap
) {
    public static Builder builder(String customerId) {
        return new Builder(customerId);
    }
    public static final class Builder {
        private final String customerId;
        private List<LineItem> items = List.of();
        private Address shipping;
        private String coupon;
        private boolean giftWrap;
        private Builder(String customerId) { this.customerId = customerId; }
        public Builder items(List<LineItem> items) { this.items = List.copyOf(items); return this; }
        public Builder shipping(Address a) { this.shipping = a; return this; }
        public Builder coupon(String c) { this.coupon = c; return this; }
        public Builder giftWrap(boolean v) { this.giftWrap = v; return this; }
        public Order build() {
            if (items.isEmpty()) throw new IllegalStateException("items required");
            if (shipping == null) throw new IllegalStateException("shipping required");
            return new Order(customerId, items, shipping, coupon, giftWrap);
        }
    }
}

Order order = Order.builder("C-1")
        .items(lines)
        .shipping(addr)
        .coupon("SAVE10")
        .build();
```

Java records + builder; or `StringBuilder`-style for queries.

## Trade-offs

| + | − |
|---|---|
| Readable, validatable | Boilerplate (mitigate with records/tools) |
| Immutable products | Overkill for 2-field DTOs |
| Fluent APIs | Mutable builder not thread-safe (usually OK local) |

## When to use

Complex domain objects, test data, query objects, HTTP clients.

## When NOT to use

Simple types; prefer constructors/factories.

## Production example

**Order service:** build an order from cart + address + gift options with validation at `build()`.

## Interview question

*Builder vs telescoping constructor vs JavaBeans setters? How do you keep the product immutable?*

**SOLID/LLD:** SRP (construction vs behavior); LLD complex aggregates.

### Related

[factory.md](./factory.md) · [singleton.md](./singleton.md)

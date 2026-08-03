# Defensive Programming

## Purpose

Assume bad inputs, partial failures, and clock skew — fail **loudly** at boundaries, keep core calm.

## Before

```java
public void refund(Order order, long amount) {
    order.setStatus("REFUNDED"); // no checks
    gateway.refund(order.getId(), amount);
}
```

## After

```java
public void refund(Order order, Money amount) {
    Objects.requireNonNull(order, "order");
    Objects.requireNonNull(amount, "amount");
    if (!order.isPaid()) throw new IllegalStateException("not paid");
    if (amount.gt(order.capturable())) throw new IllegalArgumentException("amount");
    gateway.refund(order.id(), amount);
    order.markRefunded(amount);
}
```

## Techniques

Validate at edges; copy defensive collections; timeouts on IO; idempotency keys; never trust client prices.

## Trade-offs

Too defensive inside hot private loops → noise. Prefer **parse, don’t validate** deep inside once types are trusted.

## PE Decision

Hard validation at API/adapters; domain invariants in constructors/records.

### Related

[immutability.md](./immutability.md) · [error-handling.md](./error-handling.md) · [api-design.md](./api-design.md)

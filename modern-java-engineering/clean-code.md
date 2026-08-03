# Clean Code (Production Sense)

Clean code is **change-friendly clarity**, not dogma (no universal 5-line method religion).

## Before

```java
// 80-line method: validate, price, pay, email, metrics mixed
public void checkout(Map<String,Object> m) { /* ... */ }
```

## After

```java
public CheckoutResult checkout(CheckoutCommand cmd) {
    var reserved = inventory.reserve(cmd.items());
    var paid = payments.charge(cmd.payment());
    var order = orders.create(cmd, reserved, paid);
    events.publish(new OrderPaid(order.id()));
    return CheckoutResult.of(order.id());
}
```

Names reveal intent; one level of abstraction per method; side effects obvious.

## Practices that pay rent

| Practice | Why |
|----------|-----|
| Intention-revealing names | Search & review speed |
| Small cohesive types | SRP without class explosion |
| Delete dead code | Reduces fear |
| Comments for *why* | Not narrate *what* |

## Trade-offs

Over-fragmentation → hop fatigue. Under-structure → ball of mud. Prefer clarity for **hot paths of change**.

## PE Decision

Enforce in review for money/auth paths; don’t bikeshed every DTO getter name.

### Related

[api-design.md](./api-design.md) · [maintainability.md](./maintainability.md) · [solid.md](./solid.md)

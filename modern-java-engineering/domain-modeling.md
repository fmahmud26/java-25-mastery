# Domain Modeling (Engineering Angle)

Model **business rules in types**, not only in if-statements.

## Before

```java
if (status.equals("PAID") && amount > 0) { ... }
```

## After

```java
order.pay(payment); // enforces transitions inside Order
Money total = line.items().stream().map(LineItem::subtotal).reduce(Money.ZERO, Money::plus);
```

Use records + sealed outcomes; keep persistence models separate from domain when they diverge.

### Related

[records.md](./records.md) · [sealed-classes.md](./sealed-classes.md) · [immutability.md](./immutability.md)

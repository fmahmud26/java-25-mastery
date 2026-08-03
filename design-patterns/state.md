# State

## Problem

An object’s behavior changes drastically based on an explicit lifecycle (order: `NEW → PAID → SHIPPED`), and transitions must be enforced.

## Forces

- Illegal transitions must fail loudly  
- Behavior per state without giant `switch`  
- Clear lifecycle for business/compliance  
- Possible state-specific data  

## Naive solution

```java
if (status == PAID) { /* ship */ }
else if (status == NEW) { /* pay */ }
// flags + boolean soup; invalid transitions slip in
```

## Pattern

State interface; concrete states implement transitions; context delegates.

## Implementation

```java
public interface OrderState {
    OrderState pay(OrderContext ctx);
    OrderState ship(OrderContext ctx);
}

public final class NewOrder implements OrderState {
    public OrderState pay(OrderContext ctx) {
        ctx.capturePayment();
        return new PaidOrder();
    }
    public OrderState ship(OrderContext ctx) {
        throw new IllegalStateException("pay first");
    }
}

public final class PaidOrder implements OrderState {
    public OrderState pay(OrderContext ctx) {
        throw new IllegalStateException("already paid");
    }
    public OrderState ship(OrderContext ctx) {
        ctx.dispatchWarehouse();
        return new ShippedOrder();
    }
}

public final class OrderContext {
    private OrderState state = new NewOrder();
    public void pay() { state = state.pay(this); }
    public void ship() { state = state.ship(this); }
}
```

## Trade-offs

| + | − |
|---|---|
| Explicit transitions | Many classes |
| Removes flag logic | Mapping to DB enums needed |
| Easy to test states | Overkill for 2-status entities |

## When to use

Orders, payments, tickets, connections, workflow engines.

## When NOT to use

Simple boolean; Strategy (algorithms without lifecycle).

## Production example

**Marketplace orders:** cannot ship before pay; cannot pay twice; refunds as separate transitions.

## Interview question

*State vs Strategy? How persist state in DB? Enum + switch vs State classes?*

**SOLID/LLD:** OCP for new states; classic LLD order lifecycle.

### Related

[strategy.md](./strategy.md) · [template-method.md](./template-method.md)

# Strategy

## Problem

A family of interchangeable algorithms (shipping cost, fraud score, FX) must vary independently of the code that uses them.

## Forces

- New algorithms without editing callers (OCP)  
- Select algorithm by config/runtime  
- Test each algorithm in isolation  
- Avoid deep `if/else` trees  

## Naive solution

```java
if (method == EXPRESS) cost = weight * 2 + 10;
else if (method == GROUND) cost = weight * 0.5;
else if (method == STORE) cost = 0;
// + tax quirks inline...
```

Every new method edits the service; untestable branches.

## Pattern

Strategy interface + concrete strategies; context holds a strategy and delegates.

## Implementation

```java
public interface ShippingPricing {
    Money quote(Shipment shipment);
}

public final class GroundShipping implements ShippingPricing {
    public Money quote(Shipment s) { return Money.usd(s.weightKg() * 0.5); }
}
public final class ExpressShipping implements ShippingPricing {
    public Money quote(Shipment s) { return Money.usd(s.weightKg() * 2 + 10); }
}

public final class CheckoutPricing {
    private final ShippingPricing shipping;
    public CheckoutPricing(ShippingPricing shipping) { this.shipping = shipping; }
    public Money total(Cart cart, Shipment shipment) {
        return cart.subtotal().plus(shipping.quote(shipment));
    }
}
```

## Trade-offs

| + | − |
|---|---|
| OCP-friendly | Class proliferation |
| Clear tests | Need selection wiring (factory/DI) |
| Runtime swap | Overkill for two stable branches |

## When to use

Multiple algorithms for one responsibility; payment methods; sort/compress policies; tax engines.

## When NOT to use

One algorithm forever; trivial boolean flag.

## Production example

**Logistics:** `ShippingPricing` strategies per carrier service level selected from checkout options.

## Interview question

*Strategy vs State? Strategy vs polymorphism on the domain entity itself?*

**SOLID/LLD:** OCP + DIP; classic LLD payment/shipping.

### Related

[state.md](./state.md) · [factory.md](./factory.md)

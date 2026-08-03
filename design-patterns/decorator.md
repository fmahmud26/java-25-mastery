# Decorator

## Problem

Add responsibilities to an object (logging, metrics, discount stacking, compression) **without** exploding subclasses.

## Forces

- Combine behaviors flexibly  
- Keep core class closed for modification (OCP)  
- Preserve the component interface  
- Avoid combinatorial subclassing  

## Naive solution

```java
class Pricing {}
class PricingWithLogging extends Pricing {}
class PricingWithCoupon extends Pricing {}
class PricingWithLoggingAndCoupon extends Pricing {} // explosion
```

## Pattern

Wrapper implements the same interface, delegates to a wrapped component, adds behavior before/after.

## Implementation

```java
public interface PriceCalculator {
    Money price(Cart cart);
}

public final class BasePriceCalculator implements PriceCalculator {
    public Money price(Cart cart) { return cart.subtotal(); }
}

public final class CouponDecorator implements PriceCalculator {
    private final PriceCalculator inner;
    private final Coupon coupon;
    public Money price(Cart cart) {
        return coupon.apply(inner.price(cart));
    }
}

public final class MetricsDecorator implements PriceCalculator {
    private final PriceCalculator inner;
    public Money price(Cart cart) {
        long t = System.nanoTime();
        try { return inner.price(cart); }
        finally { metrics.timing("price", System.nanoTime() - t); }
    }
}

PriceCalculator calc = new MetricsDecorator(new CouponDecorator(new BasePriceCalculator(), coupon));
```

Java I/O streams are classic decorators (`BufferedInputStream`).

## Trade-offs

| + | − |
|---|---|
| Stack features | Debugging wrapper stacks |
| OCP-friendly | Identity/`equals` pitfalls |
| Runtime composition | Order of decorators matters |

## When to use

Cross-cutting on a clear interface; stream/buffer; pricing rules; Spring `HandlerInterceptor`-like wrapping.

## When NOT to use

Behavior depends on deep internals — need real redesign. Prefer Proxy when the focus is *access control* to the same object.

## Production example

**Checkout pricing:** base → tax → coupon → loyalty multipliers as decorators.

## Interview question

*Decorator vs Proxy vs Inheritance? Does decorator change the interface?*

**SOLID/LLD:** OCP + SRP; LLD pricing pipelines.

### Related

[proxy.md](./proxy.md) · [adapter.md](./adapter.md)

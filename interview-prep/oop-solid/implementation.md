# OOP + SOLID — Implementation

Idiomatic Java 25 shape for interview designs.

```java
public sealed interface Pricing permits FlatPricing, TieredPricing {
    Money price(Order order);
}

public record FlatPricing(Money amount) implements Pricing {
    @Override public Money price(Order order) { return amount; }
}

public final class CheckoutService {
    private final Pricing pricing;
    private final PaymentGateway gateway; // DIP: depend on abstraction

    public CheckoutService(Pricing pricing, PaymentGateway gateway) {
        this.pricing = pricing;
        this.gateway = gateway;
    }

    public Receipt checkout(Order order) {
        return gateway.charge(pricing.price(order), order.id());
    }
}
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Immutable data | `record` |
| Closed variant set | `sealed` + pattern switch |
| Pluggable algorithm | Strategy interface + inject |
| Shared skeleton | Template Method *or* composition of steps |
| Multiple roles | Small interfaces (ISP) — not one mega-API |

## Talk track

1. Nouns → types; verbs → services.  
2. Variation points → interfaces.  
3. Inject dependencies (DIP).  
4. Prefer composition; justify inheritance with Liskov.

Related: [interfaces.md](../../oop/interfaces.md), [records.md](../../oop/records.md), [sealed-classes.md](../../oop/sealed-classes.md).

# OOP + SOLID — Coding

Patterns that show design judgment (not pattern bingo).

| Prompt | Design move |
|--------|-------------|
| Payment methods grow | Strategy / sealed Pricing+Payment |
| Notification channels | Interface + composition (email/SMS/push) |
| Game characters / weapons | Prefer composition of behaviors |
| Shape hierarchy area() | Polymorphic method or sealed + switch |
| Logger / cache LLD | Ports + strategies; inject policies |

```java
// OCP: add TieredPricing without editing CheckoutService
public record TieredPricing(Money base, Money perItem) implements Pricing {
    @Override
    public Money price(Order order) {
        return base.plus(perItem.times(order.itemCount()));
    }
}
```

## Whiteboard checklist

1. Clarify actors & invariants.  
2. Draw types + one sequence for the hot path.  
3. Name the SOLID trade-off you made (“ISP: split Read/Write repos”).  
4. Say how you’d unit-test with a fake gateway.

Practice: [../../low-level-design](../../low-level-design/), [../../design-patterns](../../design-patterns/).

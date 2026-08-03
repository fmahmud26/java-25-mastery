# Unit Testing

Test a **small unit of behavior** in isolation — fast, deterministic.

## Mental Model

```text
Arrange inputs + stubs
Act on unit
Assert observable outcomes
```

```java
class ShippingPricingTest {
    @Test
    void expressAddsSurcharge() {
        ShippingPricing pricing = new ExpressShipping();
        Money quote = pricing.quote(shipment(weightKg(2)));
        assertEquals(Money.usd(14), quote);
    }
}
```

## Practices

| Do | Don’t |
|----|-------|
| Test public behavior | Couple to private methods |
| Cover edge cases | Assert implementation details |
| One logical assert theme | Mega-tests with 20 unrelated checks |
| Deterministic time/ids | Depend on wall clock |

## Service Example

`Order.pay()` state machine unit-tested for illegal transitions — no DB.

### Related

[what-to-unit-test.md](./what-to-unit-test.md) · [assertions.md](./assertions.md) · [parameterized-tests.md](./parameterized-tests.md)

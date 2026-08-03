# Dependency Injection (related)

DI is not a GoF pattern but the usual **replacement for Singleton service locators**.

## Idea

Pass dependencies via constructors (or container) so classes depend on abstractions — DIP — and lifecycles (including “singleton scope”) are owned externally.

```java
public final class CheckoutFacade {
    private final PaymentGateway payments;
    public CheckoutFacade(PaymentGateway payments) {
        this.payments = payments;
    }
}
```

Prefer this over `PaymentGateway.getInstance()` in LLD and production.

### Related

[singleton.md](./singleton.md) · [patterns-and-solid.md](./patterns-and-solid.md)

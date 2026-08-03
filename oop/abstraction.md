# Abstraction

Show essential behavior; hide irrelevant detail. Interfaces, abstract classes, and module boundaries are tools — abstraction is the design goal.

## 1. Mental Model

```text
Checkout  →  PaymentPort.capture(…)
                ├── StripeAdapter
                └── MockPaymentAdapter
```

## 2. Problem It Solves

Callers must not depend on Stripe SDK details, SQL schema, or SMTP to complete checkout.

## 3. Bad Design → Problems → Better Design

**Bad:** `CheckoutService` calls `stripe.charges.create(...)` directly and builds MIME email inline.

**Problems:** Untestable without network; vendor lock-in; can’t add wallet PSP without editing checkout.

**Better:** Ports + adapters (hexagonal).

```java
public interface PaymentPort {
    PaymentResult capture(PaymentCommand cmd);
}

public interface NotificationPort {
    void send(NotificationMessage message);
}
```

## 4. Technical Rules (Java 25)

Abstraction ≠ “add an interface for every class.” Abstract over **volatility** and **test seams**. Sealed types abstract closed variant sets with exhaustiveness.

## 5. Internal Behavior

Interfaces → `invokeinterface`; abstract classes → normal virtual invoke. No runtime magic beyond dispatch and type checking.

## 6. Domain Scenarios

- **Inventory:** `StockPort.reserve` hides Redis vs DB implementation.  
- **Banking:** `FxRatePort` hides Bloomberg vs ECB feed.

## 7. Trade-offs & When Not

Premature abstraction (IOrderServiceFactoryBuilder) increases indirection. Abstract when you have a second implementation, a test need, or a known variation axis.

## 8. Failure Scenario

Interface with 30 methods (ISP violation) — every adapter stubs 25. Split by role: `CapturePort`, `RefundPort`.

## 9. LLD Interview Scenario

Abstract “notify customer” for order events. Push vs email vs SMS — one port or many? Argue churn and fan-out.

## 10. SOLID / Extensibility

Abstraction enables DIP and OCP. Bad abstractions violate ISP and create false coupling.

## 11. Interview Ladder

- Abstraction vs encapsulation?  
- When is an interface unjustified?  
- Ports and adapters in one sentence?

## 12. Principal Engineer Perspective

Abstract **policy and volatility**, not nouns. The wrong interface freezes a bad boundary; the right one lets the system evolve under load and vendor change.

### Related

[interfaces.md](./interfaces.md) · [abstract-classes.md](./abstract-classes.md) · [domain-modeling.md](./domain-modeling.md)

# Composition

Strong **has-a**: the owner creates/owns the part and controls its lifecycle. Primary tool for reuse and extensibility.

## 1. Mental Model

```text
OrderService
  ├── PricingEngine      (composed)
  ├── InventoryPort      (composed / injected)
  └── PaymentPort        (composed / injected)
```

## 2. Problem It Solves

Reuse and vary behavior without subclass trees; keep each piece replaceable and testable.

## 3. Bad Design → Problems → Better Design

**Bad:** `class OrderService extends PricingEngine extends InventoryClient` (inheritance for reuse).

**Problems:** One inheritance slot burned; impossible to mock cleanly; unrelated changes cascade.

**Better:**

```java
public final class CheckoutService {
    private final PricingEngine pricing;
    private final InventoryPort inventory;
    private final PaymentPort payments;

    public CheckoutService(PricingEngine pricing, InventoryPort inventory, PaymentPort payments) {
        this.pricing = pricing;
        this.inventory = inventory;
        this.payments = payments;
    }

    public OrderId checkout(Cart cart) {
        var quote = pricing.quote(cart);
        inventory.reserve(cart.lines());
        return payments.capture(quote).orderId();
    }
}
```

## 4. Technical Rules (Java 25)

Composition is a design relationship, not a keyword. Implemented as private final fields + delegation. Prefer depending on interfaces for the parts.

## 5. Internal Behavior

Extra objects and calls (delegation). JIT often inlines monomorphic delegates. Cost is design clarity, not usually CPU.

## 6. Domain Scenarios

- **Payments:** `PaymentProcessor` composes `FraudChecker`, `Ledger`, `PspClient`.  
- **Logistics:** `RoutingService` composes `DistanceMatrix`, `VehicleCapacity`.

## 7. Trade-offs & When Not

More wiring (DI). Over-fragmentation yields “class explosion.” Compose at meaningful boundaries, not every two lines.

## 8. Failure Scenario

Owner shares a mutable composed cache without sync → cross-request bleed. Fix: immutable parts or request-scoped collaborators.

## 9. LLD Interview Scenario

Design notification fan-out: email, push, SMS. Inheritance tree vs composed `List<Notifier>`? Argue testability and adding Slack.

## 10. SOLID / Extensibility

Composition is how you practice DIP and OCP daily. Swap `PaymentPort` for a fake in tests; add decorators for metrics without touching core.

## 11. Interview Ladder

- Composition vs inheritance?  
- Composition vs aggregation?  
- How does composition improve testability?

## 12. Principal Engineer Perspective

**Prefer composition by default.** Inheritance must earn its place with LSP-safe *is-a*. Ownership: if you `new` it and nobody else shares it, you compose it; if lifecycle is external, you associate/aggregate.

### Related

[aggregation.md](./aggregation.md) · [association.md](./association.md) · [inheritance.md](./inheritance.md) · [domain-modeling.md](./domain-modeling.md)

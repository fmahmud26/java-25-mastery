# Inheritance

**Inheritance** (`extends`) creates an *is-a* subtype with a single superclass. Use sparingly; prefer composition for reuse.

## 1. Mental Model

```text
is-a?  CreditCardPayment  is-a  PaymentMethod?  maybe
has-a? Order  has-a  PricingPolicy?  usually better as composition
```

## 2. Problem It Solves

Share a polymorphic type and specialize behavior when there is a true subtype relationship and a stable base contract.

## 3. Bad Design → Problems → Better Design

**Bad:** `class EmailNotification extends SmsNotification` to reuse “send” helpers; or deep `BaseEntity → … → Order`.

**Problems:** Fragile base class; wrong *is-a*; can’t reuse across unrelated channels; diamond of confusion via deep trees.

**Better:** `NotificationChannel` interface + composed `RetryingSender`; sealed hierarchy only when variants are closed and conceptual subtypes.

```java
public abstract class PaymentMethod {
    private final String customerId;
    protected PaymentMethod(String customerId) {
        this.customerId = Objects.requireNonNull(customerId);
    }
    public final String customerId() { return customerId; }
    public abstract CaptureResult capture(Money amount);
}
```

Prefer ending inheritance with `final` subclasses or **sealed** parents.

## 4. Technical Rules (Java 25)

| Rule | Detail |
|------|--------|
| Single class inheritance | Multiple via interfaces |
| Ctor chaining | Subclass → `super(...)` |
| `final` class/method | Block extension/override |
| Fields | Hidden, not overridden |
| `@Override` | Use always when intending override |

## 5. Internal Behavior

Subclass instance contains superclass state. Virtual methods dispatch on runtime type. Changing protected members in a library base breaks subclasses (fragile base).

## 6. Domain Scenarios

- **Banking:** avoid `SavingsAccount extends CheckingAccount`. Prefer `Account` + `AccountRules` strategy.  
- **Inventory:** `SerializedItem` vs `BulkItem` as sealed subtypes of `StockItem` if closed set.

## 7. Trade-offs & When Not

Don’t inherit for code reuse alone. Don’t inherit from types you don’t control without isolating adapters. Prefer composition for cross-cutting (logging, retry, metrics).

## 8. Failure Scenario

Base `validate()` gains a new required step; subclass override skips `super.validate()` → invalid payments. Fix: template method `final process()` calling abstract hooks; or composition.

## 9. LLD Interview Scenario

“Should `PriorityShipment extends Shipment`?” Probe: is priority a subtype or an attribute/policy? Design both ways; compare change impact when adding “economy air.”

## 10. SOLID / Extensibility

LSP: subtypes must honor base contracts. OCP: inheritance is one tool; strategy/composition often open for extension with less coupling. ISP: fat base classes force useless overrides.

## 11. Interview Ladder

- Why single inheritance of classes?  
- Fragile base class problem?  
- When is inheritance justified?

## 12. Principal Engineer Perspective

Inheritance is a **public coupling contract**. Default to composition; use inheritance for true subtypes and sealed domain ADTs. Ban deep framework inheritance in app code without review.

### Related

[composition.md](./composition.md) · [polymorphism.md](./polymorphism.md) · [sealed-classes.md](./sealed-classes.md) · [method-overriding.md](./method-overriding.md)

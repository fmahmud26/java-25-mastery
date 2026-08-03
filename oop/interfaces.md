# Interfaces

A **contract** types can implement — multiple inheritance of type, no ordinary instance state.

## 1. Mental Model

```text
implements PaymentPort, RefundPort
     → must provide capture/refund
     → may use default methods for shared policy
```

## 2. Problem It Solves

Define capabilities (`Payable`, `Auditable`, `NotificationPort`) so callers depend on roles, not concrete classes.

## 3. Bad Design → Problems → Better Design

**Bad:** One `IService` with every method; or interface that mirrors a single concrete class 1:1 with no second use.

**Problems:** ISP violations; noise; still tightly coupled.

**Better:** Small role interfaces; default methods only for true shared behavior.

```java
public interface PaymentPort {
    CaptureResult capture(CaptureCommand cmd);

    default boolean supportsCurrency(String currency) {
        return true; // override in adapters that are limited
    }
}

public final class StripePaymentAdapter implements PaymentPort {
    @Override
    public CaptureResult capture(CaptureCommand cmd) { /* SDK call */ 
        return CaptureResult.ok(cmd.paymentId());
    }
}
```

## 4. Technical Rules (Java 25)

| Member | Allowed |
|--------|---------|
| Abstract methods | Yes |
| `default` / `static` / `private` methods | Yes |
| Constants | `public static final` |
| Instance fields | **No** |
| Multiple implements | Yes |
| Sealed interfaces | Yes |

## 5. Internal Behavior

`invokeinterface`; itables. Default methods are injected into implementors’ dispatch — evolving defaults can conflict (diamond of default methods).

## 6. Domain Scenarios

- **Order:** `PricingPort`, `InventoryPort`.  
- **Notification:** `Notifier` with `supports(Channel)`.

## 7. Trade-offs & When Not

Don’t interface-wrap stable internal helpers. Prefer abstract class when many implementors share fields/ctor template — or compose a shared helper.

## 8. Failure Scenario

Two interfaces with conflicting `default void close()` — implementor must override. Fix: redesign; avoid fat defaults.

## 9. LLD Interview Scenario

Design payment capture + refund. One interface or two? What if wallet supports capture but not partial refund?

## 10. SOLID / Extensibility

Interfaces are ISP/DIP instruments. Keep them stable; evolve with defaults carefully; prefer new interfaces over breaking old ones.

## 11. Interview Ladder

- Interface vs abstract class?  
- Default method evolution risks?  
- Why no instance fields?

## 12. Principal Engineer Perspective

Start API design with **interfaces at boundaries**. Inside a module, concrete types are fine. Measure interface success by how easily you swap adapters and tests.

### Related

[abstract-classes.md](./abstract-classes.md) · [abstraction.md](./abstraction.md) · [polymorphism.md](./polymorphism.md) · [interview.md](./interview.md)

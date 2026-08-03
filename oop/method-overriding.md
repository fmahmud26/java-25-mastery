# Method Overriding

Replace a superclass/interface method with a subtype implementation — the core of **runtime polymorphism**.

## 1. Mental Model

```text
static type:  PaymentPort
runtime type: StripePaymentAdapter
call capture → StripePaymentAdapter.capture
```

## 2. Problem It Solves

Specialize behavior while callers program to the base contract.

## 3. Bad Design → Problems → Better Design

**Bad:** Override changes semantics (base says “never throw”; override throws unchecked for normal cases); or forget `@Override` and accidentally overload.

**Problems:** LSP breaks; silent wrong method.

**Better:** Honor contract; use `@Override`; prefer interface implementation over deep class overrides.

```java
public class InventoryPort {
    public Reservation reserve(Sku sku, int qty) {
        throw new UnsupportedOperationException();
    }
}

public final class DbInventoryAdapter extends InventoryPort {
    @Override
    public Reservation reserve(Sku sku, int qty) {
        // transactional reserve
        return Reservation.confirmed(sku, qty);
    }
}
```

(Prefer `InventoryPort` as interface in greenfield code.)

## 4. Technical Rules (Java 25)

| Rule | Detail |
|------|--------|
| Same name + params | Return same or covariant |
| Accessibility | Cannot narrow |
| Exceptions | Cannot add broader checked |
| `@Override` | Recommended always |
| `final` / `static` / `private` | Not overridable (static hides) |

## 5. Internal Behavior

Virtual dispatch via vtable/itable. Bridge methods may appear for generics/covariant returns (`javap`).

## 6. Domain Scenarios

- **Payments:** each PSP overrides/implements `capture`/`refund`.  
- **Banking:** interest calculators override `accrue(Account)`.

## 7. Trade-offs & When Not

If override only calls `super` + logging, use a decorator (composition) instead of subclassing.

## 8. Failure Scenario

Subclass method `reserve(Sku sku, Integer qty)` — doesn’t override `reserve(Sku, int)`; dead code path. `@Override` would have failed compile.

## 9. LLD Interview Scenario

Base `Shipper.ship` promises idempotency. Can a subclass skip idempotency keys? (No — LSP.) How enforce?

## 10. SOLID / Extensibility

LSP is the overriding law. Document pre/postconditions; test subtypes against base contract tests.

## 11. Interview Ladder

- Overriding vs overloading?  
- Rules for exceptions/access?  
- What does `@Override` buy you?

## 12. Principal Engineer Perspective

Every override is a **contract commitment**. Prefer small interfaces and composition when “override” means “wrap with extra policy.”

### Related

[polymorphism.md](./polymorphism.md) · [method-overloading.md](./method-overloading.md) · [inheritance.md](./inheritance.md) · [covariant-return-types.md](./covariant-return-types.md)

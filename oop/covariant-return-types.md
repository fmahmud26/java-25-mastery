# Covariant Return Types

An override may return a **subtype** of the parent method’s return type — still a valid override.

## 1. Mental Model

```text
Parent factory(): PaymentMethod
Child  factory(): CardPaymentMethod   // subtype return — OK
```

## 2. Problem It Solves

Specialize return types without breaking callers that use the parent type; avoid useless casts in subclass-aware code.

## 3. Bad Design → Problems → Better Design

**Bad:** Force all factories to return `PaymentMethod` and cast at every child call site.

**Better:**

```java
public abstract class PaymentMethodFactory {
    public abstract PaymentMethod create(String customerId);
}

public final class CardMethodFactory extends PaymentMethodFactory {
    @Override
    public CardPaymentMethod create(String customerId) {
        return new CardPaymentMethod(customerId);
    }
}
```

## 4. Technical Rules (Java 25)

- Return type must be subtype (reference types).  
- Primitives must match exactly.  
- Bridge methods generated for binary compatibility (`javap -c`).

## 5. Internal Behavior

Compiler emits bridge invoking the covariant method and casting — dynamic dispatch still lands on the override.

## 6. Domain Scenarios

- **Logistics:** `CarrierClient.connect(): Connection` overridden to `FedExConnection`.  
- **Orders:** builder `build(): Order` vs `build(): PriorityOrder` in a specialized builder (use carefully).

## 7. Trade-offs & When Not

Doesn’t replace generics. Don’t invent deep factory hierarchies just to covariant-return — prefer composition.

## 8. Failure Scenario

Changing parent return to an unrelated type breaks overrides — compile errors; fix hierarchy or stop forcing inheritance.

## 9. LLD Interview Scenario

Fluent builders with covariant `self` types (`S extends Builder<S>`) — when worth the complexity?

## 10. SOLID / Extensibility

Supports LSP when the subtype truly is substitutable. Returning a more specific type must not surprise parent-contract callers.

## 11. Interview Ladder

- What is a covariant return?  
- Why bridge methods?  
- Overloading vs covariant override?

## 12. Principal Engineer Perspective

A small language feature — use when it removes casts at real call sites. Don’t build architecture around it.

### Related

[method-overriding.md](./method-overriding.md) · [inheritance.md](./inheritance.md) · [polymorphism.md](./polymorphism.md)

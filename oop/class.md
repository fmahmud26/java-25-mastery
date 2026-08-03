# Class

A **class** is a named type that defines state shape, construction rules, and behavior — the primary unit of domain modeling in Java.

## 1. Mental Model

```text
Class Order  =  invariant rules + operations
     │
     ├── fields (private): what must stay true
     ├── constructors: only valid birth
     └── methods: the only legal transitions
```

## 2. Problem It Solves

You need a stable vocabulary for “Order,” “PaymentIntent,” “Shipment” so many services don’t invent incompatible shapes and mutation paths.

## 3. Bad Design → Problems → Better Design

**Bad:** God class `OrderService` with 40 public fields and 200 methods (persist, email, tax, fraud).

**Problems:** Untestable; every change risks unrelated features; no single ownership of invariants.

**Better:** `Order` entity (invariants), `PricingPolicy` (composition), `OrderRepository` (port), `NotifyOnPaid` (side effect). Class = one reason to change (SRP).

```java
public final class Order {
    private final String orderId;
    private OrderStatus status;
    private final List<OrderLine> lines;

    public Order(String orderId, List<OrderLine> lines) {
        this.orderId = Objects.requireNonNull(orderId);
        if (lines == null || lines.isEmpty()) throw new IllegalArgumentException("lines");
        this.lines = List.copyOf(lines);
        this.status = OrderStatus.CREATED;
    }

    public void markPaid(Instant at) {
        if (status != OrderStatus.CREATED) throw new IllegalStateException(status.name());
        this.status = OrderStatus.PAID;
    }
}
```

## 4. Technical Rules (Java 25)

| Rule | Detail |
|------|--------|
| One public top-level type per file | File name matches |
| Members | Fields, ctors, methods, nested types, initializers |
| Prefer `final` class | Unless designed for extension |
| Records / sealed | Prefer when data/carrier or closed hierarchy |

## 5. Internal Behavior

Class metadata lives in the Class object; instances allocate on the heap. Methods are not stored per instance — they live with the Class; dispatch uses the receiver’s class.

## 6. Domain Scenarios

- **Banking:** `Account` holds balance invariants; interest is a separate `InterestPolicy`.  
- **Inventory:** `Sku` is identity + attributes; `StockLevel` is mutable inventory state — don’t merge into one mega-class.

## 7. Trade-offs & When Not

Not every noun becomes a class hierarchy. Prefer records for DTOs; prefer functions/services for orchestration without state.

## 8. Failure Scenario

Symptom: negative inventory after concurrent adjusts. Cause: public setters on `qty`. Fix: `reserve(n)` / `release(n)` with checks. Prevent: encapsulate + tests for invariants.

## 9. LLD Interview Scenario

Design `PaymentIntent` for card + wallet. Interviewer probes: where does status live? Who may transition it? What is not the class’s job (HTTP, SQL)?

## 10. SOLID / Extensibility

SRP for class boundaries; OCP via strategy/composition rather than subclassing every variant; DIP: depend on ports, not concrete gateways inside the domain class.

## 11. Interview Ladder

- What can a class contain?  
- When prefer record vs class?  
- How do you keep a domain class testable?

## 12. Principal Engineer Perspective

Classes encode **ownership of invariants**. If a type has no invariant and no behavior, it may be a record or map. If it has both persistence and email and fraud, split until change blast radius is local.

### Related

[object.md](./object.md) · [encapsulation.md](./encapsulation.md) · [domain-modeling.md](./domain-modeling.md)

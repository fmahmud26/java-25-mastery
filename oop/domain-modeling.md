# Domain Modeling

Map business reality into types, invariants, and relationships — the point of OOP in backend systems.

## 1. Mental Model

```text
Ubiquitous language → types
Invariants → encapsulated methods
Boundaries → aggregates / ports
Variants → sealed events / strategies
```

## 2. Problem It Solves

Without a model, services become procedural scripts over mutable maps — bugs multiply as payment, order, and inventory rules intertwine.

## 3. Bad Design → Problems → Better Design

**Bad:** One `OrderEntity` with 80 columns, public setters, and status as `String`; business rules in controllers.

**Problems:** Illegal states; duplicated rules; can’t test without Spring; coupling to DB.

**Better (sketch):**

```text
Order (aggregate root)
  ├── OrderId, CustomerId
  ├── lines: List<OrderLine>   (value/entities inside boundary)
  ├── status transitions: pay / cancel / ship
  └── domain events: OrderPaid, OrderCancelled

Ports: PaymentPort, InventoryPort, OrderRepository
```

```java
public final class Order {
    private final OrderId id;
    private OrderStatus status;
    private final List<OrderLine> lines;

    public DomainEvents markPaid(PaymentId paymentId) {
        if (status != OrderStatus.AWAITING_PAYMENT)
            throw new ConflictException("order", id.value());
        status = OrderStatus.PAID;
        return DomainEvents.of(new OrderPaid(id, paymentId, Instant.now()));
    }
}
```

## 4. Technical Rules (Java 25 toolkit)

| Need | Tool |
|------|------|
| Values | `record`, immutable classes |
| Closed facts | `sealed` + switch |
| Capabilities | interfaces (ports) |
| Algorithms with hooks | abstract class sparingly |
| Reuse | composition |
| Identity | entity classes + ids |

## 5. Internal Behavior

No special JVM support — modeling quality is design discipline. Persistence mapping must not dictate illegal states (avoid leaking ORM into the domain API).

## 6. Domain Scenarios

| Domain | Modeling focus |
|--------|----------------|
| Payment | Intent lifecycle; idempotency keys; money as value |
| Order | Aggregate boundaries; price snapshots |
| Notification | Channel strategies; templates as values |
| Banking | Ledger entries immutable; accounts encapsulate balance |
| Inventory | Reservation vs on-hand; SKU identity |
| Logistics | Shipment status sealed; address value objects |

## 7. Trade-offs & When Not

Don’t over-DDD a 200-line CRUD service. Scale modeling depth with complexity and risk (money/compliance → deeper).

## 8. Failure Scenario

Inventory reserved but payment fails — no compensation. Model needs explicit `Reservation` lifecycle + timeout job, not only happy-path methods.

## 9. LLD Interview Scenario

**Prompt:** Design checkout for an online store (cart → pay → ship).  

**Probe:** Where do invariants live? Composition vs inheritance for shipping methods? How test without Stripe? How evolve `PaymentEvent`?

**Strong answer:** Aggregate `Order`, ports for pay/inventory/notify, sealed events, immutable `Money`, composition for policies.

## 10. SOLID / Extensibility

- **S:** Order doesn’t send email.  
- **O:** new notifier via new adapter.  
- **L:** payment adapters honor capture contract.  
- **I:** split read vs write ports if needed.  
- **D:** domain depends on `PaymentPort`, not Stripe SDK.

## 11. Interview Ladder

- Entity vs value object?  
- Aggregate boundary?  
- When sealed events vs open polymorphism?

## 12. Principal Engineer Perspective

Domain modeling is **risk management**: make illegal states unrepresentable, keep coupling at ports, and choose closed vs open extension deliberately. Ownership: who may mutate an aggregate, in which transaction, under which idempotency key?

### Related

[encapsulation.md](./encapsulation.md) · [composition.md](./composition.md) · [sealed-classes.md](./sealed-classes.md) · [records.md](./records.md) · [interview.md](./interview.md)

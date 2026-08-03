# Association

**Association** is a general “knows about / uses” relationship — navigability without implying ownership.

## 1. Mental Model

```text
PaymentService ──── uses ────> FraudClient
Order ──── references ────> CustomerId
```

## 2. Problem It Solves

Connect collaborators and domain concepts without claiming exclusive ownership (that’s composition) or whole-part (aggregation).

## 3. Bad Design → Problems → Better Design

**Bad:** Bidirectional object links everywhere (`order.getCustomer().getOrders().get(0).getCustomer()…`).

**Problems:** Graph spaghetti; serialization cycles; can’t load partial data; testing nightmare.

**Better:** One-way associations; IDs at boundaries; explicit domain services for use-cases that need both sides.

```java
public final class RefundService {
    private final PaymentPort payments; // association to port
    private final LedgerPort ledger;

    public void refund(String paymentId, Money amount) {
        payments.refund(paymentId, amount);
        ledger.post(RefundEntry.of(paymentId, amount));
    }
}
```

## 4. Technical Rules (Java 25)

Association = field, parameter, or temporary local. Multiplicity: 1, 0..1, 1..*, * — use `Optional`, collections, or none. Direction matters more than UML labels.

## 5. Internal Behavior

References keep objects reachable. Circular associations can leak memory if caches retain both directions carelessly (usually app logic bug, not GC myth).

## 6. Domain Scenarios

- **Notifications:** `OrderPaidHandler` associated with `Notifier` port.  
- **Logistics:** `Shipment` associated with `CarrierCode` (not a live carrier object).

## 7. Trade-offs & When Not

Bidirectional associations are rarely worth it in backend models. Prefer search/query on the other side.

## 8. Failure Scenario

JSON infinite recursion from bidirectional JPA associations. Fix: DTOs; `@JsonIgnore` is a symptom fix — redesign API models.

## 9. LLD Interview Scenario

Link `Invoice` and `Payment`. Uni-directional from payment to invoiceId? Who owns the relationship for “list payments for invoice”?

## 10. SOLID / Extensibility

Depend on abstractions (ports). Association to concrete SDKs inside domain entities is coupling debt.

## 11. Interview Ladder

- Association vs aggregation vs composition?  
- Why prefer CustomerId over Customer object on Order?  
- Navigability — why one way?

## 12. Principal Engineer Perspective

Associations are **coupling edges**. Draw fewer of them. Across services, association becomes a foreign id + an API call — design for failure and latency.

### Related

[aggregation.md](./aggregation.md) · [composition.md](./composition.md) · [domain-modeling.md](./domain-modeling.md)

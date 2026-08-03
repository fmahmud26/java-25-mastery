# Aggregation

**Aggregation** is a weaker **has-a**: the whole uses parts that can outlive it or be shared. Lifecycle is independent; ownership is conceptual, not exclusive.

## 1. Mental Model

```text
Department  ◇──  Employee
(delete dept; employees may still exist / reassign)
```

UML hollow diamond (informal in code — same field syntax as composition; **intent** differs).

## 2. Problem It Solves

Model relationships where parts are shared or independently managed (catalog products on many orders; drivers in a fleet).

## 3. Bad Design → Problems → Better Design

**Bad:** `Order` deeply copies and “owns” the entire `Product` catalog entity including mutable stock.

**Problems:** Stale product data; duplicate sources of truth; inventory updates don’t propagate.

**Better:** `Order` holds `ProductId` + snapshot price (value); inventory service owns stock. Aggregation to catalog identity, not ownership of stock.

```java
public final class OrderLine {
    private final String sku;           // identity reference (aggregation-like)
    private final String description; // snapshot
    private final Money unitPrice;    // snapshot at order time
    private final int qty;
}
```

## 4. Technical Rules (Java 25)

No language keyword. Expressed as references to independently managed objects/ids. Prefer IDs + repositories across aggregates (DDD) over holding live foreign graphs.

## 5. Internal Behavior

Same as any reference field. GC doesn’t know “aggregation” — if you retain references, parts stay alive. Don’t confuse UML with memory ownership.

## 6. Domain Scenarios

- **Inventory:** `Warehouse` aggregates `Bin` locations that are reassigned.  
- **Banking:** `Customer` aggregated into multiple `Account` relationships via ids.

## 7. Trade-offs & When Not

Holding live references to other aggregates invites consistency bugs. Prefer IDs and eventual consistency between aggregates.

## 8. Failure Scenario

Order stores live `Product` reference; price changes mid-checkout. Fix: snapshot money/description at line creation.

## 9. LLD Interview Scenario

Course enrollment: `Course` and `Student`. Aggregation or composition? What deletes when a course is cancelled?

## 10. SOLID / Extensibility

Clear aggregate boundaries reduce coupling. Don’t navigate endless object graphs from a web request — use queries/ports.

## 11. Interview Ladder

- Aggregation vs composition?  
- How express aggregation in Java?  
- Why snapshot prices on order lines?

## 12. Principal Engineer Perspective

Treat aggregation as **shared lifecycle / shared identity**, not a diamond in a diagram. In services, prefer **IDs over live object graphs** across consistency boundaries.

### Related

[composition.md](./composition.md) · [association.md](./association.md) · [domain-modeling.md](./domain-modeling.md)

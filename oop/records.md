# Records

Immutable **data carriers** with generated canonical ctor, accessors, `equals`, `hashCode`, `toString` — idiomatic for DTOs and value objects on **Java 25**.

## 1. Mental Model

```text
record Money(long cents, String currency) { }
→ private final fields + accessors cents()/currency()
→ value equality
```

## 2. Problem It Solves

Kill boilerplate POJOs that only hold data and drift out of sync on `equals`/`hashCode`.

## 3. Bad Design → Problems → Better Design

**Bad:** Mutable JavaBean DTO for an event payload with setters used mid-pipeline.

**Problems:** Accidental mutation in async handlers; broken equality.

**Better:**

```java
public record PaymentCaptured(
        String paymentId,
        long amountCents,
        String currency,
        Instant at
) {
    public PaymentCaptured {
        Objects.requireNonNull(paymentId);
        Objects.requireNonNull(currency);
        Objects.requireNonNull(at);
        if (amountCents <= 0) throw new IllegalArgumentException("amount");
    }
}
```

## 4. Technical Rules (Java 25)

| Trait | Detail |
|-------|--------|
| Implicitly `final` | No subclassing |
| May `implements` | Interfaces / sealed |
| Compact ctor | Validate/normalize |
| No extra instance fields | Static OK |
| Accessors | `name()` not `getName()` |

## 5. Internal Behavior

Compiler generates members; records can participate in pattern matching/`switch`. Shallow immutability only — copy mutable component inputs.

## 6. Domain Scenarios

- **Orders:** `OrderLineView` API records.  
- **Logistics:** `GeoPoint(lat, lon)` value.  
- **Banking:** `Iban` record with validation.

## 7. Trade-offs & When Not

Not for mutable JPA entities, types needing identity lifecycle, or class inheritance. Use a class when behavior+identity dominate data.

## 8. Failure Scenario

Record holds `List<String> tags` from a mutable ArrayList without `List.copyOf` — caller mutates after send. Fix: copy in compact ctor.

## 9. LLD Interview Scenario

Model `Quote` as record vs class. When does quoting become an entity with status transitions?

## 10. SOLID / Extensibility

Records + sealed interfaces excel at closed domain messages (OCP via new permitted types carefully versioned).

## 11. Interview Ladder

- What does the compiler generate?  
- Records vs classes?  
- Deep vs shallow immutability?

## 12. Principal Engineer Perspective

Prefer records for **values and messages**. Keep entities as classes with encapsulated transitions. Validate in compact ctors so bad data never enters the bus.

### Related

[immutability.md](./immutability.md) · [sealed-classes.md](./sealed-classes.md) · [encapsulation.md](./encapsulation.md)

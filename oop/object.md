# Object

An **object** is a runtime instance: identity + state + behavior. Variables hold **references** (or `null`), not the object bytes.

## 1. Mental Model

```text
Order a ──► [Order#1 heap]
Order b ──┘   alias — same identity
Order c ──► [Order#2]   equal by value? only if equals says so
```

## 2. Problem It Solves

You need multiple live entities (many orders, many shipments) sharing one blueprint but distinct identity and lifecycle.

## 3. Bad Design → Problems → Better Design

**Bad:** Treat two `Customer` instances with the same id as interchangeable via `==`, or put customers in `HashSet` without `equals`/`hashCode`.

**Problems:** Duplicate “same” customers; cache misses; Set lies.

**Better:** Document identity: entities = id equality; value objects = structural equality (records).

```java
public final class CustomerId {
    private final String value;
    public CustomerId(String value) { this.value = Objects.requireNonNull(value); }
    @Override public boolean equals(Object o) {
        return o instanceof CustomerId other && value.equals(other.value);
    }
    @Override public int hashCode() { return value.hashCode(); }
}
```

## 4. Technical Rules (Java 25)

| Concept | Meaning |
|---------|---------|
| Identity | `==` / `System.identityHashCode` |
| Equality | `equals` + `hashCode` contract |
| `null` | No target — NPE on deref |
| `Object` | Ultimate superclass |

## 5. Internal Behavior

`new` allocates + runs constructors. Escape analysis may scalar-replace non-escaping objects. GC reclaims unreachable instances. Aliasing is how shared mutable state becomes a concurrency bug.

## 6. Domain Scenarios

- **Logistics:** Two `Shipment` objects with same tracking number in different JVMs — equality policy must be explicit for dedupe.  
- **Payments:** Don’t intern payment objects; identity and audit trail matter.

## 7. Trade-offs & When Not

Don’t override `equals` for JPA entities carelessly (proxies, mutable ids). Prefer id-based equality with a clear rule, or don’t put entities in hash structures across sessions.

## 8. Failure Scenario

Symptom: “duplicate charge” after retry — two objects, same paymentId, treated as distinct in an idempotency set because `equals` missing. Fix: key by paymentId string/record.

## 9. LLD Interview Scenario

Model cart line items: when is a line an entity vs a value? What happens when the same SKU is added twice — merge or two objects?

## 10. SOLID / Extensibility

Objects should not become service locators. Keep collaborators injected; keep identity rules stable as APIs evolve.

## 11. Interview Ladder

- Identity vs equality?  
- Why override `hashCode` with `equals`?  
- Dangers of aliasing mutable objects across threads?

## 12. Principal Engineer Perspective

Decide **entity vs value** early. Publish equality rules. Treat shared mutable objects as concurrency hazards — prefer immutable messages across boundaries.

### Related

[class.md](./class.md) · [immutability.md](./immutability.md) · [records.md](./records.md)

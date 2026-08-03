# Immutability

After construction, observable state does not change. Safer sharing, simpler reasoning, better defaults for messages and value objects.

## 1. Mental Model

```text
Money m = new Money(500, "USD");
// no setters — any “change” returns a new Money
Money taxed = m.plus(tax);
```

## 2. Problem It Solves

Mutable shared objects cause races, cache corruption, and “who changed this?” debugging across payment/order pipelines.

## 3. Bad Design → Problems → Better Design

**Bad:** Mutable `Money` with `setCents`; shared across threads in a pricing cache.

**Problems:** Torn reads; one request poisons another’s price.

**Better:** Immutable `Money` / records; updates allocate new values; entities that must mutate keep mutation **inside** one aggregate.

```java
public record Money(long cents, String currency) {
    public Money {
        Objects.requireNonNull(currency);
    }
    public Money plus(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("fx");
        return new Money(cents + other.cents, currency);
    }
}
```

## 4. Technical Rules (Java 25)

- `final` fields + no mutators + defensive copies of mutable inputs (`List.copyOf`).  
- Records are shallow-immutable (components final; nested mutables still mutate).  
- Prefer immutable collections at APIs.

## 5. Internal Behavior

Immutable objects are safely publishable after ctor (JMM-friendly with final fields). Allocation cost vs lock cost — usually immutability wins for small values.

## 6. Domain Scenarios

- **Notification:** immutable `EmailPayload` queued to workers.  
- **Logistics:** immutable `Address` value object reused on many shipments.

## 7. Trade-offs & When Not

Hot tight loops updating huge mutable buffers may need careful mutability. Domain entities with long lifecycles often mutate — still keep value objects immutable.

## 8. Failure Scenario

Symptom: “immutable” `Order` exposes `List<Line> lines()` live reference; caller clears list. Fix: `List.copyOf` on the way out.

## 9. LLD Interview Scenario

Cart pricing: immutable quote snapshot vs live mutable cart. When do you freeze a `PriceQuote` for checkout?

## 10. SOLID / Extensibility

Immutability reduces temporal coupling. New behaviors become functions on values (`plus`, `convert`) instead of new setters.

## 11. Interview Ladder

- `final` vs deep immutability?  
- Are records always deeply immutable?  
- Safe publication?

## 12. Principal Engineer Perspective

Default to **immutable at boundaries** (events, DTOs, money, ids). Allow controlled mutation inside aggregates with clear transaction boundaries.

### Related

[encapsulation.md](./encapsulation.md) · [records.md](./records.md) · [object.md](./object.md)

# Immutability (Java-specific)

## Problem

Shared mutable state causes races, defensive copies, and unpredictable APIs.

## Why naive approach fails

Setters everywhere; “maybe thread-safe” classes; bugs under concurrency.

## Pattern / practice

Prefer objects whose state doesn’t change after construction — **records**, `final` fields, unmodifiable collections.

## Implementation

```java
public record Money(long cents, Currency currency) {
    public Money {
        if (cents < 0) throw new IllegalArgumentException();
    }
    public Money plus(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException();
        return new Money(cents + other.cents, currency);
    }
}

var items = List.of("a", "b"); // unmodifiable
```

## Trade-offs

| Pros | Cons |
|------|------|
| Safer concurrency | More allocations (mitigate with flyweight/caches) |
| Simpler reasoning | Builders for complex construction |
| Great with records | Mapping to JPA entities needs care |

## Real-world usage

Value objects, DTOs, message payloads, `java.time`, concurrent publications of immutable snapshots.

Related: [builder.md](./builder.md), [java-builder.md](./java-builder.md), [../oop/records.md](../oop/records.md).

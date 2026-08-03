# Immutability

## Why

Mutable shared state causes races, spooky action-at-a-distance, and APIs that lie. Immutable values are easier to reason about and share across threads.

## Before

```java
public class Money {
    public long cents; // callers mutate
    public String currency;
}
cart.getTotal().cents = -1; // corruption
```

## After

```java
public record Money(long cents, String currency) {
    public Money {
        if (cents < 0) throw new IllegalArgumentException("cents");
        Objects.requireNonNull(currency);
    }
    public Money plus(Money o) {
        if (!currency.equals(o.currency)) throw new IllegalArgumentException("fx");
        return new Money(cents + o.cents, currency);
    }
}
```

Collections: `List.copyOf(items)` in constructors; never expose live mutable lists.

## Trade-offs

| Gain | Cost |
|------|------|
| Thread-safe sharing | More allocations (usually fine) |
| Safer APIs | Mapping to mutable JPA entities at edges |
| Simpler mental model | “Update” = new instance |

## PE Decision

Domain money/ids/commands = immutable. Persistence entities may be mutable — **don’t leak them** past the repository boundary.

### Related

[records.md](./records.md) · [defensive-programming.md](./defensive-programming.md) · [api-design.md](./api-design.md)

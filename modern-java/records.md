# Records (Language Evolution View)

**Introduced:** preview 14–15 · **Final:** Java 16 · **Java 25:** core style for values/DTOs

Deep OOP notes: [../oop/records.md](../oop/records.md). Here: **why Java added them** and migration from POJOs.

## Problem Before

DTOs needed fields, ctor, getters, `equals`, `hashCode`, `toString` — or Lombok — easy to drift.

```java
public final class Money {
    private final long cents;
    private final String currency;
    public Money(long cents, String currency) { /* ... */ }
    public long getCents() { return cents; }
    // equals/hashCode/toString...
}
```

## The Feature

`record` — shallow-immutable **nominal tuples** with generated canonical members; compact ctor for validation.

## How It Works

Components become `private final` fields + accessors `name()`. Implicitly `final`. May implement interfaces; cannot extend classes. Pattern-deconstruction in 21+.

## Before → After

```java
public record Money(long cents, String currency) {
    public Money {
        Objects.requireNonNull(currency);
        if (currency.length() != 3) throw new IllegalArgumentException("currency");
    }
    public Money plus(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("fx");
        return new Money(cents + other.cents, currency);
    }
}
```

```java
// Event payload
public record PaymentCaptured(String paymentId, long cents, Instant at) { }
```

## Production Usage

- API DTOs, value objects, map keys, message payloads  
- Compact ctor validation at boundaries  
- With sealed interfaces for closed event families

## Trade-offs

| Pros | Cons |
|------|------|
| Correct equality by default | Shallow immutability (copy mutable components) |
| Tiny readable types | Not a JPA entity replacement |
| Pattern-friendly | Accessor naming differs from JavaBeans (`cents()` vs `getCents`) — JSON config may need tweaks |

## When NOT to Use

- Mutable identity entities with lifecycle  
- Types that must extend a class  
- When framework insists on setters (isolate with mapper)

## Migration Notes

1. Convert immutable DTOs → records.  
2. Fix serializers (`@JsonProperty` / record support).  
3. Don’t convert aggregates with rich transitions blindly — keep classes.

## Interview Questions

- What does the compiler generate?  
- Records vs Lombok `@Value`?  
- Can records be sealed partners?  
- Deep vs shallow immutability?

### Related

[record-patterns.md](./record-patterns.md) · [sealed-classes.md](./sealed-classes.md) · [modern-coding-style.md](./modern-coding-style.md)

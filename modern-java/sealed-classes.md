# Sealed Classes (Language Evolution View)

**Introduced:** preview 15–16 · **Final:** Java 17 · **Java 25:** idiomatic with pattern switch

OOP deep-dive: [../oop/sealed-classes.md](../oop/sealed-classes.md).

## Problem Before

Open interfaces allowed unknown implementors; `switch`/`instanceof` needed `default` that **silently ignored** new variants — production event drops.

```java
// Before: easy to miss a type
if (event instanceof Captured) { ... }
else if (event instanceof Failed) { ... }
// forgot Refunded
```

## The Feature

`sealed` types list allowed subtypes (`permits`). Subtypes are `final`, `sealed`, or `non-sealed`. Enables **exhaustive** switches.

## How It Works

Compiler + classfile enforce permits. Pattern switch without `default` when all permits covered. Models algebraic data types in Java.

## Before → After

```java
// After
public sealed interface PaymentEvent permits Captured, Failed, Refunded { }
public record Captured(String paymentId, long cents) implements PaymentEvent { }
public record Failed(String paymentId, String code) implements PaymentEvent { }
public record Refunded(String paymentId, long cents) implements PaymentEvent { }

String route(PaymentEvent e) {
    return switch (e) {
        case Captured c -> "ledger.capture";
        case Failed f -> "notify.fail";
        case Refunded r -> "ledger.refund";
    };
}
```

## Production Usage

- Domain events/commands inside a service  
- Result types (`Ok`/`Err`)  
- Prefer **open** interfaces for SPI/adapters (`PaymentPort`)

## Trade-offs

| Pros | Cons |
|------|------|
| Exhaustiveness | Harder for third parties to extend |
| Documents variant set | Cross-module permits need care |
| Pairs with records | Over-sealing infrastructure APIs hurts plugins |

## When NOT to Use

- Plugin/adapter surfaces  
- When variants live in other teams’ jars often — use versioned open schemas instead  
- `non-sealed` escape hatches that defeat the purpose

## Migration Notes

Start with internal event types. Replace Visitor only when the variant set is stable. Coordinate schema evolution with consumers.

## Interview Questions

- Sealed vs final class?  
- Why exhaustiveness matters in prod?  
- Sealed domain event vs open `PaymentPort`?  
- Role of `non-sealed`?

### Related

[pattern-matching-for-switch.md](./pattern-matching-for-switch.md) · [records.md](./records.md) · [java-evolution.md](./java-evolution.md)

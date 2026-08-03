# Pattern Matching (Overview)

**Java 16–25:** progressive feature family — type tests that **bind**, then switch/record/unnamed/primitive refinements.

## Problem Before

Type narrowing meant cast duplication and error-prone `instanceof` chains:

```java
if (o instanceof String) {
    String s = (String) o;
    ...
}
```

Visitor pattern was the “type-safe” alternative — heavy for simple domain ADTs.

## The Feature

Patterns match shape/type and introduce bindings. Hub for:

| Feature | Final since | Doc |
|---------|-------------|-----|
| `instanceof` type patterns | 16 | [pattern-matching-for-instanceof.md](./pattern-matching-for-instanceof.md) |
| Pattern `switch` | 21 | [pattern-matching-for-switch.md](./pattern-matching-for-switch.md) |
| Record patterns | 21 | [record-patterns.md](./record-patterns.md) |
| Unnamed `_` | 22 | [unnamed-variables-patterns.md](./unnamed-variables-patterns.md) |
| Enhanced/nested | 21+ | [enhanced-type-patterns.md](./enhanced-type-patterns.md) |
| Primitive patterns | **Preview** on 25 | [primitive-patterns.md](./primitive-patterns.md) |

## How It Works

Match success → bindings in scope under definite assignment rules. Sealed hierarchies unlock exhaustive switches. Guards: `when`.

## Before → After

```java
// Before
if (msg instanceof PaymentCaptured) {
    PaymentCaptured c = (PaymentCaptured) msg;
    ledger.post(c.getPaymentId(), c.getCents());
}

// After
if (msg instanceof PaymentCaptured(String id, long cents, Instant _)) {
    ledger.post(id, cents);
}
```

## Production Usage

- Event routing, deserialization triage, error-type handling  
- Prefer sealed + switch over long `instanceof` else-if for closed sets

## Trade-offs

| Pros | Cons |
|------|------|
| Less casting | Over-nested patterns become cryptic |
| Exhaustiveness | Open types still need `default` |
| Replaces some Visitor | Not multiple dispatch |

## When NOT to Use

- Behavior that should be on the type (`shape.area()`) instead of external switch  
- Preview primitive patterns in prod without policy  
- Replacing polymorphism for adapter/plugin cases

## Migration Notes

1. `instanceof` patterns (low risk).  
2. Records + sealed events.  
3. Pattern switch routers.  
4. Record patterns in those routers.

## Interview Questions

- Pattern matching vs Visitor?  
- What is exhaustiveness?  
- Which pattern features are preview on 25?

### Related

[switch-expressions.md](./switch-expressions.md) · [sealed-classes.md](./sealed-classes.md) · [java-evolution.md](./java-evolution.md)

# Pattern Matching for switch

**Final:** Java 21 · **Java 25:** standard (core modern control flow)

## Problem Before

```java
String route(Object event) {
    if (event instanceof Captured) {
        return "ledger";
    } else if (event instanceof Failed) {
        return "notify";
    } else {
        return "ignore"; // swallows unknowns
    }
}
```

## The Feature

`case` labels as patterns (types, records, null, guards with `when`). Dominance checking; exhaustiveness with sealed types.

## How It Works

First matching case wins. More specific patterns before general. `case null` optional — type patterns don’t match null. Combine with switch expressions.

## Before → After

```java
String route(PaymentEvent event) {
    return switch (event) {
        case Captured(String id, long cents, Instant _) -> "ledger:" + id;
        case Failed(String id, String code) when code.startsWith("3") -> "retry";
        case Failed(String id, String code) -> "notify:" + code;
        case Refunded(String id, long cents) -> "ledger.refund:" + id;
    };
}
```

## Production Usage

- Event/command routers inside a service  
- Deserialized `Object` triage at boundaries (still validate schema)  
- Error hierarchies

## Trade-offs

| Pros | Cons |
|------|------|
| Exhaustive, readable | Mega-switches accumulate responsibilities |
| Guards express policy | Guard order bugs if nonspecific first |

## When NOT to Use

- Adapter selection better via DI/map of ports  
- Preview-only primitive patterns without policy  
- Cross-cutting concerns (metrics) — decorate instead of bloating cases

## Migration Notes

Introduce sealed events, then convert routers. Keep `default` only for truly open types and **metric/alert** on hit.

## Interview Questions

- Dominance vs exhaustiveness?  
- Does `case String s` match null?  
- When keep `default` on sealed switch?

### Related

[switch-expressions.md](./switch-expressions.md) · [record-patterns.md](./record-patterns.md) · [sealed-classes.md](./sealed-classes.md)

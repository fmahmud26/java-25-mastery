# Record Patterns

**Final:** Java 21 · **Java 25:** standard

## Problem Before

Even with records, code unpacked components manually:

```java
if (o instanceof Point p) {
    int x = p.x();
    int y = p.y();
    ...
}
```

Nested structures needed nested casts.

## The Feature

Deconstruct records in `instanceof` / `switch`: `case Point(int x, int y)`. Nested patterns allowed. `var` / `_` in components.

## How It Works

Match succeeds if type matches and nested patterns match. Generics: `Box(String s)` matches `Box<String>` payloads. Unnamed `_` skips components.

## Before → After

```java
public record OrderLine(String sku, int qty, Money unitPrice) { }
public record Money(long cents, String currency) { }

// After
if (line instanceof OrderLine(String sku, int qty, Money(long cents, String ccy))
        && qty > 0) {
    inventory.reserve(sku, qty);
    ledger.hold(cents, ccy);
}

return switch (event) {
    case Captured(var id, var cents, _) -> post(id, cents);
    default -> ignore();
};
```

## Production Usage

- Event handlers unpacking payloads  
- Nested value objects (money, geo points)  
- Prefer shallow deconstruction for readability

## Trade-offs

Deep nesting reads like Prolog — extract methods when >2 levels.

## When NOT to Use

- Types that aren’t records (use type patterns only)  
- When you need the whole record object afterward — bind the record, not only parts

## Migration Notes

Requires records first. Adopt in switch routers after sealed events exist.

## Interview Questions

- Record pattern vs accessor calls?  
- Nested record patterns — costs/readability?  
- How does `_` help?

### Related

[records.md](./records.md) · [unnamed-variables-patterns.md](./unnamed-variables-patterns.md) · [pattern-matching-for-switch.md](./pattern-matching-for-switch.md)

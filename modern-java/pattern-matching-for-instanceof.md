# Pattern Matching for instanceof

**Final:** Java 16 · **Java 25:** standard

## Problem Before

```java
if (value instanceof String) {
    String s = (String) value; // duplicate, cast can drift from check
    process(s);
}
```

## The Feature

Type pattern: `instanceof Type name` binds `name` when the test succeeds.

## How It Works

Binding scoped where the compiler proves the match. Works with `&&` guards; not on the failing side of `||`. Flow scoping after early `return` when negated.

## Before → After

```java
// After
if (value instanceof String s && !s.isBlank()) {
    process(s);
}

if (!(value instanceof RefundRequest req)) {
    return;
}
gateway.refund(req);
```

## Production Usage

- Narrowing `Object`/`Exception` payloads  
- Step toward full pattern switch migration

## Trade-offs

Smaller than switch for 2–3 types; becomes noisy beyond that — prefer sealed switch.

## When NOT to Use

- Closed variant sets with many types → pattern switch  
- When polymorphism belongs on the type

## Migration Notes

Mechanical replace of `instanceof` + cast. Safe, high ROI.

## Interview Questions

- Scope rules for pattern variables?  
- Why `instanceof T t || t.foo()` fails?  
- Relation to pattern switch?

### Related

[pattern-matching.md](./pattern-matching.md) · [pattern-matching-for-switch.md](./pattern-matching-for-switch.md)

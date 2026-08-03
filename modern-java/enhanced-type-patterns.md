# Enhanced / Nested Type Patterns

**Status:** Final patterns ecosystem on Java 21+ (nested type & record patterns in switch/`instanceof`)

## Problem Before

Matching only top-level types still needed manual unwrapping of wrappers/`Box`/`Either`-like structures.

## The Feature

Patterns nest: `Box(String s)`, guards, `var` in patterns, dominance ordering across type hierarchies.

## How It Works

Nested match = outer type + inner pattern. Dominance: more specific cases first. `when` for boolean guards.

## Before → After

```java
record Box<T>(T value) { }

// Before
if (o instanceof Box<?> b && b.value() instanceof String) {
    String s = (String) b.value();
}

// After
if (o instanceof Box(String s)) {
    use(s);
}

return switch (o) {
    case Box(String s) when s.startsWith("PAY-") -> s;
    case Box(String s) -> "str";
    case Box(Integer i) -> "int";
    case Box(var v) -> "other";
    default -> "no";
};
```

## Production Usage

- Nested DTOs/events  
- Prefer records so nested patterns stay clean

## Trade-offs

Powerful but dense — readability is the budget.

## When NOT to Use

- Deep nests (>2–3) → intermediate locals/methods  
- When a proper domain method hides the structure better

## Migration Notes

Adopt after record patterns. Add team rule: max nesting depth.

## Interview Questions

- What is pattern dominance?  
- `Box(String s)` vs `Box(var v)`?  
- When extract a method instead of nesting?

### Related

[record-patterns.md](./record-patterns.md) · [pattern-matching-for-switch.md](./pattern-matching-for-switch.md)

# var

**Introduced:** Java 10 · **Java 25:** final, idiomatic for locals

## Problem Before

Nested generics and obvious constructors forced noisy duplication:

```java
Map<String, List<Payment>> paymentsByCustomer =
    new HashMap<String, List<Payment>>();
```

## The Feature

`var` — **local-variable type inference**. Still statically typed; the compiler infers from the initializer.

## How It Works

Inference uses the initializer’s standalone type. No diamond-only without target in some cases — `var list = new ArrayList<>()` needs context (Java refined this; prefer `var list = new ArrayList<String>()` for clarity). Illegal: fields, most parameters, no initializer, `var x = null`.

## Before → After

```java
// Before
ResponseEntity<OrderResponse> response = restTemplate.exchange(...);

// After — when RHS makes type obvious
var response = restTemplate.exchange(...);

// Keep explicit when type is the documentation
List<OrderLine> lines = order.lines();
```

```java
try (var in = Files.newInputStream(path)) {
    // ...
}
for (var line : lines) { }
```

## Production Usage

- `var` next to `new`, factories, obvious stream terminals  
- Team style guide: “obvious RHS only”  
- IDEs show inferred type on hover — don’t rely on that for public reviews of subtle APIs

## Trade-offs

| Pros | Cons |
|------|------|
| Less visual noise | Hides type when RHS is opaque (`service.process(x)`) |
| Encourages better naming | Overuse reduces scanability in reviews |

## When NOT to Use

- Public API signatures (methods/fields — not allowed for fields anyway)  
- When the left-hand type carries domain meaning the RHS obscures  
- Parallel assignments where types differ subtly (`Number` vs `Integer`)

## Migration Notes

Enable in new code; don’t mass-convert a codebase in one PR. Pair with good variable names (`paymentPort` not `p`).

## Interview Questions

- Is `var` dynamic typing?  
- Where is `var` illegal?  
- When would you forbid `var` in a style guide?

### Related

[unnamed-variables-patterns.md](./unnamed-variables-patterns.md) · [modern-coding-style.md](./modern-coding-style.md)

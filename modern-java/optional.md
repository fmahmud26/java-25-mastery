# Optional

**Introduced:** Java 8 · **Java 25:** same API + later helpers (`orElseThrow()`, `isEmpty()`, `stream()`, …)

## Problem Before

`null` returns meant undocumented absence — NPEs at distance, unclear APIs (`findUser` returns null?).

## The Feature

`Optional<T>` is a **limited** container for a value that may be absent — intended mainly as a **return type** to force callers to confront missingness.

## How It Works

- `Optional.of(x)` — x non-null  
- `Optional.ofNullable(x)` — null → empty  
- `Optional.empty()`  
- Transform: `map` / `flatMap` / `filter`  
- Exit: `orElse`, `orElseGet`, `orElseThrow`, `ifPresent`, `ifPresentOrElse`

## Before → After

```java
// Before
User u = repo.find(id);
if (u != null) {
    return u.email();
}
return "unknown";

// After
return repo.find(id).map(User::email).orElse("unknown");
```

```java
// Prefer orElseGet for expensive defaults
return stock.find(sku).orElseGet(() -> catalog.fallback(sku));
```

## Production Usage

- Repository `findById` → `Optional<Order>`  
- Avoid `optional.get()` without guard  
- Chain at the edges; don’t pass `Optional` through deep layers if a clear domain type (`OptionalPayment` vs absent) is better

## Trade-offs

| Pros | Cons |
|------|------|
| Documents absence | Allocation (usually tiny / irrelevant) |
| Forces handling | Abuse as field / parameter / collection element |
| Composable maps | `orElse(null)` reintroduces null |

## When NOT to Use

- **Fields** on entities (use nullable with clear model or `Optional` only at API boundary — style debates; prefer empty values / null + `@Nullable` consistently)  
- **Parameters** — overload or clear API instead of `Optional` params  
- Collections of Optional  
- As a substitute for exceptions for real failures (“DB down”)

## Migration Notes

1. Replace null-return finds with `Optional`.  
2. Ban `get()` in code review unless preceded by `isPresent` (prefer `orElseThrow`).  
3. Don’t wrap everything — only uncertain returns.

## Interview Questions

- Why shouldn’t Optional be a field?  
- `orElse` vs `orElseGet`?  
- `map` vs `flatMap`?  
- Optional vs null vs exceptions — decision framework?

### Related

[lambdas.md](./lambdas.md) · [modern-coding-style.md](./modern-coding-style.md) · [java-evolution.md](./java-evolution.md)

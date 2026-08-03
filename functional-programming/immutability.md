# Immutability (FP Discipline)

Prefer values that don’t change after creation — makes lambdas, streams, and concurrency safer.

## Mental Model

```text
mutable shared state + lambdas/parallel = races & surprising captures
immutable inputs → functions stay referentially transparent
```

## Imperative vs Functional

```java
// Mutable accumulation
List<String> ids = new ArrayList<>();
payments.forEach(p -> ids.add(p.id())); // side-effecting build

// Prefer collect/reduce into new structures
List<String> ids = payments.stream().map(Payment::id).toList(); // unmodifiable
```

```java
Money total = lines.stream()
        .map(OrderLine::price)
        .reduce(Money.zero("USD"), Money::plus); // Money immutable
```

## Production Example

```java
public record PricingContext(String currency, Rate taxRate) { }

Function<Cart, Quote> price = cart -> engine.quote(cart, ctx);
// ctx immutable — safe to close over and reuse across threads
```

Publish snapshots: `List.copyOf`, records, defensive copies — then share freely with readers.

## When Better / Worse

| Better | Worse |
|--------|-------|
| Domain money/events/DTOs | Extreme chattiness allocating per tiny field update in hot loop — mutate carefully inside one method then publish |
| Keys in maps; parallel reduce | Pretend entities are immutable while exposing mutable lists |

## Performance & Readability

Immutability can increase allocations; usually wins on correctness. Profile before micro-optimizing to mutability.

## Common Mistake

`final List<X> list` that is still mutable — `final` ≠ deep immutability. Use `List.copyOf` / unmodifiable.

## Interview / PE

- Effectively final vs immutable object?  
- **PE:** immutable event payloads on a virtual-thread workers pool — why?

### Related

[side-effects.md](./side-effects.md) · [closures.md](./closures.md) · [binary-operator.md](./binary-operator.md)

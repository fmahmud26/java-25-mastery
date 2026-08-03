# When to Use Functional Style

Decision guide: imperative vs functional in production Java.

## Imperative vs functional (same job)

```java
// Imperative — authorize payments over a threshold
List<Payment> authorized = new ArrayList<>();
for (Payment p : payments) {
    if (p.status() == Status.CAPTURED && p.cents() >= minCents) {
        authorized.add(p);
    }
}

// Functional
List<Payment> authorized = payments.stream()
        .filter(p -> p.status() == Status.CAPTURED)
        .filter(p -> p.cents() >= minCents)
        .toList();
```

Both fine. Functional wins when policies compose; imperative wins when you need complex early-exit, mutable accumulators, or crystal-clear step debugging.

## Improves design when…

1. **Behavior is a parameter** — `Function`/`Predicate`/`Comparator` injected (Strategy without classes).  
2. **Transforms are pure** — same input → same output; easy tests.  
3. **Pipelines are shallow** — 2–4 steps, readable names via method refs.  
4. **APIs already expect SAMs** — `removeIf`, `computeIfAbsent`, `sort`.

## Makes code worse when…

1. Lambdas exceed ~5–10 lines or nest deeply.  
2. You mutate shared state inside `forEach` “because stream.”  
3. Exception-heavy JDBC/tx logic forced into `Function`.  
4. Parallel streams for tiny lists or blocking I/O (often worse).  
5. Composition obscures order (`compose` vs `andThen` confusion).

## Performance trade-offs

| Concern | Guidance |
|---------|----------|
| Boxing | Prefer `IntPredicate` / primitive streams for hot numeric paths |
| Allocation | Lambdas + streams allocate; OK unless profiled hot |
| Call site | Monomorphic method refs often inline well |
| Parallel | Only for CPU-bound, large, associative work |

## Readability trade-offs

Named private methods + method references beat opaque `x -> y -> z -> …`.  
Code review bar: “Can a teammate explain this in 30 seconds?”

## Principal scenarios

1. Pricing rules: compose `Function<Cart, Money>` vs rules engine class?  
2. Fraud checks: `Predicate<Payment>` chain vs explicit service with logging/metrics at each step?  
3. Where do you put side effects in a stream pipeline (hint: usually not mid-map)?

### Related

[side-effects.md](./side-effects.md) · [immutability.md](./immutability.md) · [interview.md](./interview.md)

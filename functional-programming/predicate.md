# Predicate\<T>

`boolean test(T t)` — reusable boolean policy; composable with `and`/`or`/`negate`.

## Mental Model

```text
Predicate<Payment> = rule that returns pass/fail
filter / removeIf / assert
```

## Imperative vs Functional

```java
List<Payment> out = new ArrayList<>();
for (Payment p : payments) {
    if (p.captured() && p.cents() >= 100) out.add(p);
}

Predicate<Payment> eligible =
        Payment::captured
                .and(p -> p.cents() >= 100);
List<Payment> out = payments.stream().filter(eligible).toList();
```

## Production Example

```java
public final class FraudPolicies {
    public static Predicate<Payment> amountAtLeast(long minCents) {
        return p -> p.cents() >= minCents;
    }

    public static Predicate<Payment> notCountry(String blocked) {
        return p -> !blocked.equalsIgnoreCase(p.country());
    }
}

Predicate<Payment> captureOk = FraudPolicies.amountAtLeast(50)
        .and(FraudPolicies.notCountry("XX"))
        .and(Predicate.not(Payment::sandbox));

payments.removeIf(Predicate.not(captureOk));
```

## Composition

`and` / `or` short-circuit. `Predicate.not` (11+). `isEqual`. Primitive: `IntPredicate`, etc.

## When Better / Worse

| Better | Worse |
|--------|-------|
| Named reusable rules | Predicates that log/send email (side effects) |
| Dynamic rule lists | Single-use complex branching — imperative clearer |

## Performance & Readability

Compose a few named predicates > one giant lambda. Boxing: use `IntPredicate` for hot int filters.

## Common Mistake

Predicates with side effects (metrics inside `test`) — hard to test/reuse; use explicit steps.

## Interview / PE

- How does `and` short-circuit?  
- **PE:** feature-flagged fraud rules as `List<Predicate<Payment>>` — observability?

### Related

[function.md](./function.md) · [side-effects.md](./side-effects.md) · [function-composition.md](./function-composition.md)

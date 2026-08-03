# Effectively Final Variables

Locals/parameters captured by lambdas must not be reassigned — **final** or **effectively final**.

## Mental Model

```text
int min = 100;           // effectively final
Predicate<Payment> p = x -> x.cents() >= min;
// min = 200;            // compile error if captured
```

## Why the Rule Exists

Captures copy values into the lambda (like enclosing instance fields). Allowing reassignment would make it unclear whether the lambda sees updates — and match historical inner-class rules. Mutation of **heap objects** is still possible if the *reference* is stable.

## Imperative vs Functional Pitfall

```java
int count = 0;
for (Payment p : payments) if (p.captured()) count++;

// Broken idea:
int count = 0;
payments.forEach(p -> { if (p.captured()) count++; }); // won't compile

// OK but often wrong for parallel:
AtomicInteger count = new AtomicInteger();
payments.forEach(p -> { if (p.captured()) count.incrementAndGet(); });

// Better:
long count = payments.stream().filter(Payment::captured).count();
```

## Production Example

```java
long minCents = config.minCaptureCents(); // effectively final
List<Payment> ok = payments.stream()
        .filter(p -> p.cents() >= minCents)
        .toList();
```

## Common Mistake

```java
var box = new int[]{0};
Runnable r = () -> box[0]++; // compiles — mutates array, not rebinding box
```

Works; confuses readers; races if shared across threads.

## Interview / PE

- What does effectively final mean?  
- Why can’t lambdas mutate locals?  
- **PE:** how do you accumulate in streams correctly (reduce/collect)?

### Related

[closures.md](./closures.md) · [lambda-expressions.md](./lambda-expressions.md) · [side-effects.md](./side-effects.md)

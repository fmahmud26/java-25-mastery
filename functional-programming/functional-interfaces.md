# Functional Interfaces

Exactly one abstract method (SAM) — the target type for lambdas and method references.

## Mental Model

```text
@FunctionalInterface
interface PaymentFilter { boolean allow(Payment p); }

PaymentFilter f = p -> p.cents() > 0;   // implements allow
```

Defaults/statics don’t count toward the SAM count.

## Imperative vs Functional

```java
// Imperative strategy via class
final class MinAmountFilter implements PaymentFilter {
    private final long min;
    MinAmountFilter(long min) { this.min = min; }
    public boolean allow(Payment p) { return p.cents() >= min; }
}

// Functional — same seam
PaymentFilter minAmount = p -> p.cents() >= min;
```

## Production Example

```java
@FunctionalInterface
public interface CapturePort {
    CaptureResult capture(CaptureCommand cmd);
}

// adapters remain classes; call sites can still accept SAM for tests:
CapturePort stub = cmd -> CaptureResult.ok(cmd.paymentId());
```

JDK: `Predicate`, `Function`, `Consumer`, `Supplier`, operators, `Comparator`, `Runnable`.

## Internal Behavior

Lambdas bind via `invokedynamic` + `LambdaMetafactory`. Capturing lambdas allocate; non-capturing may be cached. Not the same as anonymous classes (`this`, class files).

## When Better / Worse

| Better | Worse |
|--------|-------|
| Single-method policies & callbacks | Multi-method APIs forced into one mega-SAM |
| Test fakes in one line | Domain ports that need 4 methods — use a real interface |

## Performance & Readability

Primitive specializations avoid boxing. Custom named SAMs (`FraudCheck`) beat raw `Function` when ubiquitous language matters.

## Common Mistake

Adding a second abstract method to a “functional” interface — breaks all lambdas. Use `@FunctionalInterface`.

## Interview / PE

- SAM rules? Object methods?  
- Lambda vs anonymous `this`?  
- **PE:** When is a domain-named FI better than `Function<T,R>`?

### Related

[lambda-expressions.md](./lambda-expressions.md) · [predicate.md](./predicate.md) · [function.md](./function.md)

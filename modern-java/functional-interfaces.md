# Functional Interfaces

**Introduced:** Java 8 (`java.util.function`) · **Java 25:** still the SAM contract for lambdas

## Problem Before

Every callback needed a bespoke interface (`Callback`, `Handler`, `MyListener`) or Guava-style types — inconsistent and verbose.

## The Feature

A **functional interface** has exactly one abstract method (SAM). `@FunctionalInterface` documents and enforces that. Package `java.util.function` supplies common shapes.

| Type | Shape | Example use |
|------|-------|-------------|
| `Function<T,R>` | T → R | Map DTO → entity id |
| `Predicate<T>` | T → boolean | Filter active accounts |
| `Consumer<T>` | T → void | Side-effect send |
| `Supplier<T>` | () → T | Lazy config |
| `UnaryOperator<T>` | T → T | Normalize string |
| `BiFunction` / `BiConsumer` | two args | Merge, logging |

Primitive specializations (`ToLongFunction`, `LongPredicate`, …) avoid boxing.

## How It Works

Lambdas/`::` target a functional interface type via inference. Default/static methods on the interface don’t count toward the SAM count. Compositions: `predicate.and(...)`, `function.andThen(...)`.

## Before → After

```java
// Before
interface PaymentFilter {
    boolean accept(Payment p);
}

// After — use Predicate or keep domain name if ubiquitous language needs it
Predicate<Payment> captureReady = p -> p.status() == Status.AUTHORIZED;

@FunctionalInterface
public interface CapturePort {
    CaptureResult capture(CaptureCommand cmd); // domain-named SAM — good
}
```

## Production Usage

- Stream APIs, Spring `*Function` beans, Retry predicates  
- Domain ports that are naturally one method  
- Prefer **named** functional interfaces for ubiquitous language (`FraudCheck`), JDK types for generic plumbing

## Trade-offs

| Pros | Cons |
|------|------|
| Shared vocabulary | `Function` everywhere erases domain meaning |
| Composition helpers | Primitive specializations add API surface |
| `@FunctionalInterface` safety | Adding a second abstract method breaks binaries |

## When NOT to Use

- Two+ methods that belong together → normal interface  
- Need instance state → class/record  
- “Functional” wrapper over a rich service — keep a real port with several methods (ISP) rather than forcing one mega-SAM

## Migration Notes

Replace one-method anonymous interfaces with lambdas targeting JDK or domain SAMs. Don’t rename every interface to `Function` — keep `PaymentPort`.

## Interview Questions

- Rules for `@FunctionalInterface`?  
- Why `ToLongFunction` exists?  
- Can functional interfaces have default methods?  
- When prefer custom SAM vs `Function`?

### Related

[lambdas.md](./lambdas.md) · [optional.md](./optional.md) · [modern-coding-style.md](./modern-coding-style.md)

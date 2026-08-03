# Lambdas

**Introduced:** Java 8 · **Java 25:** foundational (unchanged core model)

## Problem Before

Callbacks required verbose anonymous classes — noisy, awkward `this`, hard to read at call sites.

```java
// Before (Java 7 style)
Collections.sort(orders, new Comparator<Order>() {
    @Override
    public int compare(Order a, Order b) {
        return Long.compare(a.totalCents(), b.totalCents());
    }
});
```

## The Feature

A **lambda** is a concise implementation of a **functional interface** (single abstract method). Method references (`Order::totalCents`) bind existing methods.

## How It Works

Compiler desugars to invokedynamic + LambdaMetafactory. Captured locals must be **effectively final**. Runtime may use non-capturing singleton strategies for allocation-sensitive cases.

## Before → After

```java
// After
orders.sort(Comparator.comparingLong(Order::totalCents));

payments.forEach(p -> ledger.post(p));
payments.forEach(ledger::post);   // when signatures match
```

```java
ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor(); // 21+
pool.execute(() -> gateway.capture(cmd));
```

## Production Usage

- Comparators, listeners, stream pipelines, CompletableFuture callbacks  
- Prefer method references when they read clearer than `x -> x.foo()`  
- Keep lambdas **short**; extract named methods when logic grows

## Trade-offs

| Pros | Cons |
|------|------|
| Less boilerplate | Stack traces show `lambda$...` |
| Enables Streams / APIs | Over-nested lambdas hurt debugging |
| Clear intent at call site | Accidental capture of mutable state (if not effectively final — compile error; or capturing mutable *objects*) |

## When NOT to Use

- Multi-method behavior → real class/record  
- Need identity / fields / serialization of the function itself casually  
- Extremely hot micro-benchmarked paths where a named class profiles clearer (rare; measure)

## Migration Notes

1. Replace anonymous SAM classes with lambdas.  
2. Don’t rewrite every loop into streams in the same PR.  
3. Train “effectively final” and capture of mutable holders (`atomic`, arrays) as smells.

## Interview Questions

- What is a functional interface?  
- Why must captured locals be effectively final?  
- Lambda vs anonymous class — `this` difference?  
- How are lambdas implemented (invokedynamic)?

### Related

[functional-interfaces.md](./functional-interfaces.md) · [optional.md](./optional.md) · [modern-coding-style.md](./modern-coding-style.md)

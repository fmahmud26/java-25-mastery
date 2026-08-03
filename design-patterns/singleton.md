# Singleton

## Problem

A type should have at most one instance (process-wide config hub, metrics registry) with a single access point.

## Forces

- Shared global state temptation  
- Thread-safe init  
- Testability / replaceability  
- Hidden dependencies vs explicit DI  

## Naive solution

```java
public static Config INSTANCE = new Config(); // or lazy if (x==null) x=new... without sync
```

Races create two instances; tests cannot substitute; everything couples to globals.

## Pattern

Control instantiation so only one exists; modern Java prefers **DI singleton scope** or `enum` when a true process singleton is required.

## Implementation

```java
// OK when truly needed — enum singleton
public enum Secrets {
    INSTANCE;
    private final String apiKey = System.getenv("API_KEY");
    public String apiKey() { return apiKey; }
}

// Prefer in apps: one bean via DI (Spring @ApplicationScope / constructor inject)
public final class CheckoutFacade {
    private final PaymentClient payments; // not PaymentClient.getInstance()
    public CheckoutFacade(PaymentClient payments) { this.payments = payments; }
}
```

Double-checked locking is historically error-prone; avoid teaching it as first choice.

## Trade-offs

| + | − |
|---|---|
| Shared resource coordination | Global mutable state |
| Simple access | Hidden deps; hard tests |
| Enum is serialization-safe | Parallel tests / multi-tenant pain |

## When to use

Rare: process-wide immutable config, shared thread-safe registries — prefer containers to own lifecycle.

## When NOT to use

Business services, repositories, “I’ll just singleton the DbConnection.” Prefer DI.

## Production example

**Metrics registry** or **feature-flag client** as a single process instance injected everywhere — not `OrderService.getInstance()`.

## Interview question

*Singleton vs Spring singleton bean? Why do singletons hurt unit tests? Enum vs holder idiom?*

**SOLID/LLD:** Often conflicts with DIP; LLD: avoid service-locator singletons.

### Related

[dependency-injection.md](./dependency-injection.md) · [patterns-and-solid.md](./patterns-and-solid.md)

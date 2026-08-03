# Logging

## Purpose

Make production debuggable without drowning in noise or leaking secrets.

## Before

```java
System.out.println("user=" + user + " token=" + token);
log.debug("order " + order); // huge, maybe PII
```

## After

```java
log.info("order_paid orderId={} customerId={} amountCents={}",
        order.id(), order.customerId(), order.total().cents());
// structured fields for log aggregation
```

## Levels

| Level | Use |
|-------|-----|
| ERROR | Failed operation needing attention |
| WARN | Degraded / retry |
| INFO | Business milestones |
| DEBUG | Diagnostic — sampled in prod |

## Rules

- No passwords, tokens, PANs, session IDs  
- Prefer parameterized logging (no string concat cost when disabled)  
- Correlation / trace ids on every request  
- Don’t log entire payloads by default  

## Trade-offs

More logs ≠ more observability; cardinality explosions cost money.

## PE Decision

Define a logging standard per domain event; alert on ERROR rate, not on DEBUG absence.

### Related

[observability.md](./observability.md) · [error-handling.md](./error-handling.md)

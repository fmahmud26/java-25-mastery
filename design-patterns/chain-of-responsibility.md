# Chain of Responsibility

## Problem

A request should pass through a pipeline of handlers (authn, authz, validation, rate limit) where each may handle, enrich, or stop the chain.

## Forces

- Ordered processing  
- Add/remove handlers without rewriting callers  
- Short-circuit on failure  
- Keep handlers focused (SRP)  

## Naive solution

```java
if (!auth.ok()) return;
if (!authz.ok()) return;
if (!validator.ok()) return;
if (!rateLimit.ok()) return;
controller.handle();
```

Monolithic filter method; hard to reuse handlers.

## Pattern

Each handler points to the next (or a list is composed); request flows along the chain.

## Implementation

```java
public interface HttpFilter {
    void doFilter(HttpRequest req, HttpResponse res, Chain chain);
    interface Chain { void next(HttpRequest req, HttpResponse res); }
}

public final class AuthFilter implements HttpFilter {
    public void doFilter(HttpRequest req, HttpResponse res, Chain chain) {
        if (!tokens.valid(req)) { res.unauthorized(); return; }
        chain.next(req, res);
    }
}

public final class RateLimitFilter implements HttpFilter {
    public void doFilter(HttpRequest req, HttpResponse res, Chain chain) {
        if (!limiter.tryAcquire(req.clientId())) { res.tooMany(); return; }
        chain.next(req, res);
    }
}

// composition: auth → rate limit → controller
```

Servlet filters / Spring `FilterChain` / Netty pipelines are CoR in production clothing.

## Trade-offs

| + | − |
|---|---|
| Flexible pipelines | Debugging “who stopped it?” |
| OCP for new handlers | Order bugs |
| Good SRP | Performance if chain is huge/naïve |

## When to use

Middleware, validation pipelines, approval workflows, logging/metrics wrappers in sequence.

## When NOT to use

Single check; need guaranteed all-handlers-run aggregator (use a list + fold carefully). Don’t confuse with Decorator stacking on a functional interface (similar but different intent).

## Production example

**API gateway path:** authenticate → authorize → schema validate → handler.

## Interview question

*CoR vs Decorator? Who decides to continue the chain? How test a single filter?*

**SOLID/LLD:** OCP + SRP; LLD middleware.

### Related

[decorator.md](./decorator.md) · [command.md](./command.md)

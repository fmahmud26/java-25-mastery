# Proxy

## Problem

Control access to a real object — caching, authorization, lazy load, remote call, rate limit — while presenting the same interface.

## Forces

- Keep subject interface unchanged  
- Add access policy without editing subject  
- Local stand-in for remote (gRPC stub feel)  
- Lifecycle: lazy expensive creation  

## Naive solution

Scatter cache/auth checks into every caller or into the subject (SRP violation).

## Pattern

Proxy implements the subject’s interface and delegates to the real subject when policy allows.

## Implementation

```java
public interface ProductCatalog {
    Product find(ProductId id);
}

public final class CachingProductCatalogProxy implements ProductCatalog {
    private final ProductCatalog db;
    private final Cache<ProductId, Product> cache;
    public Product find(ProductId id) {
        return cache.get(id, db::find);
    }
}

public final class AuthorizingCatalogProxy implements ProductCatalog {
    private final ProductCatalog inner;
    private final Authz authz;
    public Product find(ProductId id) {
        authz.require("catalog:read");
        return inner.find(id);
    }
}
```

## Trade-offs

| + | − |
|---|---|
| Clear access policies | Proxy hell / debugging |
| Same interface as subject | Cache consistency complexity |
| Good for cross-cutting | Overlap with Decorator conceptually |

## When to use

Caching repositories, security proxies, lazy init, virtual proxies for heavy graphs, remote proxies.

## When NOT to use

You need to *change* the interface — that’s Adapter. Stacking many behaviors for features → Decorator mindset may fit better.

## Production example

**Product catalog:** DB repository behind cache proxy + auth proxy in the composition root.

## Interview question

*Proxy vs Decorator (intent)? Protection vs virtual vs remote proxy?*

**SOLID/LLD:** SRP for access concerns; LLD caching layers.

### Related

[decorator.md](./decorator.md) · [adapter.md](./adapter.md)

# Adapter

## Problem

An existing class/SDK has an incompatible interface with what your domain needs — you must integrate without rewriting the foreign code.

## Forces

- Cannot change vendor API  
- Domain wants a clean port (`PaymentGateway`)  
- Multiple vendors over time  
- Isolate translation / quirky types  

## Naive solution

```java
// domain service calls Stripe SDK types directly
StripeCharge c = stripe.charges.create(StripeChargeRequest...);
```

Domain polluted; swapping vendors rewrites core.

## Pattern

Adapter implements your target interface and translates calls to the adaptee.

## Implementation

```java
public interface PaymentGateway {
    ChargeResult charge(ChargeRequest request);
}

public final class StripePaymentAdapter implements PaymentGateway {
    private final StripeClient stripe;
    public ChargeResult charge(ChargeRequest request) {
        var stripeReq = toStripe(request);
        var resp = stripe.charges().create(stripeReq);
        return toDomain(resp);
    }
}
```

Object adapter (composition) preferred over class adapter (inheritance) in Java.

## Trade-offs

| + | − |
|---|---|
| Anti-corruption layer | Mapping boilerplate |
| Test domain with fake gateway | Adapter bugs = integration bugs |
| DIP compliance | Two models to maintain |

## When to use

Legacy APIs, third-party SDKs, DB drivers behind repositories (same idea).

## When NOT to use

You own both interfaces and can change them to match — just refactor.

## Production example

**Tax service:** Avalara SDK adapted to `TaxCalculator` port used by checkout.

## Interview question

*Adapter vs Facade vs Anti-Corruption Layer? Where does mapping live?*

**SOLID/LLD:** DIP + LSP for the port; hexagonal LLD staple.

### Related

[facade.md](./facade.md) · [abstract-factory.md](./abstract-factory.md)

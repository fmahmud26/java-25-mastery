# Abstract Factory

## Problem

You need to create a **family** of related products (client, webhook parser, refund service) that must match one vendor/theme — without scattering vendor `if`s.

## Forces

- Products are used together (Stripe client ≠ Adyen webhook parser)  
- Swap entire family by config  
- Keep domain code vendor-agnostic  

## Naive solution

```java
if (vendor == STRIPE) {
   pay = new StripeClient(...);
   hooks = new StripeWebhook(...);
} else {
   pay = new AdyenClient(...);
   hooks = new StripeWebhook(...); // bug: mismatched family
}
```

Easy to mix families; every use case repeats vendor switches.

## Pattern

Abstract factory exposes factory methods for each product in the family; concrete factories implement one vendor’s set.

## Implementation

```java
public interface PaymentFamilyFactory {
    PaymentClient payments();
    WebhookVerifier webhooks();
    RefundService refunds();
}

public final class StripeFamilyFactory implements PaymentFamilyFactory {
    public PaymentClient payments() { return new StripePaymentClient(cfg); }
    public WebhookVerifier webhooks() { return new StripeWebhookVerifier(cfg); }
    public RefundService refunds() { return new StripeRefundService(cfg); }
}

public final class AdyenFamilyFactory implements PaymentFamilyFactory { /* ... */ }

// composition root
PaymentFamilyFactory family = switch (cfg.vendor()) {
    case STRIPE -> new StripeFamilyFactory(cfg);
    case ADYEN -> new AdyenFamilyFactory(cfg);
};
checkout = new CheckoutService(family.payments(), family.webhooks());
```

## Trade-offs

| + | − |
|---|---|
| Family consistency | Many interfaces/classes |
| Clean vendor swap | Overkill for one product type |
| Good for plugins | Evolving family = changing abstract factory |

## When to use

Multi-vendor stacks with several related objects; UI widget families; cloud provider SDKs.

## When NOT to use

Only one product type → plain Factory/Strategy. One vendor forever.

## Production example

**Payment platform:** Stripe vs Adyen families for charge + webhook + refund used by the same checkout pipeline.

## Interview question

*Factory Method vs Abstract Factory? What goes wrong if you mix products from two concrete factories?*

**SOLID/LLD:** DIP for ports; LLD multi-PSP systems.

### Related

[factory.md](./factory.md) · [adapter.md](./adapter.md)

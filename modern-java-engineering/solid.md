# SOLID in Modern Java Practice

| Principle | Production reading |
|-----------|-------------------|
| SRP | One reason to change — CheckoutFacade ≠ PricingPolicy |
| OCP | Strategy/sealed extension without editing callers |
| LSP | Subtypes don’t break contracts (no surprise throws) |
| ISP | Narrow ports (`RefundPort` ≠ god `PaymentOps`) |
| DIP | Depend on `PaymentGateway`, not Stripe SDK |

Before/after: fat service class → ports + adapters + strategies.

### Related

[api-design.md](./api-design.md) · [clean-code.md](./clean-code.md)

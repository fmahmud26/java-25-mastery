# Patterns ↔ LLD

Low-level design interviews reward **use-case structure**, not pattern name-drops.

## Example: Checkout (e-commerce)

| Concern | Pattern candidate | Why |
|---------|-------------------|-----|
| One API for clients | **Facade** | Hide inventory + pay + ledger |
| Multiple PSPs | **Strategy** or **Adapter** | Swap processors / wrap vendor SDKs |
| Provider-specific product families | **Abstract Factory** | Stripe vs Adyen client+webhook+refund types |
| Order status | **State** | Legal transitions |
| Notify email/SMS/push | **Observer** or events | Decouple side effects |
| Idempotent pay retry | **Command** + outbox | Queueable work |
| Fraud / validation pipeline | **Chain of Responsibility** | Ordered checks |
| Complex order construction | **Builder** | Many optional lines/address fields |

## Example: Reporting

| Concern | Pattern |
|---------|---------|
| PDF vs CSV | **Factory** / Strategy |
| Shared export steps | **Template Method** or pipeline |
| Authz + audit wrapping | **Decorator** / **Proxy** |

## LLD tip

Draw components and edges first. Name the pattern only when it clarifies the edge (*“PaymentPort ← Adapter ← StripeSdk”*).

### Related

[patterns-and-solid.md](./patterns-and-solid.md) · [facade.md](./facade.md) · [strategy.md](./strategy.md)

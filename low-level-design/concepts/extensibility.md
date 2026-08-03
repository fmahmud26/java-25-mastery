# Extensibility — Change Without Rewrites

Extensibility is successful when a **new requirement touches new types + wiring**, not a cascade of edits.

## Techniques

| Technique | Why |
|-----------|-----|
| Strategy / policy objects | Vary algorithms |
| Ports & adapters | Vary I/O vendors |
| Sealed types + pattern match | Closed variant sets with exhaustiveness |
| Event hooks / domain events | Side effects without bloating core |
| Feature flags at edges | Gradual rollout without fork |

## Test: “Add X”

| Add… | Expected blast radius |
|------|------------------------|
| New vehicle size | New `Vehicle` + allocator rule |
| New fee rule | New `PricingPolicy` |
| New pay provider | New adapter behind `PaymentGateway` |
| New notify channel | New `Channel` |

If answer is “edit the big switch in Service,” design failed OCP.

## What not to abstract yet

YAGNI: don’t invent factories for one concrete path. Staff says: “I’ll extract when a second variant appears or when testing demands a port.”

# OOP + SOLID — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Domain money/IDs | `record` value types | Immutability, clear equals |
| Pricing / shipping rules | Strategy + DI | OCP without editing core checkout |
| API error taxonomy | Sealed error types | Exhaustive handling |
| Persistence | Repository port interface | DIP; swap JPA/jdbc in tests |
| Legacy god class | Extract roles by reason-to-change | SRP migration, not big-bang rewrite |
| Framework entrypoints | Thin adapters → domain services | Keep DIP inward |

## Production rules of thumb

- SOLID guides **boundaries**; don’t invent interfaces for every private helper.
- Prefer **composition + sealed models** over deep inheritance trees.
- Testability is a SOLID smell detector: hard to fake ⇒ DIP/ISP probably broken.
- Document Liskov contracts (pre/postconditions) when overriding critical methods.

Related: [api-design.md](../../modern-java-engineering/api-design.md), [domain-modeling.md](../../modern-java-engineering/domain-modeling.md), [clean-code.md](../../modern-java-engineering/clean-code.md).

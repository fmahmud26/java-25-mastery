# Patterns ↔ SOLID

Patterns don’t replace SOLID; they **encode** common SOLID-friendly shapes.

| Pattern | SOLID angle |
|---------|-------------|
| **Strategy** | OCP + DIP — new algorithm without editing caller |
| **Factory / Abstract Factory** | DIP — callers depend on product interfaces |
| **Decorator** | OCP + SRP — extend behavior without rewriting core |
| **Adapter** | DIP + LSP — wrap foreign API behind your port |
| **Observer** | OCP — add subscribers; ISP — narrow event APIs |
| **Proxy** | SRP — separate access control/caching from core |
| **Facade** | SRP — simplify subsystem for a use case |
| **Command** | SRP — request as object; OCP for new ops |
| **State** | OCP — new states; SRP per state class |
| **Template Method** | OCP via hooks — careful: inheritance coupling |
| **CoR** | OCP — new handlers; SRP per handler |
| **Singleton** | Often **hurts** DIP/testability — prefer DI scope |
| **Builder** | SRP — construction separate from business use |

## Interview move

“We used Strategy to satisfy OCP for shipping rules; the context depends on `ShippingCost` (DIP).”

### Related

[patterns-and-lld.md](./patterns-and-lld.md) · [README.md](./README.md)

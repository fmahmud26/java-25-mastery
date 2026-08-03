# How to Think About Patterns

## Patterns are compression for design conversations

Saying “Strategy” should communicate: *swap algorithms behind an interface without rewriting the caller*.

## Forces (why “it depends”)

Forces are competing pressures, e.g.:

- Flexibility vs simplicity  
- Performance vs clarity  
- Testability vs global access  
- Open for extension vs YAGNI  

If you cannot name forces, you are memorizing.

## Naive → Pattern

Always show the **painful** code first. The pattern earns its complexity by removing that pain.

## SOLID mapping (cheat sheet)

| SOLID | Patterns often involved |
|-------|-------------------------|
| SRP | Facade, Command, Strategy (split responsibilities) |
| OCP | Strategy, Decorator, Factory, CoR |
| LSP | Any subtype plug-in (Strategy, State) — don’t break contracts |
| ISP | Fat listener interfaces → split Observers |
| DIP | Factory, Strategy, Adapter — depend on abstractions |

Deep dive: [patterns-and-solid.md](./patterns-and-solid.md)

## LLD mapping

In LLD interviews, patterns appear as **structure for use cases**:

- Checkout → Facade + Strategy (payment) + Observer (events)  
- Notification → Factory / Abstract Factory  
- Workflow → State / Template / CoR  

Deep dive: [patterns-and-lld.md](./patterns-and-lld.md)

## Anti-pattern: Pattern souvenir hunting

Adding Decorator+Proxy+Facade+AbstractFactory to a CRUD app because “clean architecture” — without forces — fails reviews.

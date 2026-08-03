# Design Patterns — Decision Guide (not a flashcard deck)

Patterns are **named solutions to recurring design forces**. Memorizing UML is useless; explaining *when and why* is Principal-level.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## How to study every pattern

```text
Problem → Forces → Naive solution → Pattern → Implementation
  → Trade-offs → When to use → When NOT to use
  → Production example → Interview question
```

Mindset: [how-to-think-about-patterns.md](./how-to-think-about-patterns.md)  
Bridges: [patterns-and-solid.md](./patterns-and-solid.md) · [patterns-and-lld.md](./patterns-and-lld.md)

## Catalog (required set)

### Creational

| Pattern | Domain flavor | Doc |
|---------|---------------|-----|
| Factory | Reports, notifications | [factory.md](./factory.md) |
| Abstract Factory | Payment provider families | [abstract-factory.md](./abstract-factory.md) |
| Builder | Complex orders / search queries | [builder.md](./builder.md) |
| Singleton | Config (use sparingly) | [singleton.md](./singleton.md) |

### Structural

| Pattern | Domain flavor | Doc |
|---------|---------------|-----|
| Adapter | Legacy PSP / tax API | [adapter.md](./adapter.md) |
| Decorator | Pricing / auth wrappers | [decorator.md](./decorator.md) |
| Facade | Checkout orchestration | [facade.md](./facade.md) |
| Proxy | Caching, auth, remote | [proxy.md](./proxy.md) |

### Behavioral

| Pattern | Domain flavor | Doc |
|---------|---------------|-----|
| Strategy | Shipping / pricing / fraud | [strategy.md](./strategy.md) |
| Observer | Order events / notifications | [observer.md](./observer.md) |
| Command | Job queue / undo | [command.md](./command.md) |
| State | Order / payment lifecycle | [state.md](./state.md) |
| Template Method | ETL / report pipelines | [template-method.md](./template-method.md) |
| Chain of Responsibility | Authz / validation / middleware | [chain-of-responsibility.md](./chain-of-responsibility.md) |

## Interview

[interview.md](./interview.md)

## One-line PE rule

**Pick a pattern only when the naive design’s pain is real — patterns without forces are ceremony.**

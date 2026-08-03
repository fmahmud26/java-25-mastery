# Design Patterns — Cheat Sheet

**Sources:** [../design-patterns/README.md](../design-patterns/README.md) · [how-to-think-about-patterns](../design-patterns/how-to-think-about-patterns.md) · [patterns-and-lld](../design-patterns/patterns-and-lld.md) · [low-level-design/concepts/design-patterns](../low-level-design/concepts/design-patterns.md) · [java-interview-questions/design-patterns](../java-interview-questions/design-patterns/)

## How to talk about a pattern

```text
Problem → Forces → Naive → Pattern → Trade-offs → When NOT
```

(from design-patterns chapter)

## Catalog (repo set only)

### Creational

| Pattern | When (chapter flavor) | Doc |
|---------|----------------------|-----|
| Factory | Vary creation | [factory](../design-patterns/factory.md) |
| Abstract Factory | Families (e.g. PSP) | [abstract-factory](../design-patterns/abstract-factory.md) |
| Builder | Complex construction | [builder](../design-patterns/builder.md) |
| Singleton | True process-wide — sparingly | [singleton](../design-patterns/singleton.md) |

### Structural

| Pattern | When | Doc |
|---------|------|-----|
| Adapter | Legacy / foreign API | [adapter](../design-patterns/adapter.md) |
| Decorator | Cross-cutting around port | [decorator](../design-patterns/decorator.md) |
| Facade | Coarse use-case API | [facade](../design-patterns/facade.md) |
| Proxy | Cache / auth / remote | [proxy](../design-patterns/proxy.md) |

### Behavioral

| Pattern | When | Doc |
|---------|------|-----|
| Strategy | Swap algorithms | [strategy](../design-patterns/strategy.md) |
| Observer | Fan-out events (process-local) | [observer](../design-patterns/observer.md) |
| Command | Queue / undo | [command](../design-patterns/command.md) |
| State | Lifecycle behavior | [state](../design-patterns/state.md) |
| Template Method | Fixed steps + hooks | [template-method](../design-patterns/template-method.md) |
| Chain of Responsibility | Pipeline handlers | [chain-of-responsibility](../design-patterns/chain-of-responsibility.md) |

## Reliability reminder (bank)

In-process Observer ≠ durable outbox — [q04 outbox-not-observer](../java-interview-questions/design-patterns/q04-outbox-not-observer.md)

## Anti-pattern

Pattern theater / YAGNI layers — [q05](../java-interview-questions/design-patterns/q05-pattern-overuse.md)

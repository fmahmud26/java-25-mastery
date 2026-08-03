# Low-Level Design — Internals

What good LLD answers make explicit about structure and concurrency.

## Structural internals

| Piece | Point |
|-------|-------|
| Domain vs application vs ports | Dependencies point inward |
| Invariants | Enforced in constructors/methods, not callers |
| Identity vs value | Entities vs `record` values |
| State transitions | Illegal transitions throw / return errors |

## Concurrency internals (single process)

| Approach | When |
|----------|------|
| Confine mutability | Best default |
| `synchronized` / locks | Multi-field invariants |
| `ConcurrentHashMap` | Fine-grained map ops |
| Immutable snapshots | Publish read models safely |

## Pattern internals (only if asked)

- Strategy: interface + inject.  
- Observer: registration list + notify (mind concurrent mod).  
- Singleton: avoid for domain; prefer DI scoping.

Related: [thread-safety.md](../../low-level-design/concepts/thread-safety.md), [extensibility.md](../../low-level-design/concepts/extensibility.md).

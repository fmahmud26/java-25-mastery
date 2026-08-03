# Design Patterns — When and Why

Patterns are **named solutions to recurrence**, not decorations.

| Pattern | Use when | Why | Avoid when |
|---------|----------|-----|------------|
| Strategy | Algorithm varies (price, allocate, limit) | Swap without editing callers | One algorithm forever |
| State | Behavior depends on lifecycle | Invalid transitions become impossible | Simple boolean flags suffice |
| Factory / Abstract Factory | Creation rules vary | Hide construction complexity | `new` is trivial and stable |
| Observer / Pub-Sub port | Many consumers of an event | Decouple producers from channels | Single hard-wired caller |
| Decorator | Cross-cutting around a port | Stack logging/metrics/retry | Business rules belong in domain |
| Facade | Coarse use-case API | Interview-friendly entrypoint | Facade becomes god object |
| Singleton | Truly process-wide resource | One logger config, one meter registry | Hideable global mutable state |
| Template Method | Fixed steps, custom hooks | Shared workflow | Composition/strategy clearer |
| Command | Queue/undo/audit actions | Reify requests | Pass-through method calls |

## Staff phrasing

“Elevator dispatch is Strategy because buildings tune policies; cabin behavior is State because OPENING vs MOVING reject different commands.”

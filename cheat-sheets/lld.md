# LLD — Cheat Sheet

**Sources:** [../low-level-design/README.md](../low-level-design/README.md) · [interview.md](../low-level-design/interview.md) · [concepts/](../low-level-design/concepts/) · [systems/](../low-level-design/systems/) · [interview-prep/tracks/lld](../interview-prep/tracks/lld.md)

## Interview spine (chapter)

```text
Requirements → Use cases → Domain → Classes → Interfaces
→ Relationships → SOLID → Patterns → Thread safety
→ Errors → Extensibility → Testing → Trade-offs
```

Decision grammar: **choice → why → rejected**

## What “good” sounds like (README)

- Name **invariants**  
- Interfaces only at real variation  
- Domain ≠ I/O adapters  
- Lock granularity explicit  
- “Add X” without editing callers  

## SOLID one-liners

See [concepts/solid.md](../low-level-design/concepts/solid.md) — SRP/OCP/LSP/ISP/DIP as **boundary justification**, not checklist spam.

## Pattern triggers (LLD)

| Clue | Pattern | Concept doc |
|------|---------|-------------|
| Swap algorithm | Strategy | [design-patterns concept](../low-level-design/concepts/design-patterns.md) |
| Lifecycle behavior | State | same |
| Coarse API | Facade | same |
| Pluggable I/O | Port + adapter | [interfaces](../low-level-design/concepts/interfaces.md) |

## Concurrency in LLD

[thread-safety.md](../low-level-design/concepts/thread-safety.md): name shared state; don’t lock across I/O; prefer per-resource locks / single-writer.

## System index (practice)

Parking · Elevator · ATM · Vending · Library · Hotel · Movie · Payment · Notification · Logger · Cache · Rate limiter → [systems/](../low-level-design/systems/)

## Timed mock

[interview-prep LLD 45](../interview-prep/formats/mock-interviews/lld-45.md)

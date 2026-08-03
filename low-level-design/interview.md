# LLD Interview — Principal / Staff Rubric

## Opening script (45–60s)

“I’ll clarify single-process vs distributed, actors, and non-goals. Then I’ll list domain entities and the invariants they protect. I’ll introduce interfaces only at variation points—pricing, providers, channels—define ownership of shared state and locking, walk the critical failure path, and show how a new variant extends the design without rewriting the core.”

## Scoring

| Signal | Strong | Weak |
|--------|--------|------|
| Requirements | Explicit non-goals, scale assumption, consistency needs | Jumps to classes |
| Domain | Invariants owned by types | Anemic bags of getters |
| Interfaces | At real change points with why | Interface per class |
| Concurrency | Shared state + granularity named | `synchronized` everything |
| Errors | Typed domain failures + recovery | Swallow / generic Exception |
| Evolution | New policy = new type | Edit switch statements |
| Testing | Ports faked; deterministic clock/id | Untestable statics |
| Trade-offs | Rejected alternatives with cost | “This is best practice” |

## 40-minute timeline

| Min | Move |
|-----|------|
| 0–5 | Clarify scope, actors, single JVM assumption |
| 5–12 | Domain model + invariants |
| 12–20 | Classes, interfaces, relationships, why |
| 20–28 | Hottest use-case sequence + locking |
| 28–34 | Errors, extensibility story |
| 34–40 | Tests + trade-offs / follow-ups |

## Decision grammar (use this language)

- **Choice:** Strategy for pricing  
- **Why:** Fee rules change per city/hour without touching parking flow  
- **Rejected:** `if (type)` in `ParkingLot` — every new fee rule edits core  

## Cross-cutting traps

- God `Manager` / `Service` that owns every rule  
- Leaking Stripe/Twilio types into domain  
- Holding coarse locks across I/O (payment, SMS)  
- Ignoring idempotency on payment / booking  
- Designing distributed systems when the prompt is LLD  

## Follow-up drills interviewers love

1. Double submission (same request twice) — what happens?  
2. Add a new vehicle / channel / provider — which files change?  
3. Traffic ×10 on one hot resource — what breaks first?  
4. Partial failure after side effect — how do you reconcile?  
5. What would you **not** abstract yet, and why?

Related lenses: [concepts/solid.md](./concepts/solid.md), [concepts/thread-safety.md](./concepts/thread-safety.md), [concepts/extensibility.md](./concepts/extensibility.md).

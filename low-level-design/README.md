# Low-Level Design — Principal / Staff Interview Prep

This section is **object design under constraints**, not UML theater and not system-design (no Kafka topology unless a class needs a port).

You are scored on: what is shared, what can change, what fails, and **why** your boundaries look that way.

## How to use

1. Pick a system under [systems/](./systems/).  
2. Timebox: clarify → model → interfaces → concurrency → evolve.  
3. For every type you introduce, answer **why it exists** and **what would break if you merged it**.  
4. Rehearse aloud using [interview.md](./interview.md).

## Required design template

Every system file contains:

```text
Requirements → Use cases → Domain model → Classes → Interfaces
→ Relationships → SOLID → Design patterns → Thread safety
→ Error handling → Extensibility → Testing → Trade-offs
```

Decisions are written as **choice → why → alternative rejected**.

## Systems

| System | Core hardness |
|--------|----------------|
| [Parking Lot](./systems/parking-lot.md) | Inventory allocation + pricing strategies |
| [Elevator](./systems/elevator.md) | Scheduling state machine + concurrency |
| [ATM](./systems/atm.md) | Session state, cash dispenser, auth |
| [Vending Machine](./systems/vending-machine.md) | Finite state + money handling |
| [Library](./systems/library.md) | Borrow lifecycle, reservations |
| [Hotel](./systems/hotel.md) | Inventory over dates, overbooking policy |
| [Movie Booking](./systems/movie-booking.md) | Seat holds, payment race |
| [Payment System](./systems/payment-system.md) | Idempotency, providers, ledger |
| [Notification System](./systems/notification-system.md) | Channels, retries, fan-out |
| [Logger](./systems/logger.md) | Appenders, levels, async backpressure |
| [Cache](./systems/cache.md) | Eviction, concurrency, stampede |
| [Rate Limiter](./systems/rate-limiter.md) | Algorithms, keying, distributed port |

## Supporting concepts

[concepts/](./concepts/) — SOLID, patterns, thread safety, extensibility, testability as **decision lenses**, not checklists.

## What “good” looks like at Staff level

- Names **invariants** (a seat cannot be sold twice).  
- Puts interfaces only where **variation or substitution** is real.  
- Separates **domain rules** from **I/O adapters**.  
- States lock granularity and failure modes explicitly.  
- Shows how a new requirement plugs in **without editing callers**.

## Cross-links

| Need | Go to |
|------|--------|
| Answer spine | [../interview-prep/answer-framework.md](../interview-prep/answer-framework.md) · [../interview-prep/tracks/lld.md](../interview-prep/tracks/lld.md) |
| Timed mock | [../interview-prep/formats/mock-interviews/lld-45.md](../interview-prep/formats/mock-interviews/lld-45.md) |
| Patterns depth | [../design-patterns/](../design-patterns/) |
| Cheat sheet | [../cheat-sheets/lld.md](../cheat-sheets/lld.md) |
| Portfolio bridge | [../real-world-projects/](../real-world-projects/) (06–08) |
| SD (topology) | [../system-design/](../system-design/) |

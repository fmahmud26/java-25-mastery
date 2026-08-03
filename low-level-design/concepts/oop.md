# OOP for LLD Interviews

## What to model as types

| Kind | Examples | Why |
|------|----------|-----|
| Entity | `Booking`, `Loan`, `Payment` | Identity + lifecycle |
| Value object | `Money`, `DateRange`, `Email` | Equality by value; validate at creation |
| Aggregate root | `Booking`, `Payment` | Consistency boundary |
| Domain service | `SpotAllocator` | Logic that isn’t naturally one entity |
| Application service | `BookingService` | Use-case orchestration |
| Port | `PaymentGateway` | Outbound capability |

## Anemic vs rich

- **Rich:** `seat.hold(until)` enforces FREE→HELD  
- **Anemic:** setters everywhere; service does all checks — weaker invariant protection  

Staff preference: **invariants on the type that owns the data**.

## Composition first

“Elevator **has** Door” beats “Elevator **is** MotorController”. Inheritance for true is-a variants (`Vehicle` kinds); composition for capabilities.

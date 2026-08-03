# Sealed Classes & Interfaces

Restrict the permitted subtypes (`permits`) — closed hierarchies with **exhaustive** pattern switches.

## 1. Mental Model

```text
sealed interface PaymentEvent permits Captured, Failed, Refunded
switch (event) { case Captured c -> … }  // compile error if a permit missing
```

## 2. Problem It Solves

Domain facts have a known variant set. You want the compiler to force handling when a new variant appears — unlike open interface implementors anywhere.

## 3. Bad Design → Problems → Better Design

**Bad:** Open `interface PaymentEvent` + `default` branch that ignores unknowns; events silently dropped.

**Better:**

```java
public sealed interface PaymentEvent
        permits PaymentCaptured, PaymentFailed, PaymentRefunded {}

public record PaymentCaptured(String paymentId, long cents) implements PaymentEvent {}
public record PaymentFailed(String paymentId, String code) implements PaymentEvent {}
public record PaymentRefunded(String paymentId, long cents) implements PaymentEvent {}

String route(PaymentEvent e) {
    return switch (e) {
        case PaymentCaptured c -> "ledger.capture";
        case PaymentFailed f -> "notify.failure";
        case PaymentRefunded r -> "ledger.refund";
    };
}
```

## 4. Technical Rules (Java 25)

| Rule | Detail |
|------|--------|
| Permitted types | `final`, `sealed`, or `non-sealed` |
| Location | Same module (typically same package) |
| Nested permits | May omit `permits` if nested |
| Exhaustiveness | Switch/expressions checked |

## 5. Internal Behavior

Sealing is enforced by the compiler and reflected in class file attributes; JVM checks subclassing permissions.

## 6. Domain Scenarios

- **Inventory:** `StockCommand` sealed — `Reserve`, `Release`, `Adjust`.  
- **Logistics:** `ShipmentStatus` as sealed interface instead of free strings.

## 7. Trade-offs & When Not

Closed set fights third-party extension — use open interfaces for plugins (`PaymentPort`). Use sealed for **facts**; open for **adapters**.

## 8. Failure Scenario

Library adds `non-sealed` escape; random subtypes break exhaustiveness assumptions. Keep domain sealed types `final` permits only.

## 9. LLD Interview Scenario

Model order lifecycle events for a consumer. Sealed events vs Kafka schema registry — how evolve versions?

## 10. SOLID / Extensibility

Expression problem: sealed+switch makes adding operations easy, adding variants a deliberate compile break (often desirable for domain events).

## 11. Interview Ladder

- Why seal?  
- `final` vs `non-sealed` permitted subtype?  
- Sealed vs open interface for payments?

## 12. Principal Engineer Perspective

Seal **domain languages**; leave **infrastructure ports** open. Exhaustiveness is a production safety feature — treat new variants as release work, not silent defaults.

### Related

[records.md](./records.md) · [polymorphism.md](./polymorphism.md) · [domain-modeling.md](./domain-modeling.md) · [inheritance.md](./inheritance.md)

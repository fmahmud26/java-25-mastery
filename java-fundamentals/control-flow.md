# Control Flow

Branching: `if`, `switch`, pattern matching — choosing a path under business rules.

## 1. Mental Model

```text
request ──► rules ──► one outcome path
                 └── exhaustive switch on sealed domain
```

## 2. Simple Explanation

Control flow selects which statements run. Prefer clear conditions and **exhaustive** switches on domain enums/sealed types over deep nested `if` pyramids.

## 3. Technical Explanation

- `if` / `else if` / `else`  
- `switch` expressions (yield a value) vs statements  
- Pattern matching for `instanceof` and switch  
- Sealed hierarchies enable compiler exhaustiveness checks  
- Avoid fall-through unless intentional and commented

## 4. Internal Behavior

Compiles to conditional jumps / tableswitch / lookupswitch. Switch expressions must be exhaustive or have `default`. Missed cases become compile errors when types are sealed/enums properly designed.

## 5. Java 25 Example

```java
sealed interface PaymentEvent permits CardAuthorized, CardDeclined, RefundPosted {}
record CardAuthorized(String paymentId) implements PaymentEvent {}
record CardDeclined(String paymentId, String reason) implements PaymentEvent {}
record RefundPosted(String paymentId, long cents) implements PaymentEvent {}

String route(PaymentEvent e) {
    return switch (e) {
        case CardAuthorized a -> "ledger.capture";
        case CardDeclined d -> "notify.decline";
        case RefundPosted r -> "ledger.refund";
    }; // exhaustive — new permit without a branch fails compile
}

if (body instanceof CreateInvoice cmd && !cmd.lines().isEmpty()) {
    invoiceService.create(cmd);
}
```

## 6. Real-World Scenario

**Payment event router:** nested ifs missed a new event type → silent drop. Migrated to sealed `PaymentEvent` + switch expression; CI fails on new subtype without a branch.

## 7. Common Mistake

Non-exhaustive switches with silent `default` that swallows unknowns; deeply nested conditions no one can test.

## 8. Failure Scenario

Prod drops `ChargebackOpened` events. Detect via consumer lag + dead-letter metrics. Fix exhaustive domain modeling.

## 9. Performance Implications

Branch misprediction rarely matters vs I/O. Clarity and exhaustiveness beat micro-optimizing branch layout.

## 10. Interview Questions

- Switch expression vs statement?  
- Why sealed + switch?

## 11. Senior-Level Follow-ups

- How do you evolve event types without silent drops?  
- Guard patterns vs nested ifs?

## 12. Principal Engineer Perspective

Model **domain alternatives** so the compiler enforces completeness. Prefer explicit unknown handling at system edges (deserialize) and exhaustiveness inside the core.

### Related

[operators.md](./operators.md) · [loops.md](./loops.md) · [methods.md](./methods.md)

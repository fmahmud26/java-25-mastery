# Method Overloading

Same name, different parameter lists — resolved at **compile time** (static polymorphism).

## 1. Mental Model

```text
log(String)
log(String, Throwable)
compiler picks from argument types at the call site
```

## 2. Problem It Solves

Ergonomic APIs for related operations without inventing `logWithError` names — when differences are clear.

## 3. Bad Design → Problems → Better Design

**Bad:** `process(Order)`, `process(Order, boolean force)`, `process(Order, boolean, boolean)` flag soup; or overloads that differ only by similar boxed types.

**Problems:** Wrong overload selected; unreadable call sites; fragile after refactors.

**Better:** Named methods or a command/record parameter.

```java
// Fragile
void refund(String paymentId, long cents) { }
void refund(String paymentId, long cents, boolean notify) { }

// Clearer
public record RefundCommand(String paymentId, long cents, boolean notifyCustomer) {}
void refund(RefundCommand cmd) { }
```

## 4. Technical Rules (Java 25)

- Differ by arity or parameter types (not return type alone).  
- Resolution uses static types; autoboxing/varargs complicate specificity.  
- Inherited overloads participate in resolution.

## 5. Internal Behavior

Compile-time choice → fixed `invoke*` target for that call site (unless the chosen method is itself overridden — then dispatch applies to that method identity).

## 6. Domain Scenarios

- **Orders:** `addLine(sku, qty)` vs `addLine(OrderLine)` — OK if both obvious.  
- **Notifications:** avoid `send(userId, String)` vs `send(email, String)` — both Strings; disaster.

## 7. Trade-offs & When Not

Overloading helps libraries (e.g. `List.of`). In domain services, command objects scale better than 6 overloads.

## 8. Failure Scenario

`send("user-1", "hi")` binds to `(String email, String body)` incorrectly. Fix: distinct types (`UserId`, `Email`).

## 9. LLD Interview Scenario

Design `InventoryService.adjust` for absolute set vs delta. Overloads or separate methods `setOnHand` / `adjustBy`?

## 10. SOLID / Extensibility

Overload sets are part of public API surface — hard to shrink. Prefer additive records over boolean flags.

## 11. Interview Ladder

- Overloading vs overriding?  
- Can overloads differ only by return type?  
- How do varargs affect resolution?

## 12. Principal Engineer Perspective

Treat overloads as **API sugar**, not domain model. If call sites need a guide to pick an overload, the design failed — use types.

### Related

[polymorphism.md](./polymorphism.md) · [method-overriding.md](./method-overriding.md) · [../java-fundamentals/varargs.md](../java-fundamentals/varargs.md)

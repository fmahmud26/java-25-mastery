# Variables

Locals, fields, and parameters — scope, lifetime, definite assignment.

## 1. Mental Model

```text
method frame:  locals (amount, customerId)
instance:      fields live with the object
class:         static fields live with the Class
```

## 2. Simple Explanation

A variable names a storage location. Locals die with the method frame. Instance fields live with the object. Static fields live with the class. Locals must be definitely assigned before use.

## 3. Technical Explanation

| Kind | Lifetime | Default |
|------|----------|---------|
| Local | Frame | Must assign before read |
| Parameter | Frame | Provided by caller |
| Instance field | Object | Zero/`null`/`false` |
| Static field | Class | Same defaults |
| `var` | Local only | Infer from initializer |

## 4. Internal Behavior

Locals map to stack slots. Fields are in object/class layout. Captured locals for lambdas must be effectively final. Shadowing hides outer names — easy bug source.

## 5. Java 25 Example

```java
void settle(String paymentId, long amountCents) {
    var status = "PENDING";           // local, inferred
    // long fee; IO.println(fee);     // illegal — not definitely assigned
    this.lastPaymentId = paymentId;   // field
}
```

## 6. Real-World Scenario

**Refund service:** a field `retryCount` was static by mistake — all refunds shared one counter. Incidents showed cross-tenant “retries exceeded.” Moved to instance/request scope.

## 7. Common Mistake

Uninitialized locals; shadowing parameters with fields (`this.x = x` forgotten); mutable statics as “globals.”

## 8. Failure Scenario

Compile fail on definite assignment, or subtle production bug from static mutable state. Fix scope; add tests for isolation.

## 9. Performance Implications

Locals are cheap. Unnecessary fields extend object lifetime (GC). Huge locals arrays still allocate on heap when escaped.

## 10. Interview Questions

- Local vs instance field?  
- What is definite assignment?

## 11. Senior-Level Follow-ups

- When is `var` harmful for readability?  
- Effectively final — why?

## 12. Principal Engineer Perspective

Scope is **ownership**. Prefer locals; fields for true object state; statics only for process-wide constants or carefully managed shared resources.

### Related

[static.md](./static.md) · [final.md](./final.md) · [primitive-types.md](./primitive-types.md) · [reference-types.md](./reference-types.md)

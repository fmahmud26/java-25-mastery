# final

Immutability of **bindings** (and some classes/methods) — not automatically deep immutability of objects.

## 1. Mental Model

```text
final Customer c = ...;  // c cannot be rebound
c.mutate();              // still possible if Customer is mutable
```

## 2. Simple Explanation

`final` on a variable means the variable always refers to the same value/reference after assignment. On a method, no override. On a class, no subclass. It does **not** make a mutable object’s fields frozen unless those fields are also designed immutable.

## 3. Technical Explanation

| Target | Effect |
|--------|--------|
| Local / param | Single assignment; helps “effectively final” |
| Instance field | Blank final must be set by every ctor |
| Static field | Often `static final` constants |
| Method | No override |
| Class | No subclass (prefer carefully) |

Records already give shallow immutability of components.

## 4. Internal Behavior

Compiler enforces definite assignment for blank finals. `final` fields have special safe-publication properties under JMM when set in constructors (with caveats). JIT may constant-fold `static final` primitives/strings.

## 5. Java 25 Example

```java
public final class InvoiceId {
    private final String value;
    public InvoiceId(String value) {
        this.value = Objects.requireNonNull(value);
    }
    public String value() { return value; }
}

void process(final String paymentId) {
    // paymentId = "x"; // illegal
}
```

## 6. Real-World Scenario

**Money type:** `final class Money` with `final long cents` prevented accidental subclassing and reassignment. A `final List<Line> lines` field still needed `List.copyOf` — otherwise callers mutated the list.

## 7. Common Mistake

Believing `final List` is an immutable list; overusing `final` on every local without design benefit; sealing nothing while marking random classes final inconsistently.

## 8. Failure Scenario

“Immutable” DTO leaked a mutable `Date`/`List` field — concurrent modification. Fix defensive copies / unmodifiable views / records of immutable parts.

## 9. Performance Implications

`final` enables some JIT assumptions; not a substitute for algorithm choice. Deep copy for safety can cost — choose at boundaries.

## 10. Interview Questions

- What does `final` on a reference mean?  
- final method vs final class?

## 11. Senior-Level Follow-ups

- final fields and safe publication?  
- When prefer sealed over final class?

## 12. Principal Engineer Perspective

Use `final` to encode **invariants**. Pair with immutable *content* for thread-safe sharing. Document when a type is deeply immutable vs “final reference only.”

### Related

[static.md](./static.md) · [reference-types.md](./reference-types.md) · [access-modifiers.md](./access-modifiers.md)

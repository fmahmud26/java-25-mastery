# Constructor

A **constructor** is the only legal birth channel for a valid instance — enforce invariants before the object is visible.

## 1. Mental Model

```text
new Payment(…)  →  validate  →  assign finals  →  object escapes to caller
```

## 2. Problem It Solves

Prevent half-built objects (`amount = 0`, `currency = null`) from entering the domain and corrupting ledgers.

## 3. Bad Design → Problems → Better Design

**Bad:** No-arg ctor + 12 setters called “when convenient.”

**Problems:** Temporal coupling; invalid intermediate states; hard to test; thread publishes half-init object.

**Better:** Canonical ctor (or factory) requires all essentials; optional via builders only if truly optional; prefer records for pure data.

```java
public final class Money {
    private final long cents;
    private final String currency;

    public Money(long cents, String currency) {
        if (currency == null || currency.length() != 3)
            throw new IllegalArgumentException("currency");
        this.cents = cents;
        this.currency = currency;
    }

    public static Money zero(String currency) {
        return new Money(0, currency);
    }
}
```

## 4. Technical Rules (Java 25)

- Name = class name; no return type.  
- If none declared → default no-arg (only if no other ctor).  
- Subclass must chain `super(...)`.  
- Can overload; delegate with `this(...)`.  
- Records: canonical + compact ctor for validation.

## 5. Internal Behavior

Allocation then `<init>`. `this` escapes only after superclass ctors finish — leaking `this` from ctor to other threads is a publication bug. Instance initializers run as part of construction.

## 6. Domain Scenarios

- **Order:** ctor requires non-empty lines; status starts `CREATED`.  
- **Notification:** `EmailMessage` ctor validates address format once.

## 7. Trade-offs & When Not

Frameworks may need no-arg (JPA) — keep a protected no-arg for tooling and a public domain factory that validates. Don’t let the no-arg become the public API.

## 8. Failure Scenario

Symptom: NPE deep in tax calc. Cause: deserialized/order built via setters skipped currency. Fix: compact ctor / factory only; fail at boundary.

## 9. LLD Interview Scenario

Design construction of `WireTransfer` needing account, amount, FX rate. Where validate FX? Ctor vs domain service?

## 10. SOLID / Extensibility

Constructors shouldn’t start threads, hit network, or register listeners (hard to test, failure mid-init). Inject collaborators after or via factory used by composition root.

## 11. Interview Ladder

- Are constructors inherited?  
- `this()` vs `super()`?  
- Why avoid work in constructors?

## 12. Principal Engineer Perspective

Construction is an **API**. Invalid objects must be unrepresentable. Prefer fail-fast at birth over “fix later” in every method.

### Related

[constructor-chaining.md](./constructor-chaining.md) · [this.md](./this.md) · [initialization-blocks.md](./initialization-blocks.md) · [records.md](./records.md)

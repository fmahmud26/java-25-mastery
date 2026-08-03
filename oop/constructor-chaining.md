# Constructor Chaining

Delegate construction with `this(...)` / `super(...)` so validation and defaults live in one place.

## 1. Mental Model

```text
Money.usd(cents) → this(cents, "USD") → canonical checks
Subclass → super(id) → parent canonical
```

## 2. Problem It Solves

Avoid copy-pasted validation across overloads and subclasses.

## 3. Bad Design → Problems → Better Design

**Bad:** Three public ctors each half-validating differently.

**Better:** One canonical ctor; others chain; factories for readability.

```java
public final class Account {
    private final String id;
    private final String currency;
    private long balanceCents;

    public Account(String id, String currency, long openingCents) {
        this.id = Objects.requireNonNull(id);
        this.currency = Objects.requireNonNull(currency);
        if (openingCents < 0) throw new IllegalArgumentException("opening");
        this.balanceCents = openingCents;
    }

    public Account(String id, String currency) {
        this(id, currency, 0L);
    }
}
```

## 4. Technical Rules (Java 25)

- `this`/`super` ctor invocation restrictions — keep chaining simple and first.  
- Records: compact ctor wraps canonical.  
- Don’t mix heavy logic in every overload — chain to one.

## 5. Internal Behavior

Bytecode shows chained `<init>` calls. Each level runs initializers appropriately.

## 6. Domain Scenarios

- **Orders:** `Order(cart)` chains to canonical `(id, lines, status)`.  
- **Logistics:** `Shipment.international(...)` factory chains to base ctor with customs flags.

## 7. Trade-offs & When Not

Too many public ctors confuse overload resolution — prefer factories with intent names (`openDomestic`, `openFx`).

## 8. Failure Scenario

Subclass chains wrong `super` overload → null currency. Tests for all public construction paths.

## 9. LLD Interview Scenario

Design construction API for `WireTransfer` with optional FX — chaining vs builder vs record command?

## 10. SOLID / Extensibility

Factories can return subtypes (interface return type) without exposing many ctors — DIP-friendly.

## 11. Interview Ladder

- `this()` vs `super()`?  
- Are constructors inherited?  
- Canonical ctor pattern?

## 12. Principal Engineer Perspective

One **validation funnel** per type. Public construction paths are API — keep them few, named, and tested.

### Related

[constructor.md](./constructor.md) · [this.md](./this.md) · [super.md](./super.md)

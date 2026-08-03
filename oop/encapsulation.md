# Encapsulation

Hide representation; expose **operations that preserve invariants**. Access modifiers are the mechanism; design is the point.

## 1. Mental Model

```text
outside  ──►  public behavior API
inside   ──►  private fields + checks
```

## 2. Problem It Solves

Callers must not bypass rules (negative balance, shipped-then-cancelled) by writing fields directly.

## 3. Bad Design → Problems → Better Design

**Bad:** Public fields or getter+setter for every field on `BankAccount`.

**Problems:** Invariants scatter; auditing impossible; “anemic” domain that any layer can corrupt.

**Better:** Behavior methods; return unmodifiable views; no setter for balance.

```java
public final class BankAccount {
    private final String accountId;
    private long balanceCents;

    public BankAccount(String accountId, long openingCents) {
        if (openingCents < 0) throw new IllegalArgumentException("opening");
        this.accountId = accountId;
        this.balanceCents = openingCents;
    }

    public void withdraw(long cents) {
        if (cents <= 0) throw new IllegalArgumentException("cents");
        if (cents > balanceCents) throw new InsufficientFundsException(accountId);
        balanceCents -= cents;
    }

    public long balanceCents() { return balanceCents; }
}
```

## 4. Technical Rules (Java 25)

`private` / package / `protected` / `public` + module `exports`. Prefer package-private types for internal domain. Records encapsulate components as `private final` with accessors — still validate in compact ctor.

## 5. Internal Behavior

Language checks at compile time; reflection can bypass if module opens — treat `opens` as debt. Encapsulation is not security against a malicious same-JVM peer with deep reflection.

## 6. Domain Scenarios

- **Inventory:** `reserve`/`commit`/`release` instead of `setQty`.  
- **Payments:** status transitions only via `capture`/`void`/`refund`.

## 7. Trade-offs & When Not

Over-encapsulation (everything private + no seams) hurts testing — package-private or ports for test doubles. Don’t expose mutable internals “for JSON mapping” without a DTO boundary.

## 8. Failure Scenario

Symptom: order cancelled after shipment. Cause: controller set `status = CANCELLED`. Fix: `cancel()` checks `!SHIPPED`. Prevent: no status setter; enum + methods.

## 9. LLD Interview Scenario

Design `Shipment` encapsulation: who can mark `DELIVERED`? External webhook vs internal job? How prevent illegal reverse transitions?

## 10. SOLID / Extensibility

Encapsulation enables OCP: change storage of money (cents vs Money object) without callers knowing. Leaking lists breaks that — always copy or unmodifiable wrap at the boundary.

## 11. Interview Ladder

- Encapsulation vs information hiding?  
- Why not auto-generate setters?  
- How do modules strengthen encapsulation?

## 12. Principal Engineer Perspective

Encapsulation is **invariant ownership**. If any layer can mutate freely, you don’t have a model — you have a shared mutable bag. Prefer rich methods; keep DTOs at edges.

### Related

[immutability.md](./immutability.md) · [class.md](./class.md) · [domain-modeling.md](./domain-modeling.md) · [../java-fundamentals/access-modifiers.md](../java-fundamentals/access-modifiers.md)

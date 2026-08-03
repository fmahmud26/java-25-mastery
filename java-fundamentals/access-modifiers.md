# Access Modifiers

Who can see a type or member — language rules, then JPMS on top.

## 1. Mental Model

```text
private < package-private < protected < public
(+ module must export the package for other modules)
```

## 2. Simple Explanation

`private` = this class. Default = this package. `protected` = package + subclasses. `public` = everywhere (subject to modules). Prefer the **narrowest** visibility that works.

## 3. Technical Explanation

| Modifier | Type | Member |
|----------|------|--------|
| `public` | Everywhere* | Everywhere* |
| `protected` | — | Package + subclasses |
| *(default)* | Package | Package |
| `private` | Nested only | Declaring class |

\*Other modules need `exports` (and readability). Nested classes can be `private`.

## 4. Internal Behavior

Compiler enforces visibility. Reflection can bypass language rules when the module **opens** the package — intentional debt.

## 5. Java 25 Example

```java
package com.acme.billing.domain;

public final class LedgerEntry {
    private final long amountCents;
    LedgerEntry(long amountCents) { this.amountCents = amountCents; } // package ctor
    public long amountCents() { return amountCents; }
}
```

## 6. Real-World Scenario

**Ledger domain:** only package factories create `LedgerEntry`. Controllers can’t `new` invalid entries. A “quick” public ctor once allowed negative balances in staging.

## 7. Common Mistake

Everything `public` “for Spring,” or using `protected` when package-private would do.

## 8. Failure Scenario

Compile error “not visible” after modularization — export was too wide before, or API leaked internals. Narrow exports; expose ports.

## 9. Performance Implications

None. Wrong visibility creates coupling and security/reflection footguns.

## 10. Interview Questions

- Difference between default and `protected`?  
- Can another module use a `public` class in a non-exported package?

## 11. Senior-Level Follow-ups

- How do you design a stable library API surface?  
- When is `opens` acceptable?

## 12. Principal Engineer Perspective

Visibility is **API design**. Shrink public surface; treat openings for frameworks as reviewed debt.

### Related

[packages.md](./packages.md) · [modules.md](./modules.md) · [final.md](./final.md)

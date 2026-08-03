# Modern Coding Style (Java 25)

How a Principal-level codebase should look after migrating off legacy Java 8 habits.

## Problem Before

Anonymous classes, mutable DTOs, stringly switches, `null` returns, `Date`, deep inheritance, and `instanceof` forests — all legal, all costly.

## The Style (Target)

```text
Values        → records (+ validation)
Absence       → Optional returns (not fields)
Variants      → sealed + pattern switch
Multi-line    → text blocks
Locals        → var when obvious; _ when unused
Reuse         → composition + ports
Callbacks     → lambdas / method refs
Time          → java.time
Collections   → of/copyOf at boundaries
```

## Before → After (service sketch)

```java
// Legacy flavor
public class PaymentService {
    public String handle(Object event) {
        if (event instanceof PaymentCaptured) {
            PaymentCaptured e = (PaymentCaptured) event;
            return ledger.write(e.getId(), e.getCents());
        }
        if (event instanceof PaymentFailed) {
            PaymentFailed e = (PaymentFailed) event;
            mail.send(e.getId());
            return "failed";
        }
        return "unknown";
    }
}
```

```java
// Java 25 flavor
public final class PaymentService {
    private final LedgerPort ledger;
    private final NotificationPort notify;

    public PaymentService(LedgerPort ledger, NotificationPort notify) {
        this.ledger = ledger;
        this.notify = notify;
    }

    public String handle(PaymentEvent event) {
        return switch (event) {
            case PaymentCaptured(String id, long cents, Instant _) ->
                    ledger.write(id, cents);
            case PaymentFailed(String id, String code) -> {
                notify.paymentFailed(id, code);
                yield "failed";
            }
            case PaymentRefunded(String id, long cents) ->
                    ledger.refund(id, cents);
        };
    }
}

public sealed interface PaymentEvent permits PaymentCaptured, PaymentFailed, PaymentRefunded { }
public record PaymentCaptured(String id, long cents, Instant at) implements PaymentEvent { }
public record PaymentFailed(String id, String code) implements PaymentEvent { }
public record PaymentRefunded(String id, long cents) implements PaymentEvent { }
```

## Production Usage

Encode style in Checkstyle/Error Prone SpotBugs + review checklist. Migrate by hotspot (payments/orders first).

## Trade-offs

Consistency beats purity. Don’t rewrite stable modules without a change driver. Preview features stay behind flags.

## When NOT to “Modernize”

- Generated/thrift models  
- Framework entities that need mutable beans — map at boundary  
- Ultra-hot loops where a simple `for` is clearer and faster (profile)

## Migration Decisions (checklist)

| Step | Action |
|------|--------|
| 1 | Toolchain 25 + `--release 25` |
| 2 | `java.time` + collection factories |
| 3 | DTO records |
| 4 | Text blocks for SQL/JSON fixtures |
| 5 | Sealed events + pattern switch |
| 6 | `var`/`_` per style guide |
| 7 | Optional returns on finds |
| 8 | JPMS/`jlink` only if packaging ROI clear |

## Interview Questions

- What does “data-oriented Java” mean to you?  
- Show a before/after for event routing.  
- What would you ban in code review on a Java 25 service?  
- How do you migrate a 11 → 25 monolith without a big-bang?

### Related

[java-evolution.md](./java-evolution.md) · [records.md](./records.md) · [sealed-classes.md](./sealed-classes.md) · [interview.md](./interview.md)

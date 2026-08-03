# Sealed Classes

## Purpose

Close a hierarchy so the compiler (and readers) know **all** subtypes — exhaustiveness in `switch`.

## Before

```java
interface PaymentResult {} // open — any jar can implement
// switch must have default forever; new cases silent
```

## After

```java
public sealed interface PaymentResult permits Paid, Declined, Pending {
}

public record Paid(String txId) implements PaymentResult {}
public record Declined(String reason) implements PaymentResult {}
public record Pending(String retryId) implements PaymentResult {}

String msg(PaymentResult r) {
    return switch (r) {
        case Paid p -> "ok " + p.txId();
        case Declined d -> "no: " + d.reason();
        case Pending p -> "wait " + p.retryId();
    };
}
```

## Trade-offs

| Gain | Cost |
|------|------|
| Exhaustive handling | Evolution needs coordinated release |
| Domain clarity | Wrong for SPI open plugins |

## PE Decision

Seal **domain outcomes** you own; keep SPI interfaces open for plugins.

### Related

[records.md](./records.md) · [error-handling.md](./error-handling.md) · [domain-modeling.md](./domain-modeling.md)

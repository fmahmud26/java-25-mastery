# Error Handling

## Purpose

Make failure **explicit, actionable, and safe** — for users and operators.

## Before

```java
catch (Exception e) {
    return null; // or log and continue
}
```

## After

```java
public sealed interface ChargeResult permits Succeeded, Declined, SoftDecline {}

public ChargeResult charge(ChargeCommand cmd) {
    try {
        return gateway.charge(cmd);
    } catch (TimeoutException e) {
        metrics.timeout(cmd.psp());
        return new SoftDecline("timeout"); // caller may retry with idempotency
    }
}
```

API layer:

```java
// map domain errors → Problem Details / stable error codes
// never leak stack traces to clients
```

## Policies

| Kind | Approach |
|------|----------|
| Programmer bugs | Fail fast; fix |
| Expected business | Typed results / domain exceptions |
| Transient IO | Retry with policy |
| Authz | 403, audit |

## Trade-offs

Checked exceptions (verbose) vs unchecked (easy to miss) — pick a team standard. Result types vs exceptions for expected flows.

## PE Decision

Error **taxonomy** documented; money paths never swallow exceptions.

### Related

[sealed-classes.md](./sealed-classes.md) · [defensive-programming.md](./defensive-programming.md) · [logging.md](./logging.md)

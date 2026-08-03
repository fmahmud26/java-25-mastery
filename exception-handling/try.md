# try

Starts a protected block — paired with `catch` and/or `finally`, or try-with-resources.

## Mental Model

```text
try { risky work } → transfer to catch on throw → finally always (almost)
```

Prefer **small try blocks** around the actual failure point, not entire methods.

## Bad vs Improved

```java
// Bad — mega-try hides which call failed
try {
    validate(cmd);
    Order o = db.load(cmd.orderId());
    psp.capture(cmd);
    db.save(o);
    email.send(o);
} catch (Exception e) {
    log.error("failed", e);
}

// Improved — narrow tries or let translation layers handle
validate(cmd);
Order o = orderRepo.getRequired(cmd.orderId());
CaptureResult r = paymentPort.capture(cmd); // port already translates
…
```

## Production — network timeout

```java
try {
    return http.send(request, BodyHandlers.ofString());
} catch (HttpTimeoutException e) {
    throw new ThirdPartyTimeoutException("psp", e);
}
```

## Strategy

Don’t use try as a substitute for validation. Don’t catch unless you log/translate/recover meaningfully.

### Related

[catch.md](./catch.md) · [finally.md](./finally.md) · [try-with-resources.md](./try-with-resources.md)

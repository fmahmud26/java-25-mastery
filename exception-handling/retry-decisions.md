# Retry Decisions

Not every exception should be retried. Wrong retries cause **duplicate charges** and thundering herds.

## Mental Model

```text
retry only if: transient AND idempotent (or safe to dedupe) AND budget remains
```

## Retry vs Don’t

| Retry (usually) | Do not retry |
|-----------------|--------------|
| Timeouts, connection reset | Validation errors |
| HTTP 429 / 503 (with Respect Retry-After) | HTTP 400/401/403/404 |
| Transient SQL deadlock/timeout (careful) | Unique constraint (business) |
| PSP “try again” codes | Hard declines (insufficient funds) |

## Production — payment + network

```java
RetryPolicy policy = RetryPolicy.builder()
        .maxAttempts(3)
        .exponentialBackoff(100, 2.0)
        .jitter(0.2)
        .handle(PaymentTransientException.class)
        .abortOn(PaymentRejectedException.class)
        .build();

return policy.execute(() -> paymentPort.capture(cmd.withIdempotencyKey(key)));
```

Without **idempotency keys**, retrying capture is dangerous even on timeouts (unknown outcome).

## Bad vs Improved

```java
// Bad
for (int i = 0; i < 10; i++) {
    try { return psp.capture(cmd); }
    catch (Exception e) { /* retry everything */ }
}

// Improved — classify, bound, idempotent, metric on exhaust
```

## Database / file / third-party

- **DB:** retry transient; abort on data errors; don’t retry whole business txn blindly.  
- **File:** rarely retry parse of corrupt file; retry read on NFS timeout.  
- **Third-party:** per-status policy; circuit-break consecutive failures.

## Principal — Resilience

Retries amplify load. Pair with **timeouts**, **bulkheads**, **circuit breakers**, **rate limits**, and **hedged** requests only when understood. Exhaustion → propagate failure + alert, don’t loop forever.

### Related

[error-handling-strategy.md](./error-handling-strategy.md) · [exception-translation.md](./exception-translation.md) · [custom-exceptions.md](./custom-exceptions.md)

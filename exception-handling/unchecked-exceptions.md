# Unchecked Exceptions (RuntimeException)

`RuntimeException` and subclasses — **not** checked by the compiler. Default choice for most application/domain failures.

## Mental Model

```text
Unchecked = programming errors OR translated domain/infrastructure failures
NPE, IAE, IllegalState, custom PaymentFailedException
```

## Production Scenarios

| Scenario | Unchecked signal | Notes |
|----------|------------------|-------|
| Payment decline as bug path | Prefer **result type** for expected declines | Exception for unexpected PSP protocol failure |
| Invalid request | `IllegalArgumentException` / `ValidationException` | 400 at HTTP edge |
| Missing order | `OrderNotFoundException` | 404 mapping |
| Bug null deref | NPE | Fix code; don’t catch-and-ignore |

## Bad vs Improved

```java
// Bad — business decline as exception for control flow only
throw new RuntimeException("declined");

// Improved
return CaptureResult.declined("INSUFFICIENT_FUNDS");

// Still use exception for unexpected
throw new PaymentGatewayException(paymentId, cause);
```

## Strategy

Use unchecked for domain exceptions so service layers stay clean. Document failure modes. Map to HTTP/gRPC codes at the edge.

## Principal Discussion

Overusing unchecked for **expected** outcomes (not found, declined) can be fine if consistent — but many Principals prefer `Optional` / `Result` for expected absences and exceptions for broken contracts. Pick one style per codebase.

### Related

[checked-exceptions.md](./checked-exceptions.md) · [custom-exceptions.md](./custom-exceptions.md) · [error-handling-strategy.md](./error-handling-strategy.md)

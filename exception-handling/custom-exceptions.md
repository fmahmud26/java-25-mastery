# Custom Exceptions

Domain/API-specific types that carry **context** and preserve **cause**.

## Mental Model

```text
Name = what failed
Fields = paymentId / orderId / errorCode
Cause = underlying IOException/SQLException/client error
```

## Design Rules

| Rule | Detail |
|------|--------|
| Extend | `RuntimeException` (typical app) or `Exception` (rare forced handling) |
| Never extend | `Error` |
| Constructors | `(msg)`, `(msg, cause)`, optionally fields-only |
| Immutable | Final fields |
| Hierarchy | Shallow — `PaymentException` → transient vs permanent subtypes if needed |

## Production Examples

```java
public final class PaymentTransientException extends RuntimeException {
    private final String paymentId;
    public PaymentTransientException(String paymentId, Throwable cause) {
        super("transient payment failure: " + paymentId, cause);
        this.paymentId = paymentId;
    }
    public String paymentId() { return paymentId; }
}

public final class ThirdPartyApiException extends RuntimeException {
    private final String dependency;
    private final int status;
    public ThirdPartyApiException(String dependency, int status, String body, Throwable cause) {
        super("%s failed status=%d body=%s".formatted(dependency, status, abbreviate(body)), cause);
        this.dependency = dependency;
        this.status = status;
    }
}
```

## Bad vs Improved

```java
// Bad
throw new Exception("fail");

// Bad wrap
catch (SQLException e) { throw new RuntimeException("db"); } // lost cause + state

// Improved
catch (SQLException e) { throw new OrderAccessException(orderId, e); }
```

## Strategy

Separate **retryable** vs **not** with types or error codes (`PaymentTransientException` vs `PaymentRejectedException`). Map at HTTP layer.

## Principal Discussion

Too many custom exceptions ≈ noise; one mega `AppException` ≈ no signal. Prefer a small taxonomy aligned to **SLO burn and retry policy**.

### Related

[exception-translation.md](./exception-translation.md) · [retry-decisions.md](./retry-decisions.md) · [throw.md](./throw.md)

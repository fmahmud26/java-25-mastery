# Logging & Observability

Failures you can’t see aren’t handled — they’re deferred incidents.

## What to Log

| Include | Exclude / careful |
|---------|-------------------|
| Correlation / trace id | Full card PANs, passwords |
| paymentId / orderId | Huge response bodies (truncate) |
| Exception type + message + stack | Logging then swallowing without metric |
| Dependency name + status | Different log text per instance with no structure |

Log the exception object (`log.error("capture failed paymentId={}", id, ex)`) so stacks and suppressed print.

## Bad vs Improved

```java
// Bad
catch (Exception e) {
    System.out.println(e.getMessage()); // no stack, no id
}

// Improved
catch (PaymentTransientException e) {
    metrics.increment("payment.transient", "dependency", "psp");
    log.warn("capture transient paymentId={}", e.paymentId(), e);
    throw e;
}
```

## Metrics & Tracing

- Counters by failure class: `transient`, `rejected`, `db_timeout`, `file_io`  
- Latency histograms including failed calls  
- Trace span status = error with exception recorded  
- Alert on rate of 5xx / DLQ depth / retry exhaustion  

## Principal Discussion

Observability requirements drive exception **types** (cardinality of metrics labels). If everything is `RuntimeException("fail")`, dashboards are useless. Standardize error codes at the edge (Problem Details / gRPC status).

### Related

[error-handling-strategy.md](./error-handling-strategy.md) · [suppressed-exceptions.md](./suppressed-exceptions.md)

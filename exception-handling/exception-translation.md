# Exception Translation

Convert low-level exceptions into **layer-appropriate** types without losing the cause chain.

## Mental Model

```text
SQLException → OrderAccessException
SocketTimeoutException → PaymentTransientException
Http 502 body → ThirdPartyApiException
```

Callers depend on stable domain types, not JDBC/PSP SDKs.

## Bad vs Improved — database / third-party

```java
// Bad — leak SQLException / vendor types in service signature
public Order save(Order o) throws SQLException { ... }

// Improved — persistence adapter
public Order save(Order o) {
    try {
        return dao.insert(o);
    } catch (SQLException e) {
        throw translate(e, o.id());
    }
}

private static RuntimeException translate(SQLException e, OrderId id) {
    if (isTimeout(e)) return new TransientDataAccessException(id, e);
    if (isUniqueViolation(e)) return new DuplicateOrderException(id, e);
    return new OrderAccessException(id, e);
}
```

```java
// HTTP client adapter
catch (HttpTimeoutException e) {
    throw new ThirdPartyTimeoutException("tax-api", e);
} catch (HttpConnectException e) {
    throw new ThirdPartyTimeoutException("tax-api", e);
}
```

## Strategy Rules

1. Always pass **cause**.  
2. Add **ids** and dependency names.  
3. Classify **retryable** at translation time when possible.  
4. Don’t translate into a generic `RuntimeException("error")`.  
5. Don’t catch broad then wrap — catch specific vendor failures.

## Principal / Observability

Translation is where you attach **error codes** for metrics (`sql_state`, `http_status`, `psp_code`). Keep raw body truncated in logs (PII!).

### Related

[custom-exceptions.md](./custom-exceptions.md) · [exception-propagation.md](./exception-propagation.md) · [logging-and-observability.md](./logging-and-observability.md)

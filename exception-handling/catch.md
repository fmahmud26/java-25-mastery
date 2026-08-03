# catch

Handles a thrown exception of matching type (exact or subtype). Order: **more specific first**.

## Mental Model

```text
catch (Specific e) { recover/translate }
catch (Broader e)  { fallback }
```

Empty catch ≈ production bug. Catch-and-log-only without rethrow/metric often hides outages.

## Bad vs Improved — database failure

```java
// Bad
try {
    return repo.save(order);
} catch (Exception e) {
    return order; // pretends success
}

// Improved
try {
    return jdbc.insert(order);
} catch (SQLException e) {
    metrics.dbFail(e.getSQLState());
    if (isTransient(e)) throw new TransientDataAccessException(e);
    throw new OrderPersistException(order.id(), e);
}
```

## Production — payment / third-party

```java
} catch (PspTimeoutException e) {
    // candidate for retry higher up
    throw e;
} catch (PspDeclinedException e) {
    // do not retry — business outcome
    return CaptureResult.declined(e.code());
}
```

## Multi-catch

```java
} catch (NoSuchFileException | AccessDeniedException e) {
    throw new FileFailureException(path, e);
}
```

See [multi-catch.md](./multi-catch.md).

## Strategy

Catch to **add context**, **classify**, **recover**, or **map to API**. Otherwise propagate.

## Principal / Observability

Every catch should answer: metric? log level? retryable? user message? cause preserved?

### Related

[try.md](./try.md) · [exception-translation.md](./exception-translation.md) · [logging-and-observability.md](./logging-and-observability.md)

# Throwable Hierarchy

```text
Throwable
├── Error                 // JVM / environment — rarely catch
└── Exception
    ├── RuntimeException  // unchecked
    └── other Exception   // checked (IOException, SQLException, …)
```

## Mental Model

| Branch | Meaning for app code |
|--------|----------------------|
| **Error** | Process likely unhealthy — log/metrics, don’t “handle” as business |
| **Checked Exception** | Compiler forces acknowledgment (often I/O) |
| **RuntimeException** | Unchecked — domain bugs, validation, translated infrastructure |

## Exception vs RuntimeException vs Error

| Type | Catch in services? | Example |
|------|--------------------|---------|
| `Exception` (checked) | At boundary / wrap | `IOException`, `SQLException` |
| `RuntimeException` | When you can recover or translate | `IllegalArgumentException`, custom domain |
| `Error` | Almost never | `OutOfMemoryError` |

`Throwable` is the root — avoid `catch (Throwable)` except at the outermost thread boundary (with care).

## Production Mapping

```text
PSP timeout        → network client exception → PaymentTransientException (unchecked)
Card declined      → not an exception — PaymentResult.DECLINED
SQLTimeout         → DataAccessException → maybe retry
Corrupt file       → IOException → quarantine + alert
OOM during report  → Error → fail job, shed load, don’t catch-and-continue
```

## Bad vs Improved

```java
// Bad — swallow everything
try { pay(cmd); } catch (Exception e) { }

// Improved — boundary catch + translate + signal
try {
    return psp.capture(cmd);
} catch (SocketTimeoutException e) {
    metrics.timeout();
    throw new PaymentTransientException(cmd.paymentId(), e);
} catch (PspBusinessException e) {
    return CaptureResult.declined(e.code());
}
```

## Principal / Observability

Hierarchy is how you **classify** failures for dashboards (transient vs permanent vs programming). Catching `Exception` everywhere destroys that signal.

### Related

[errors.md](./errors.md) · [checked-exceptions.md](./checked-exceptions.md) · [unchecked-exceptions.md](./unchecked-exceptions.md)

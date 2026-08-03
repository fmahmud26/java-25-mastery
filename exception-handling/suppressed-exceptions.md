# Suppressed Exceptions

When try-with-resources `close()` fails **after** the `try` body threw, the close exception is attached via `Throwable.addSuppressed` — primary exception still propagates.

## Mental Model

```text
primary: parse failed
suppressed[0]: close failed
→ catch sees primary; always log getSuppressed()
```

## What Happens

```java
try (var in = new FailingCloseInputStream()) {
    throw new IOException("parse failed");
} catch (IOException e) {
    // e.getMessage() → parse failed
    // e.getSuppressed() may hold close failure
    for (Throwable s : e.getSuppressed()) {
        log.warn("suppressed while closing", s);
    }
    throw e;
}
```

## Production — file failure

Primary: corrupt payment file. Suppressed: NFS close error. If you only log `e.getCause()` you miss close issues — log full exception (frameworks usually print suppressed).

## Strategy

Never discard suppressed in custom wrappers — use `initCause` carefully; prefer constructors `(msg, cause)` and let TWR attach suppressed automatically.

## Principal / Observability

Incident tools must show suppressed chains. Custom `catch` that does `throw new X(e.getMessage())` **drops** suppressed and cause — forbidden in review.

### Related

[try-with-resources.md](./try-with-resources.md) · [logging-and-observability.md](./logging-and-observability.md)

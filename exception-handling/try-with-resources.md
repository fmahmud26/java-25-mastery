# Try-with-resources

Automatically closes `AutoCloseable` resources. Preferred cleanup for files, streams, JDBC statements, HTTP bodies.

## Mental Model

```text
try (open resources) { use }
→ close in reverse order
→ close failures suppressed onto primary exception
```

## Production — file / DB

```java
try (var reader = Files.newBufferedReader(path)) {
    return parsePayments(reader);
} catch (IOException e) {
    throw new FileFailureException(path, e);
}

try (Connection c = ds.getConnection();
     PreparedStatement ps = c.prepareStatement(sql)) {
    …
}
```

Java 9+: `try (existingFinalResource) { … }`.

## Bad vs Improved — network response body

```java
// Bad — leak body stream
var resp = client.send(req, BodyHandlers.ofInputStream());
return parse(resp.body()); // never closed

// Improved — handler that consumes/closes, or TWR if you own Closeable
```

## Strategy

All `Closeable` acquisition should be TWR-scoped. Don’t mix manual close + TWR carelessly.

## Principal / Resilience

Resource leaks → FD exhaustion → cascading timeouts. TWR is a reliability control, not sugar.

### Related

[suppressed-exceptions.md](./suppressed-exceptions.md) · [finally.md](./finally.md)

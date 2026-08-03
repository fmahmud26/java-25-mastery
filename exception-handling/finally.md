# finally

Block that runs on exit from `try` (normal, return, or exception) — historically for cleanup. Prefer [try-with-resources](./try-with-resources.md) for `AutoCloseable`.

## Mental Model

```text
try → maybe catch → finally (cleanup)
```

Does **not** run on JVM kill / `System.exit` / power loss.

## Bad vs Improved — file failure

```java
// Bad — easy to leak on early return mistakes in complex code
InputStream in = Files.newInputStream(path);
try {
    return parse(in);
} finally {
    in.close(); // close exception can mask primary — ugly
}

// Improved
try (var in = Files.newInputStream(path)) {
    return parse(in);
}
```

## Strategy

Use `finally` for non-close cleanup (clear ThreadLocal, stop timer). Never swallow exceptions inside `finally` without care — can mask the primary failure.

```java
} finally {
    try { metrics.stop(); } catch (RuntimeException ex) {
        log.warn("metrics stop failed", ex);
    }
}
```

## Principal Note

Masking in `finally` destroys incident timelines. Preserve primary exception; treat cleanup failures as suppressed or separate logs.

### Related

[try-with-resources.md](./try-with-resources.md) · [suppressed-exceptions.md](./suppressed-exceptions.md)

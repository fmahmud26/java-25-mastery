# Path (`java.nio.file.Path`)

Immutable location on a filesystem — join, normalize, relativize. **No I/O by itself.**

## Mental Model

```text
Path.of("data/logs/app.log")
resolve / relativize / normalize
→ pass to Files.* for actual I/O
```

## Java 25 Examples

```java
Path log = Path.of("var", "log", "payments", "app.log");
Path abs = log.toAbsolutePath().normalize();

Path inbox = Path.of("/data/inbox");
Path file = inbox.resolve("batch-2026-08-03.csv"); // /data/inbox/batch-...
Path rel = inbox.relativize(file);                 // batch-2026-08-03.csv

// Safe join — prevent path traversal from user input
Path root = Path.of("/data/inbox").toAbsolutePath().normalize();
Path user = root.resolve(userSupplied).normalize();
if (!user.startsWith(root)) {
    throw new SecurityException("path escape");
}
```

## Production — file ingestion

Build paths from config base dir + dated partitions: `base.resolve(date).resolve(filename)`.

## Failure Scenario

Forgetting `normalize` + `startsWith` check → `../../etc/passwd` style escape on uploads.

### Related

[files.md](./files.md) · [directory-traversal.md](./directory-traversal.md) · [large-files-and-memory.md](./large-files-and-memory.md)

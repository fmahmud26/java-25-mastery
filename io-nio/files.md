# Files (`java.nio.file.Files`)

Primary NIO.2 façade — read/write/copy/move/delete/attributes/walk/lines.

## Mental Model

```text
Files.*(Path, options…) → throws IOException
Small files: readString / writeString
Large files: lines / newBufferedReader / newInputStream / channels
```

## Java 25 Examples

```java
Path path = Path.of("out", "report.txt");
Files.createDirectories(path.getParent());
Files.writeString(path, "ok\n", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

String small = Files.readString(Path.of("config.json")); // OK for tiny config

try (var lines = Files.lines(Path.of("var/log/app.log"))) {
    long errors = lines.filter(l -> l.contains("ERROR")).count();
}

Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
Files.move(tmp, finalPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
```

## Large-file rule

| API | Use when |
|-----|----------|
| `readAllBytes` / `readString` / `readAllLines` | KB–low MB only |
| `lines` / buffered streams | Line-oriented GB logs |
| `newInputStream` + buffer / `FileChannel` | Binary / high throughput |

## Production — backup / batch

```java
Files.walk(Path.of("/data/payments"))
        .filter(Files::isRegularFile)
        .filter(p -> p.toString().endsWith(".csv"))
        .forEach(p -> copyWithChecksum(p, backupRoot.resolve(p.getFileName())));
```

## Failure Scenarios

| Symptom | Cause | Fix |
|---------|-------|-----|
| OOM | `readAllBytes` on 8GB log | Stream |
| `AtomicMoveNotSupportedException` | Cross-filesystem move | Copy+delete or same volume staging |
| Leaked FD | `Files.lines` not closed | try-with-resources |
| Partial file seen by consumers | Write in place | Write temp → `ATOMIC_MOVE` |

### Related

[path.md](./path.md) · [streams.md](./streams.md) · [large-files-and-memory.md](./large-files-and-memory.md) · [directory-traversal.md](./directory-traversal.md)

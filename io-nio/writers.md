# Writers (`Writer` / `BufferedWriter`)

Character output with charset encoding.

## Mental Model

```text
chars → CharsetEncoder → bytes → file
buffer + flush/close; write temp then atomic move for safe publish
```

## Java 25 Examples

```java
Path out = Path.of("reports", "daily.csv");
Files.createDirectories(out.getParent());

Path tmp = out.resolveSibling(out.getFileName() + ".tmp");
try (BufferedWriter w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
    w.write("paymentId,cents,status");
    w.newLine();
    for (PaymentRow row : rows) {
        w.write(row.toCsv());
        w.newLine();
    }
}
Files.move(tmp, out, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
```

## Production — batch reporting

Write `.tmp` then atomic move so readers never see partial CSV.

## Failure Scenario

Crash mid-write without temp pattern → truncated file consumed by downstream. Disk full on `write` → `IOException`; leave tmp for retry.

### Related

[readers.md](./readers.md) · [buffered-io.md](./buffered-io.md) · [files.md](./files.md)

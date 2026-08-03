# Large Files & Memory

How to process GB–TB inputs without OOMing.

## Rules

| Never on huge files | Prefer |
|---------------------|--------|
| `Files.readAllBytes` | Chunked `InputStream` / `FileChannel` |
| `readString` / `readAllLines` | `BufferedReader` / `Files.lines` |
| `walk().toList()` entire NAS | Incremental visitor / streaming walk |
| Map entire 100GB file | Windowed `map()` slices or sequential read |

## Patterns

### 1) Line-oriented logs

```java
try (var lines = Files.lines(path, StandardCharsets.UTF_8)) {
    lines.filter(l -> l.contains("ERROR"))
            .forEach(errorSink::accept);
}
```

Memory ≈ buffer + one line (watch pathological mega-lines — cap line length).

### 2) Fixed buffer binary

```java
ByteBuffer buf = ByteBuffer.allocateDirect(1 << 20);
try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
    while (ch.read(buf) != -1) {
        buf.flip();
        crc.update(buf);
        buf.clear();
    }
}
```

### 3) Resumable batch

Persist byte `offset` checkpoint; `channel.position(offset)` on restart.

### 4) Safe publish

Write `file.tmp` → fsync if required → `ATOMIC_MOVE` to final name.

## Production Scenarios

| Scenario | Approach |
|----------|----------|
| Large log processing | lines + filter; optional position index |
| File ingestion | stream parse CSV; don’t load all rows |
| Backup | walk + transferTo per file; skip/fail list |
| Batch | chunk + checkpoint |

## Failure Scenarios

| Symptom | Cause | Fix |
|---------|-------|-----|
| OOM | Slurp API | Stream |
| GC thrash | Tiny buffers + huge allocation rate of Strings | Batch parse / reuse buffers |
| Slow | Unbuffered NFS reads | 64K+ buffer |
| Corrupt downstream | Partial write | Temp + atomic move |

## Principal Notes

Backpressure: if sink is Kafka/DB, bound in-flight rows. Observability: bytes/sec, line rate, checkpoint lag, error file counts.

### Related

[files.md](./files.md) · [file-channels.md](./file-channels.md) · [buffered-io.md](./buffered-io.md) · [practical/large-file-processor.md](./practical/large-file-processor.md)

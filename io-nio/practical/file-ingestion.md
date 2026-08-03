# Practical: File Ingestion

Drop-folder pipeline for batch CSV/JSON payment files.

## Flow

```text
producer writes  inbox/name.csv.tmp → ATOMIC_MOVE → inbox/name.csv
watcher/poller detects → wait stable → parse stream → DB/outbox
                   ↓ success → processed/
                   ↓ fail    → failed/ + alert
```

## Sketch

```java
void ingest(Path file) {
    Path processed = root.resolve("processed").resolve(file.getFileName());
    Path failed = root.resolve("failed").resolve(file.getFileName());
    try {
        parseAndLoad(file); // streaming parser
        Files.move(file, processed, ATOMIC_MOVE, REPLACE_EXISTING);
        metrics.success();
    } catch (Exception e) {
        log.error("ingest failed {}", file, e);
        Files.move(file, failed, REPLACE_EXISTING);
        metrics.fail();
    }
}
```

## Failures

Reading while writer still writing · duplicate file names · poison file infinite retry — quarantine after N attempts.

See [../file-watching.md](../file-watching.md) · [../writers.md](../writers.md)

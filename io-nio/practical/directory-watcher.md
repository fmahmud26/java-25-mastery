# Practical: Directory Watcher

Monitor a directory for new files — ingestion trigger.

## Goal

Watch `/data/inbox`, process new `*.csv`, move to `processed/` or `failed/`.

## Approach

See [../file-watching.md](../file-watching.md). Add stability wait:

```java
long size = -1;
for (int i = 0; i < 10; i++) {
    long s = Files.size(file);
    if (s == size && s > 0) break;
    size = s;
    Thread.sleep(200);
}
```

Startup: reconcile by listing directory (events alone aren’t enough after downtime).

## Failures

OVERFLOW · partial writes · NFS · duplicate process events — use idempotent ingest keys.

See [file-ingestion.md](./file-ingestion.md)

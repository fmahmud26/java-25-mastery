# WatchService (Directory Monitoring)

NIO.2 API for filesystem event notification (create/modify/delete) — drop-folder ingestion.

## Mental Model

```text
WatchService ← register(dir, ENTRY_CREATE, …)
take()/poll() → WatchKey → pollEvents()
reset key or stop watching
```

**Caveats:** event coalescing, overflow, platform differences (latency, remote FS often weak/unreliable).

## Java 25 Example

```java
try (WatchService watch = FileSystems.getDefault().newWatchService()) {
    Path dir = Path.of("/data/inbox");
    dir.register(watch, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

    while (running) {
        WatchKey key = watch.take();
        for (WatchEvent<?> event : key.pollEvents()) {
            if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                metrics.overflow();
                rescan(dir); // safety net
                continue;
            }
            Path name = (Path) event.context();
            Path child = dir.resolve(name);
            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                ingestStable(child);
            }
        }
        if (!key.reset()) break;
    }
}
```

## Production — file ingestion

1. Watch `ENTRY_CREATE`.  
2. Wait until file size stable (writers still flushing).  
3. Process; then `ATOMIC_MOVE` to `processed/` or `failed/`.  
4. On `OVERFLOW`, full directory rescan.

## Failure Scenarios

| Issue | Mitigation |
|-------|------------|
| Partial file read | Stability check / write-temp-then-move by producer |
| Overflow under flood | Rescan + backpressure |
| NFS/shared mounts | Prefer polling or queue-based ingestion |
| Missed events on restart | Startup reconciliation scan |

### Related

[practical/directory-watcher.md](./practical/directory-watcher.md) · [files.md](./files.md) · [asynchronous-io.md](./asynchronous-io.md)

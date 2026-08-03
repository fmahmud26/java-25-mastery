# I/O & NIO — Engineering Guide (Java 25)

File and byte pipelines for ingestion, logs, backups, and batch jobs. Prefer **NIO.2 (`Path`/`Files`)** for day-to-day work; drop to **channels/buffers** for throughput, mapping, and fine control. Never load multi-GB files with `readAllBytes`.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Study path

1. Paths: [file](./file.md) → [path](./path.md) → [files](./files.md)  
2. Byte/char I/O: [streams](./streams.md) → [readers](./readers.md) / [writers](./writers.md) → [buffered-io](./buffered-io.md)  
3. NIO core: [nio](./nio.md) → [nio2](./nio2.md) → [file-channels](./file-channels.md) → [bytebuffer](./bytebuffer.md)  
4. Trees & events: [directory-traversal](./directory-traversal.md) → [file-watching](./file-watching.md)  
5. Scale: [large-files-and-memory](./large-files-and-memory.md) → [asynchronous-io](./asynchronous-io.md)  
6. Labs: [practical/](./practical/README.md) · Drill: [interview.md](./interview.md)

## Scenario index

| Scenario | Primary APIs |
|----------|----------------|
| Large log processing | `Files.lines`, buffered reader, grep filters |
| File ingestion | WatchService + atomic move + parse stream |
| Backup processing | `Files.walk` + `FileChannel` / `transferTo` |
| Directory monitoring | `WatchService` |
| Batch processing | Chunked channel reads + checkpoint offsets |

## Principal stance

Bound memory. Try-with-resources everywhere. Prefer streaming. Treat disk as unreliable (partial writes, NFS quirks, disk full). Observability: bytes/lines/sec, errors, lag behind watch events.

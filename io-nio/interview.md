# Interview — I/O & NIO

Java 25. Emphasize **large files, leaks, atomic publish, WatchService caveats**.

---

## Core

| Q | Sketch |
|---|--------|
| File vs Path/Files? | Legacy vs NIO.2 |
| Why not readAllBytes? | Memory — stream instead |
| Buffering why? | Fewer syscalls |
| ByteBuffer flip/clear? | Write↔read mode switch |
| FileChannel vs Files? | Positioned/transfer/map vs convenience |
| WatchService pitfalls? | Overflow, partial writes, NFS |
| Async file vs VT? | VT often simpler for concurrent blocking I/O |

---

## Scenario prompts

1. **Large log processing** — design ERROR counter without OOM.  
2. **File ingestion** — safe drop-folder protocol.  
3. **Backup** — walk + continue on permission errors.  
4. **Directory monitoring** — overflow strategy.  
5. **Batch** — resumable checksum from byte offset.

---

## Failure drills

- FD leak from `Files.lines`  
- OOM from `readAllLines`  
- Partial CSV consumed → temp + atomic move  
- `AtomicMoveNotSupportedException` cross FS  
- Direct buffer native leak  

---

## Principal

When is NIO channel worth it over `Files.copy`? How do you SLO a watch-based ingest pipeline? Disk full runbook?

### Related

[README.md](./README.md) · [large-files-and-memory.md](./large-files-and-memory.md) · [file-watching.md](./file-watching.md)

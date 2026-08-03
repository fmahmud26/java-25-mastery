# Asynchronous I/O Concepts

Non-blocking / async models so threads aren’t stuck waiting on disk/network.

## Mental Model

```text
Blocking:     thread waits in read()
NIO Selector: few threads multiplex many network channels
Async channels: Future/CompletionHandler callbacks (AsynchronousFileChannel)
Virtual threads: often simpler than async **network** style; for **local file** I/O still measure residual pinning ([../virtual-threads/blocking-io.md](../virtual-threads/blocking-io.md))
```

## Java APIs (conceptual)

| API | Role |
|-----|------|
| `AsynchronousFileChannel` | Async file read/write with `Future` / `CompletionHandler` |
| `AsynchronousSocketChannel` | Async TCP |
| Selector + non-blocking sockets | Classic scalable servers |
| Virtual threads (21+) | Prefer for many concurrent blocking `Files` ops |

## Java 25 Sketch — async file

```java
try (AsynchronousFileChannel ch = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
    ByteBuffer buf = ByteBuffer.allocate(8192);
    Future<Integer> f = ch.read(buf, 0);
    int n = f.get(); // still wait — or use CompletionHandler for true async pipeline
}
```

For most **business batch/log** jobs, sequential buffered I/O or VT workers are clearer than async file callbacks.

## Production Guidance

| Workload | Prefer |
|----------|--------|
| Drop-folder ingestion | WatchService + worker pool (VT ok) |
| Multi-GB sequential scan | Blocking channel/buffer |
| Many concurrent small file reads | Virtual threads + `Files` (measure pinning on hot paths) |
| High-conn network server | Framework (Netty) / async net |

## Failure Scenario

Async callback hell + unbounded outstanding reads → memory blow-up. Always bound concurrency.

## Principal Discussion

Async ≠ automatically faster disk (disk is the limit). Choose concurrency model for **orchestration**, not mythology. Virtual threads often replace async **network** callback style in app code; do **not** assume local file I/O always unmounts carriers — see [../virtual-threads/thread-pinning.md](../virtual-threads/thread-pinning.md).

### Related

[nio.md](./nio.md) · [file-watching.md](./file-watching.md) · [file-channels.md](./file-channels.md)

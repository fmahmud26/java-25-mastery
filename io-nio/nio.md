# NIO (`java.nio`)

Channels + buffers (+ selectors for network). Foundation under high-performance I/O.

## Mental Model

```text
Channel ←→ ByteBuffer
FileChannel / SocketChannel / …
Selector multiplexes non-blocking network channels
```

## When to Use vs NIO.2 Files

| Task | Prefer |
|------|--------|
| Read/write text, copy, walk, watch | NIO.2 `Files`/`Path` |
| Positioned I/O, transferTo, lock, map | `FileChannel` |
| Non-blocking sockets | Channels + Selector (or higher frameworks) |

## Java 25 Sketch

```java
try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
    ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 1024);
    while (ch.read(buf) != -1) {
        buf.flip();
        process(buf);
        buf.clear();
    }
}
```

## Production

Batch checksum / backup copy via `transferTo` between channels. Network servers usually use Netty/JDK HttpServer rather than raw selectors — but concepts matter in interviews.

## Failure Scenario

Forgetting `flip`/`clear` on buffers → empty reads or corrupted framing.

### Related

[nio2.md](./nio2.md) · [file-channels.md](./file-channels.md) · [bytebuffer.md](./bytebuffer.md) · [asynchronous-io.md](./asynchronous-io.md)

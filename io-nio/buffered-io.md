# Buffering

Reduce syscalls by aggregating reads/writes in memory (`BufferedInputStream`/`BufferedReader`/… or large `ByteBuffer`).

## Mental Model

```text
unbuffered: 1 read ≈ 1 syscall (slow for byte-at-a-time)
buffered:   fill 8K–64K (or 1MB) then serve from heap
```

## Java 25 Examples

```java
int bufSize = 64 * 1024;
try (var in = new BufferedInputStream(Files.newInputStream(path), bufSize);
     var out = new BufferedOutputStream(Files.newOutputStream(dst), bufSize)) {
    in.transferTo(out);
}

try (var br = new BufferedReader(Files.newBufferedReader(path), 65536)) {
    // Files.newBufferedReader already buffers; extra wrap rarely needed
}
```

`Files.newBufferedReader/Writer` already apply buffering — good defaults.

## Memory vs Throughput

| Buffer | Trade-off |
|--------|-----------|
| 8–16 KiB | Safe default |
| 64–256 KiB | Better sequential throughput |
| Multi-MB | Diminishing returns; more heap/direct |

Direct `ByteBuffer` (off-heap) for channel I/O can reduce copies — see [bytebuffer.md](./bytebuffer.md).

## Production — large log / backup

Always buffer sequential scans. Measure; don’t assume huge buffers win.

## Failure Scenario

Tiny reads without buffer on NFS → CPU + latency blow-up. Fix: buffer or channel with 64K+.

### Related

[streams.md](./streams.md) · [file-channels.md](./file-channels.md) · [large-files-and-memory.md](./large-files-and-memory.md)

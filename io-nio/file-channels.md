# FileChannel

Channel to a file — positioned reads/writes, `transferTo`/`transferFrom`, locks, memory mapping.

## Mental Model

```text
FileChannel.open(path, options)
read/write(ByteBuffer)
transferTo(position, count, targetChannel)  // zero-copy-ish OS support
map(MapMode, position, size) → MappedByteBuffer
```

## Java 25 Examples

```java
try (FileChannel in = FileChannel.open(src, StandardOpenOption.READ);
     FileChannel out = FileChannel.open(dst,
             StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
    long pos = 0;
    long size = in.size();
    while (pos < size) {
        long n = in.transferTo(pos, size - pos, out);
        if (n == 0) break; // avoid spin on rare platforms
        pos += n;
    }
}

// Random access record at offset
try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
    ByteBuffer hdr = ByteBuffer.allocate(8);
    ch.read(hdr, 0);
    hdr.flip();
    long recordCount = hdr.getLong();
}
```

## Production — backup / batch

`transferTo` for large file copies. Checkpoint `position` for resumable batch processing.

## Memory — mapping

```java
try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
    MappedByteBuffer map = ch.map(FileChannel.MapMode.READ_ONLY, 0, Math.min(ch.size(), 1 << 20));
    // map mid-size slices — avoid mapping entire multi-tens-of-GB file on constrained hosts
}
```

## Failure Scenario

Mapping huge files → virtual memory pressure / `IOException`. Cross-device `transferTo` may fall back to copy loops — still OK. Unclosed channels leak.

### Related

[bytebuffer.md](./bytebuffer.md) · [nio.md](./nio.md) · [large-files-and-memory.md](./large-files-and-memory.md)

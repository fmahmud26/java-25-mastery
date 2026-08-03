# Byte Streams (`InputStream` / `OutputStream`)

Binary I/O — files, sockets, process pipes. Character data needs Readers or explicit charset decoding.

## Mental Model

```text
bytes in → InputStream.read / transferTo → OutputStream
always close (TWR); prefer buffered wrappers
```

## Java 25 Examples

```java
try (InputStream in = Files.newInputStream(src);
     OutputStream out = Files.newOutputStream(dst,
             StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
    in.transferTo(out); // efficient copy
}

try (var in = new BufferedInputStream(Files.newInputStream(path), 64 * 1024)) {
    byte[] buf = new byte[8192];
    int n;
    while ((n = in.read(buf)) >= 0) {
        digest.update(buf, 0, n);
    }
}
```

## Production — backup processing

Stream copy with checksum — constant memory regardless of file size.

## Memory

Never `readAllBytes` for unbounded inputs. Fixed buffer (8K–1M) loops keep heap flat.

## Failure Scenario

Not closing streams → FD exhaustion under batch. `transferTo` interrupted mid-way → partial dst; use temp + atomic replace.

### Related

[buffered-io.md](./buffered-io.md) · [readers.md](./readers.md) · [file-channels.md](./file-channels.md)

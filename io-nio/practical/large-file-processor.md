# Practical: Large File Processor

Process multi-GB files with **constant memory**.

## Modes

`count-lines` · `grep` · `checksum` (CRC32/SHA-256)

## Design

```java
// Line mode
try (var br = Files.newBufferedReader(path, UTF_8)) {
    String line;
    long n = 0;
    while ((line = br.readLine()) != null) {
        n++;
        if (mode == GREP && line.contains(needle)) out.accept(line);
    }
}

// Checksum mode — FileChannel + direct buffer
ByteBuffer buf = ByteBuffer.allocateDirect(1 << 20);
try (FileChannel ch = FileChannel.open(path, READ)) {
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    while (ch.read(buf) != -1) {
        buf.flip();
        sha.update(buf);
        buf.clear();
    }
}
```

## Failures

OOM → using `readAllBytes`. Slow → tiny buffer on network FS. Partial grep on active file → document snapshot/copy first.

## Checklist

TWR · no slurp · progress metrics every N MB · tests with > buffer-size files

See [../large-files-and-memory.md](../large-files-and-memory.md)

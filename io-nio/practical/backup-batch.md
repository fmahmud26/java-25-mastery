# Practical: Backup / Batch Tree Copy

Backup processing — walk source tree, copy regular files, verify size/checksum.

## Approach

```java
Files.walkFileTree(srcRoot, new SimpleFileVisitor<>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path rel = srcRoot.relativize(file);
        Path dst = dstRoot.resolve(rel);
        Files.createDirectories(dst.getParent());
        copyFile(file, dst); // FileChannel.transferTo loop
        verifySize(file, dst);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        failures.add(file + ": " + exc.getMessage());
        return FileVisitResult.CONTINUE;
    }
});
```

## Batch processing variant

Enqueue paths to a worker pool (virtual threads); checkpoint completed relative paths for resume.

## Failures

Disk full mid-tree · cross-device moves · permission skips — collect failure report artifact.

See [../directory-traversal.md](../directory-traversal.md) · [../file-channels.md](../file-channels.md)

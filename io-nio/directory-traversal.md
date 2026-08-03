# File / Directory Traversal

Walk trees with `Files.walk`, `walkFileTree`, or `DirectoryStream`.

## Mental Model

```text
Files.walk(root) → Stream<Path> (must close)
walkFileTree → visitor callbacks (control depth/errors)
DirectoryStream → iterate one directory efficiently
```

## Java 25 Examples

```java
try (Stream<Path> paths = Files.walk(Path.of("/data/inbox"))) {
    List<Path> csv = paths
            .filter(Files::isRegularFile)
            .filter(p -> p.getFileName().toString().endsWith(".csv"))
            .toList();
}

Files.walkFileTree(root, new SimpleFileVisitor<>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (attrs.size() > 1_000_000_000L) {
            log.warn("huge file {}", file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        log.error("skip {}", file, exc);
        return FileVisitResult.CONTINUE; // don’t fail whole backup
    }
});
```

## Production — backup / batch discovery

Walk → filter → enqueue paths for workers. Cap depth; skip symlinks if looping risk (`FileVisitOption.FOLLOW_LINKS` careful).

## Memory

`walk().toList()` on huge trees materializes all paths — stream/process incrementally instead.

## Failure Scenario

Unclosed `walk` stream → native dir FDs leak. Permission errors aborting entire backup — use visitor `visitFileFailed` continue.

### Related

[files.md](./files.md) · [file-watching.md](./file-watching.md) · [practical/file-search-cli.md](./practical/file-search-cli.md)

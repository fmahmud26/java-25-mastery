# Practical: File Search CLI

Walk a tree and find files by name/glob/content needle.

```java
try (var paths = Files.walk(root)) {
    paths.filter(Files::isRegularFile)
            .filter(p -> matcher.matches(p.getFileName()))
            .forEach(p -> { if (contentMatch(p, needle)) System.out.println(p); });
}
```

Content match: buffered line scan — don’t slurp. Limit depth with `Files.walk(root, maxDepth)`.

## Failures

Following symlink cycles · permission errors — use `walkFileTree` and continue on failure.

See [../directory-traversal.md](../directory-traversal.md)

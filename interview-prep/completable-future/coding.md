# CompletableFuture — Coding

| Problem | CF shape |
|---------|----------|
| Parallel fetch + merge | `thenCombine` / `allOf` |
| Dependent calls | `thenCompose` |
| Timeout fallback | `completeOnTimeout` / `orTimeout` + `exceptionally` |
| Retry | helper wrapping `handle` + limited recursion/loop |
| Any success | `anyOf` + cast carefully |

```java
CompletableFuture<String> primary = fetchPrimary(vt);
CompletableFuture<String> backup = fetchBackup(vt);

CompletableFuture<String> first = CompletableFuture.anyOf(primary, backup)
        .thenApply(o -> (String) o)
        .orTimeout(1, TimeUnit.SECONDS);
```

**Talk track:** independent → combine; dependent → compose; never block commonPool; pass a VT executor for blocking I/O.

Practice: [../../concurrency](../../concurrency/), [../virtual-threads/coding.md](../virtual-threads/coding.md).

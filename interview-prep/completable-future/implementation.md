# CompletableFuture — Implementation

```java
Executor vt = Executors.newVirtualThreadPerTaskExecutor();

CompletableFuture<User> user =
        CompletableFuture.supplyAsync(() -> loadUser(id), vt);

CompletableFuture<Score> score =
        CompletableFuture.supplyAsync(() -> loadScore(id), vt);

CompletableFuture<Page> page = user.thenCombine(score, Page::new)
        .orTimeout(2, TimeUnit.SECONDS)
        .exceptionally(ex -> Page.empty());

Page result = page.join(); // on virtual thread: OK; still mind timeouts
```

## Selection cheat sheet

| Need | API |
|------|-----|
| Map result | `thenApply` |
| Chain dependent async | `thenCompose` |
| Parallel independent | `thenCombine` / `allOf` |
| Side effect | `thenAccept` / `whenComplete` |
| Recover | `exceptionally` / `handle` |
| Timeout | `orTimeout` / `completeOnTimeout` |
| Block | `get` (checked) / `join` (unchecked) |

## Prefer over raw `Future`

Composition, combining, explicit exception stages, timeouts — not just fire-and-`get`.

Related: [../../concurrency/future.md](../../concurrency/future.md), [../concurrency/implementation.md](../concurrency/implementation.md).

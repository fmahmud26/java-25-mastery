# CompletableFuture — Theory

`CompletableFuture<T>` = composable async result (implements `Future` + completion stages).

## Contracts

| Op family | Role |
|-----------|------|
| `supplyAsync` / `runAsync` | Start async work |
| `thenApply` / `thenAccept` / `thenRun` | Sync transform on completion |
| `thenApplyAsync` / … | Continue on an executor |
| `thenCompose` | Flatten nested futures (monadic bind) |
| `thenCombine` / `allOf` / `anyOf` | Join multiple stages |
| `exceptionally` / `handle` / `whenComplete` | Error path |
| `orTimeout` / `completeOnTimeout` | Time bounds (Java 9+) |
| `join` / `get` | Block for result |

## Mental model

```text
stage → (apply/compose/combine) → stage → …
              ↘ exceptionally/handle
```

- Default async executor: **`ForkJoinPool.commonPool()`** unless you pass an `Executor`.
- Completion can be explicit (`complete` / `completeExceptionally`) for adapters.

## Virtual threads interaction (Java 21/25)

| Approach | Note |
|----------|------|
| CF on FJP commonPool | Fine for **CPU-ish** callbacks; poor for blocking I/O (starves FJP) |
| CF + dedicated pool | Bound blocking work historically |
| **VT per task** | Prefer simple blocking style; less need for CF callback spaghetti |
| Blocking `get()` on VT | Cheap vs platform thread (unmount); still prefer structured clarity |
| Hybrid | CF for composing mixed async APIs; VT executor: `Executors.newVirtualThreadPerTaskExecutor()` |

Related: [future.md](../../concurrency/future.md), [../virtual-threads/theory.md](../virtual-threads/theory.md).

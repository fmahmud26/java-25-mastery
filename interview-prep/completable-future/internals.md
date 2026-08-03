# CompletableFuture — Internals

## Completion machinery

| Piece | Point |
|-------|-------|
| Result box | Holds value or exception; one-shot completion |
| Dependent stack/list | Waiters / dependent stages notified on complete |
| Executor choice | `*Async` uses commonPool or supplied executor |
| `allOf` | Completes when all complete (result is `Void`) — unpack manually |
| Cancellation | `cancel` may not interrupt running supplier unless you designed it to |

## Threading pitfalls

1. **Blocking inside `thenApply` on commonPool** — deadlocks/starvation under load.  
2. **`thenApply` vs `thenApplyAsync`** — former runs on completing thread (surprise thread hops).  
3. **Lost exceptions** — forgetting `handle`/`exceptionally`; `join` throws `CompletionException`.  
4. **Context loss** — ThreadLocals / MDC not propagated unless you wrap executor.

## VT era internals note

Virtual threads don’t change CF’s completion graph; they change **which executor** you should pass for blocking stages. Structured concurrency (preview on 25) is an alternative model for fan-out/fail-fast without CF pipelines.

Related: [../concurrency/internals.md](../concurrency/internals.md), [../virtual-threads/internals.md](../virtual-threads/internals.md).

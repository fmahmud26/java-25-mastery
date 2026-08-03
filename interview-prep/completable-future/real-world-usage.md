# CompletableFuture — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Parallel outbound HTTP | CF + VT executor (or pure VT + structured) | Cut latency vs sequential |
| Legacy async API | Adapt with `complete` / `completeExceptionally` | Bridge callbacks |
| CPU parallel | CF on FJP / custom CPU pool | Don’t use VT for pure CPU |
| Simple blocking service on 25 | VT per request, little CF | Clarity over ceremony |
| Gateway aggregation | `allOf` + timeouts + bulkheads | Protect p99 |
| Context (MDC) | Decorating executor | Restore request IDs in stages |

## Production rules of thumb

- **Always** name the executor for blocking work.  
- Timeouts on every remote stage.  
- Prefer `thenCompose` over nested `thenApply` returning futures.  
- On Java 25: ask “can VT + straightforward code replace this CF graph?” — use CF when composition/APIs demand it.

Related: [../concurrency/real-world-usage.md](../concurrency/real-world-usage.md), [../virtual-threads/real-world-usage.md](../virtual-threads/real-world-usage.md).

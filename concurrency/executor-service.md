# ExecutorService

Thread-pool abstraction: queue tasks, reuse workers, control shutdown.

## Mental Model

```text
tasks → queue → worker threads → Future results
sizing + queue + rejection policy = production behavior
```

## Internal Mechanics

`ThreadPoolExecutor`: core/max pool, work queue, `RejectedExecutionHandler`, keep-alive. `shutdown` vs `shutdownNow`. `invokeAll`/`invokeAny`.

## Code

```java
ThreadPoolExecutor payments = new ThreadPoolExecutor(
        8, 8,
        0L, TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<>(1000),
        Thread.ofPlatform().name("pay-", 0).factory(),
        new ThreadPoolExecutor.CallerRunsPolicy());

try {
    Future<CaptureResult> f = payments.submit(() -> psp.capture(cmd));
    return f.get(2, TimeUnit.SECONDS);
} finally {
    // app lifecycle: shutdown gracefully once
}

// Java 25 — Autocloseable executors
try (var exec = Executors.newFixedThreadPool(4)) {
    exec.invokeAll(List.of(c1, c2));
}
```

## Production Scenario — job processing / payments

Dedicated `pay-*` pool with **bounded queue** + rejection metrics. Separate from HTTP pool.

## Failure Scenario

| Misconfig | Result |
|-----------|--------|
| `CachedThreadPool` | Thread explosion |
| Unbounded queue + fixed threads | Huge latency / OOM queue |
| `CallerRuns` unexpected | Latency on caller thread |
| Shutdown mid-flight | Lost tasks without drain |

## Debugging Strategy

Dump workers; JMX/micrometer: active, queue size, rejected. Name threads.

## Performance

Size CPU pools ≈ cores; blocking pools by latency*QPS math + limits. Queue as buffer — not infinite.

## Trade-offs

| Queue | Effect |
|-------|--------|
| SyncQueue | Hand-off, more threads if max allows |
| Bounded Array | Backpressure |
| Unbounded Linked | Hide overload until OOM |

## Interview Questions

- core vs max pool?  
- Rejection policies?  
- shutdown vs shutdownNow?  
- Why not Executors.newCachedThreadPool in prod?

## Principal-Level Discussion

Executors are **bulkheads**. One pool for payments, one for notifications, one for CPU. Document saturation behavior (reject vs caller-runs vs shed load). Pair with timeouts and idempotency.

### Related

[future.md](./future.md) · [platform-threads.md](./platform-threads.md) · [blockingqueue.md](./blockingqueue.md) · [contention.md](./contention.md)

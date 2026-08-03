# CompletableFuture — Performance

## Cost drivers

| Factor | Effect |
|--------|--------|
| Stage objects | Allocation per hop — fine at request scale; watch huge fan-out |
| CommonPool blocking | Throughput collapse |
| Oversized pools | Context thrash (platform threads) |
| VT executor | Excellent for many blocking stages; still bound **downstream pools** (JDBC) |
| `allOf` of thousands | Memory + scheduling overhead — batch |

## Degradation causes

1. Hidden `get()` on platform request threads → pool exhaustion.  
2. Callback chains doing I/O without `*Async(executor)`.  
3. Unbounded fan-out to dependencies.  
4. Timeouts absent → thread/VT pile-up waiting on sick deps.  
5. Retry without jitter → amplification.

## Measurement

- FJP / executor active threads, queue depth.  
- JFR: blocked time in stages; pinning if synchronized+blocking on VT.  
- Trace spans per stage for p99.

Related: [../concurrency/performance.md](../concurrency/performance.md), [../virtual-threads/performance.md](../virtual-threads/performance.md).

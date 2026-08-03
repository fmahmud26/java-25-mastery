# System Design — Performance

## Levers

| Lever | Effect |
|-------|--------|
| Caching | Cut read latency & DB load |
| Async offload | Protect user-facing p99 |
| Partitioning | Parallelize writes/reads |
| Connection pooling | Bound resource use |
| Compression / pagination | Bandwidth & memory |
| CDN | Edge latency for static/rare-changing |

## Degradation causes

1. Hot partitions / hot keys.  
2. Synchronous fan-out to many deps on the request path.  
3. Unbounded retries → retry storms.  
4. Cache stampede on expiry.  
5. Oversized payloads / N+1 between services.  
6. Thread/connection pool mismatch (classic Java footgun).

## Measurement

- SLIs: p50/p99 latency, error rate, saturation (CPU, pool wait, queue lag).  
- Distributed tracing for critical path.  
- Load tests that include failure injection.

Related: [latency.md](../../system-design/fundamentals/latency.md), [throughput.md](../../system-design/fundamentals/throughput.md), [rate-limiting.md](../../system-design/distributed-systems/rate-limiting.md).

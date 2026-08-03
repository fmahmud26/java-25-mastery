# System Design — Coding

In SD rounds “coding” = **capacity math + API sketches + sequence diagrams**, plus light Java interfaces.

| Problem | Deep-dive focus |
|---------|-----------------|
| URL shortener | Key gen, redirects, cache, analytics async |
| Rate limiter | Token bucket / sliding window; Redis vs local |
| Notification | Fan-out, templates, retries, DLQ |
| Payment | Idempotency, ledger, webhooks |
| Feed / timeline | Fan-out-on-write vs read; ranking cache |

```java
// API sketch — enough to ground the design
public interface ShortLinkService {
    ShortUrl create(CreateCmd cmd);          // idempotent via requestId
    Optional<String> resolve(String code);   // cache-aside
}
```

## Capacity quick math (talk aloud)

- QPS × payload × replication ≈ bandwidth.  
- Storage = objects × size × retention × growth.  
- Cache hit ratio dominates read path cost.

Practice: [../../system-design/examples](../../system-design/examples/), [interview.md](../../system-design/interview.md).

# Systems Catalog

Principal-level designs. Each file follows:

Requirements → Capacity → Architecture → Components → Data flow → Storage → Scaling → Failures → Observability → Security → Trade-offs → Evolution

| System | Start here if you need… |
|--------|-------------------------|
| [url-shortener](./url-shortener.md) | Read skew, ID allocation, thin critical path |
| [payment-system](./payment-system.md) | Idempotency, ledger, provider uncertainty |
| [notification-platform](./notification-platform.md) | Fan-out, quotas, priority lanes |
| [order-system](./order-system.md) | Saga, inventory, outbox |
| [file-upload](./file-upload.md) | Presigned uploads, async scan |
| [rate-limiter](./rate-limiter.md) | Distributed counters, fail-open/closed |
| [distributed-cache](./distributed-cache.md) | Hot keys, invalidation, L1/L2 |
| [log-processing](./log-processing.md) | Ingest volume, tiers, backpressure |

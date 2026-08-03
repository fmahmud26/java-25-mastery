# Rate Limiting — Failure Lens

Protect systems and enforce fairness. Distributed counters add their own failure modes.

## Failure focus

| Failure | Choice |
|---------|--------|
| Redis down | Fail-closed (auth) vs fail-open (telemetry) — explicit |
| Hot limit key | Shard / approximate / dedicated |
| Fixed window | 2× burst at boundary — name it |
| No Retry-After | Client retry storms |

## Trade-offs

Global accuracy (+RTT) vs local approximation (over-allow). Cost of Redis QPS at edge scale.

Related: [retry.md](./retry.md), [backpressure.md](./backpressure.md), [fault-tolerance.md](./fault-tolerance.md).

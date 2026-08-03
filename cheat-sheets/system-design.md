# System Design — Cheat Sheet

**Sources:** [../system-design/README.md](../system-design/README.md) · [interview.md](../system-design/interview.md) · [fundamentals/](../system-design/fundamentals/) · [distributed-systems/](../system-design/distributed-systems/) · [systems/](../system-design/systems/) · [interview-prep/tracks/system-design](../interview-prep/tracks/system-design.md)

## 50-min spine (chapter)

```text
Clarify → Capacity → Architecture → Deep dive
→ Failures → Observability/DR → Evolution
```

Template per system: Requirements → Capacity → … → Evolution ([README](../system-design/README.md))

## Capacity sketch (say aloud)

From [interview.md](../system-design/interview.md): DAU → QPS avg/peak · read:write · payload → bandwidth · storage × years · cache hit → miss QPS · queue depth under outage.

## Lever map

| Lever | Fundamental |
|-------|-------------|
| Scale out / bottleneck | [scalability](../system-design/fundamentals/scalability.md) |
| Redundancy / degrade | [availability](../system-design/fundamentals/availability.md) |
| p99 budget | [latency](../system-design/fundamentals/latency.md) |
| Cache + stampede | [caching](../system-design/fundamentals/caching.md) |
| Shard key | [partitioning](../system-design/fundamentals/partitioning.md) |
| Sync vs async replica | [replication](../system-design/fundamentals/replication.md) |
| Per-API consistency | [consistency](../system-design/fundamentals/consistency.md) · [cap](../system-design/fundamentals/cap-theorem.md) |
| RPO/RTO | [disaster-recovery](../system-design/fundamentals/disaster-recovery.md) |

## Distributed failure (must)

| Topic | Doc |
|-------|-----|
| Idempotency | [idempotency](../system-design/distributed-systems/idempotency.md) |
| Delivery | [message-delivery](../system-design/distributed-systems/message-delivery.md) |
| Outbox / dual-write | [distributed-transactions](../system-design/distributed-systems/distributed-transactions.md) · [dual-write scenario](../system-design/distributed-systems/scenarios/dual-write-gap.md) |
| Retries / CB / backpressure | [retry](../system-design/distributed-systems/retry.md) · [circuit-breakers](../system-design/distributed-systems/circuit-breakers.md) · [backpressure](../system-design/distributed-systems/backpressure.md) |

Phrase bank: [system-design/interview.md](../system-design/interview.md)

## Systems to rehearse

URL shortener · Payment · Notification · Order · File upload · Rate limiter · Dist cache · Log processing → [systems/](../system-design/systems/)

## Mock

[system-design-50](../interview-prep/formats/mock-interviews/system-design-50.md)

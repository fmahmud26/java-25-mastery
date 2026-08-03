# Production Scenarios Index

Failure-first drills. For each: what breaks → bad reaction → Principal response → trade-offs → interview probes.

| Scenario | Primary concepts |
|----------|------------------|
| [payment-unknown-outcome](./payment-unknown-outcome.md) | Idempotency, failure handling |
| [consumer-lag-poison](./consumer-lag-poison.md) | Delivery, ordering, DLQ |
| [lock-split-brain](./lock-split-brain.md) | Locks, fencing |
| [replica-lag-ryw](./replica-lag-ryw.md) | Replication, consistency |
| [hot-partition](./hot-partition.md) | Partitioning, backpressure |
| [retry-storm](./retry-storm.md) | Retries, backoff, CB |
| [dual-write-gap](./dual-write-gap.md) | Distributed txns, outbox |
| [queue-overload](./queue-overload.md) | Backpressure, fault tolerance |

## Cross-links

| Practice surface | Path |
|------------------|------|
| Timed debug/scenario formats | [../../../interview-prep/formats/](../../../interview-prep/formats/) |
| Incident lab (spoilers last) | [../../../scenario-lab/](../../../scenario-lab/) |
| PE technical cards | [../../../principal-engineer/scenarios/](../../../principal-engineer/scenarios/) |
| Outbox portfolio | [../../../real-world-projects/08-notification-outbox/](../../../real-world-projects/08-notification-outbox/) |

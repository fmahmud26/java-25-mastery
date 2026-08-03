# Debug: Order exists, fulfillment never runs

## Symptoms

DB has order; Kafka topic quiet; intermittent after broker blips.

## Your move (PCR-OTDR)

Dual-write gap → Options (outbox / CDC / transactional messaging) → Decision → Result (outbox lag metric).

## Deep sources

- [../../../system-design/distributed-systems/scenarios/dual-write-gap.md](../../../system-design/distributed-systems/scenarios/dual-write-gap.md)
- [../../../system-design/distributed-systems/distributed-transactions.md](../../../system-design/distributed-systems/distributed-transactions.md)
- [../../../real-world-projects/08-notification-outbox/](../../../real-world-projects/08-notification-outbox/)
- Architecture format: [../architecture/introduce-outbox.md](../architecture/introduce-outbox.md)

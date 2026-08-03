# Architecture: Introduce outbox

## Prompt

Monolith dual-writes DB then Kafka; lost events. Design introduction of outbox with zero downtime.

## Expect

Expand/contract; publisher; idempotent consumers; lag metrics; rollback.

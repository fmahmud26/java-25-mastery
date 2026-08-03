# Event-Driven Architecture — Failure Lens

Events decouple services; they also create **lag, reordering across keys, and dual-write hazards**.

## When it helps

Fan-out reactions, smoothing spikes, independent consumer deploy.

## Failure focus

| Failure | Mitigation |
|---------|------------|
| Dual-write DB+bus | Outbox/CDC |
| Consumer bugs | DLQ, idempotency, replay |
| Schema break | Compatibility rules, registry |
| Assumed global order | Per-key only |
| Saga compensate fail | Idempotent compensate + manual reconcile |

## Trade-offs

Autonomy and scale vs debugging complexity and eventual consistency UX.

Related: [distributed-transactions.md](./distributed-transactions.md), [message-delivery.md](./message-delivery.md), [scenarios/dual-write-gap.md](./scenarios/dual-write-gap.md).

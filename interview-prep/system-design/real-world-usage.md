# System Design — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Read-heavy catalog | Cache-aside + CDN | p99 and cost |
| Checkout / payment | Strong consistency + idempotency keys | Money correctness |
| Notifications | Queue + workers + DLQ | Spikes & retries |
| Multi-region reads | Local reads, async replicate | Latency vs staleness |
| Search | Separate search index | Don’t abuse OLTP DB |
| Audit trail | Append-only log/event | Rebuild projections |

## Production rules of thumb

- Every remote call: timeout, retry policy, metrics.  
- Design for **at-least-once** delivery + idempotent handlers.  
- Backpressure beats unbounded queues.  
- Start simple (single primary + cache); add shards when numbers demand.

Related: [event-driven-architecture.md](../../system-design/distributed-systems/event-driven-architecture.md), [circuit-breakers.md](../../system-design/distributed-systems/circuit-breakers.md).

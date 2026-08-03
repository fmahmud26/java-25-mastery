# Eventual Consistency

Replicas/readers **converge** given no new writes. Interim states can be wrong — design product + repair for that.

## When it’s acceptable

Click counts, search indexes, recommendation features, email delivery status, CDN cache, analytics.

## When it’s not

Seat SOLD without a hold protocol, payment CAPTURED flag, uniqueness constraints, authorization revoke that must be immediate.

## Production scenario: search index lag

Order placed → Kafka → indexer. User searches “my orders” within 2s → miss.  
**Mitigations:** read-your-writes via primary API for “my” data; show “processing”; version tokens; synchronous write path for critical read models only.

## Repair mechanisms

| Mechanism | Use |
|-----------|-----|
| Read repair | Quorum stores |
| Anti-entropy / repair jobs | Cassandra-style, checksums |
| CDC catch-up | Rebuild projections |
| Reconciliation | Payments vs PSP reports |
| TTL + reload | Caches |

## Failure focus

- Calling it “eventual” to avoid designing conflict resolution (LWW accidentally deletes data).  
- Unbounded divergence (bug stops consumers) — lag alerts are mandatory.  
- Exactly-once UI assumptions on at-least-once pipelines.

## Trade-offs

| Buy | Sell |
|-----|------|
| Write availability, scale, decoupling | Complexity in merge, support UX, reconcile cost |

**Principal phrasing:** “Eventual consistency is a contract with a **maximum acceptable divergence window** and a **repair path** — not a shrug.”

Related: [consistency.md](./consistency.md), [message-delivery.md](./message-delivery.md), [scenarios/replica-lag-ryw.md](./scenarios/replica-lag-ryw.md).

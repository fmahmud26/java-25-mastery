# Distributed Transactions

Atomicity across **multiple** systems (two DBs, DB+queue, two services). Classic 2PC; practical alternatives: sagas, outbox, single aggregate.

## 2PC / XA

| Phase | Failure |
|-------|---------|
| Prepare | Coordinator crash → participants blocked (in-doubt) |
| Commit | Participant down → blocking / heuristic decisions |
| Network | High latency; availability couples all resources |

**Production reality:** rare in modern service architectures; ops nightmares with in-doubt txns.

## Saga

Sequence of local txns + **compensations**.  
Choreography (events) vs orchestration (coordinator service).

| Failure | Handling |
|---------|----------|
| Step N fails | Compensate N-1…1 |
| Compensate fails | Manual reconcile / retry compensate (must be idempotent) |
| No isolation | Dirty reads unless design avoids |

## Outbox / single writer

Best “transaction” for DB+event: **same local txn** writes row + outbox; publisher drains. Avoids dual-write gap.

## Production scenario: order + inventory + payment

Don’t 2PC three services.  
**Saga:** reserve inventory → charge → confirm; on fail release/refund.  
**Idempotency** on every step.  
**State machine** on order aggregate.

## Trade-offs

| Approach | Buy | Sell |
|----------|-----|------|
| 2PC | Strong atomicity | Availability, latency, ops |
| Saga | Autonomy, scale | Weak isolation, compensate design |
| Single DB aggregate | Simplicity | Scale/ownership limits |
| Outbox | Reliable events | Extra table/process |

## Principal interview angles

- “Why not XA between order DB and Kafka?”  
- “What’s your compensation for ‘payment captured, inventory commit failed’?”  

Related: [scenarios/dual-write-gap.md](./scenarios/dual-write-gap.md), [idempotency.md](./idempotency.md), [message-delivery.md](./message-delivery.md).

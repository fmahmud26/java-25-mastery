# Distributed Systems — Failure-First Mastery

Principal-level distributed systems: **what breaks, what you trade, what you measure**.

This folder is the deep dive. Product designs live in [../systems/](../systems/); capacity levers in [../fundamentals/](../fundamentals/). Here the unit of study is **partial failure**.

## How to study

1. Read each concept until you can teach the **failure mode** without notes.  
2. Walk [scenarios/](./scenarios/) aloud: what you do in the first 15 minutes.  
3. Drill [interview.md](./interview.md) — Principal questions with model answer skeletons.

## Concept map

| Topic | File | Core tension |
|-------|------|----------------|
| CAP | [cap.md](./cap.md) | Partition → refuse vs diverge |
| Consistency | [consistency.md](./consistency.md) | Freshness vs latency/availability |
| Eventual consistency | [eventual-consistency.md](./eventual-consistency.md) | Convergence vs interim wrongness |
| Replication | [replication.md](./replication.md) | Durability vs write latency / RPO |
| Partitioning | [partitioning.md](./partitioning.md) | Scale vs cross-partition truth |
| Leader / follower | [leader-follower.md](./leader-follower.md) | Single writer vs failover pain |
| Idempotency | [idempotency.md](./idempotency.md) | Safe retries vs storage/complexity |
| Message delivery | [message-delivery.md](./message-delivery.md) | Loss vs dupes vs cost |
| Ordering | [ordering.md](./ordering.md) | Per-key order vs throughput |
| Retries | [retry.md](./retry.md) | Recovery vs amplification |
| Backoff | [backoff.md](./backoff.md) | Patience vs herd effects |
| Distributed locks | [distributed-locks.md](./distributed-locks.md) | Exclusion vs split-brain risk |
| Distributed transactions | [distributed-transactions.md](./distributed-transactions.md) | Atomicity vs availability/latency |
| Failure handling | [failure-handling.md](./failure-handling.md) | Detect → isolate → degrade → recover |
| Backpressure | [backpressure.md](./backpressure.md) | Protect core vs drop/refuse work |
| Fault tolerance | [fault-tolerance.md](./fault-tolerance.md) | Survive faults without lying |

## Production scenarios

| Scenario | File |
|----------|------|
| Payment timeout: charged or not? | [scenarios/payment-unknown-outcome.md](./scenarios/payment-unknown-outcome.md) |
| Kafka consumer lag / poison message | [scenarios/consumer-lag-poison.md](./scenarios/consumer-lag-poison.md) |
| Split-brain after Redis lock | [scenarios/lock-split-brain.md](./scenarios/lock-split-brain.md) |
| Replica lag breaks read-your-writes | [scenarios/replica-lag-ryw.md](./scenarios/replica-lag-ryw.md) |
| Hot partition melts one shard | [scenarios/hot-partition.md](./scenarios/hot-partition.md) |
| Retry storm cascades | [scenarios/retry-storm.md](./scenarios/retry-storm.md) |
| Dual-write outbox gap | [scenarios/dual-write-gap.md](./scenarios/dual-write-gap.md) |
| Queue overload / no backpressure | [scenarios/queue-overload.md](./scenarios/queue-overload.md) |

## Principal stance (memorize)

- Networks duplicate, delay, reorder, and partition. Design for that first.  
- “Exactly-once” means **exactly-once effect** via idempotency + dedupe, not magic.  
- Prefer **single-writer aggregates** over distributed locks and 2PC.  
- Retries without budgets and idempotency are outage amplifiers.  
- Every consistency choice is a product UX choice — say it explicitly.

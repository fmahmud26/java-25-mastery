# Kafka Concepts — Failure Lens

Log-based streaming: durable, partitioned, replayable. Ordering and delivery myths cause SEVs.

## Essentials

| Concept | Failure if misunderstood |
|---------|--------------------------|
| Partition | Parallelism unit; one stuck key blocks that partition |
| Key | Ordering scope; bad key → hot partition or lost causality |
| Consumer group | Competing consumers; not parallel within one partition |
| Offset commit | Commit early → loss; late → dupes |
| `acks` / ISR | `acks=1` weaker RPO than `acks=all` |
| Retention | Replay window; too short → cannot rebuild |
| Compacted topic | Latest per key — not full audit |

## Delivery truth

Producers/consumers are **at-least-once** in common setups. EOS between Kafka↔Kafka ≠ EOS to Postgres. Still need idempotency/outbox for external effects.

## Production failure modes

- Rebalance storms during deploy → lag spikes  
- Stop-the-world GC on consumer → session timeout → rebalance  
- Huge messages → OOM; use object store pointers  
- Single topic for all priorities → OTP stuck behind batch  

Related: [message-delivery.md](./message-delivery.md), [ordering.md](./ordering.md), [scenarios/consumer-lag-poison.md](./scenarios/consumer-lag-poison.md).

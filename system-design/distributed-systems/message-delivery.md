# Message Delivery Semantics

Between producer and consumer, networks give you **loss, duplication, and delay**. Delivery guarantees are claims about retries and durability — not magic.

## Vocabulary

| Claim | Meaning | You must still… |
|-------|---------|-----------------|
| At-most-once | May lose; no redo | Accept loss or sync path |
| At-least-once | May duplicate | Idempotent handlers |
| Effectively once | At-least-once + dedupe/txn | Design the effect key |
| Kafka EOS | Txnal produce/consume in Kafka | External DB still needs idempotency/outbox |

## Production scenario: “we use Kafka so exactly-once”

Consumer writes to DB then commits offset — crash between → duplicate process. Offset then DB — crash → loss.  
**Defense:** transactional outbox, or idempotent upsert keyed by `event_id`, or Kafka transactions **and** idempotent external write.

## Ack timing failures

| Pattern | Risk |
|---------|------|
| Ack before side effect | Loss on crash |
| Ack after side effect | Dupes on crash after effect before ack |
| Batch ack | Partial batch complexity |

## Trade-offs

| Stronger durability (`acks=all`, sync) | Latency / throughput |
| Redelivery | Handler complexity |
| Long retention | Storage $ |

## Principal interview angles

- “Where can a message be lost in your pipeline?”  
- “Where can it be duplicated, and what’s the idempotency key?”  

Related: [ordering.md](./ordering.md), [idempotency.md](./idempotency.md), [scenarios/dual-write-gap.md](./scenarios/dual-write-gap.md).

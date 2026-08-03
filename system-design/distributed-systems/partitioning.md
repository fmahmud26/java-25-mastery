# Partitioning (Sharding)

Split ownership of data/load so nodes hold a **subset**. Key choice dominates failure modes.

## Strategies

| Strategy | Failure mode |
|----------|--------------|
| Hash(`id`) | Hard range queries; reshard pain |
| Range | Hot ranges (time, celebrity ids) |
| Directory | Directory outage = total outage |
| Geo/tenant | Imbalance; cross-cell features |

## Production scenario: shard by `user_id`

Works for “user’s orders.” Finance wants “all orders today” → scatter-gather → tail latency = slowest shard; one bad shard fails the report.

**Mitigation:** separate analytics pipeline (CDC → warehouse); don’t OLTP scatter for heavy reports.

## Hot partition

One key (flash-sale SKU) saturates one shard.  
**Mitigations:** salt keys, per-SKU queue, isolate hot entity, local cache, admission control.

## Cross-partition truth

No atomic multi-shard txn without 2PC/saga. Inventing “just update both” = dual-write gap (see scenarios).

## Trade-offs

| Buy | Sell |
|-----|------|
| Write/storage scale | Cross-key constraints, joins, resharding, hot keys |

## Principal interview angles

- “What’s the partition key and which queries become hard?”  
- “How do you handle the celebrity key?”  

Related: [scenarios/hot-partition.md](./scenarios/hot-partition.md), [distributed-transactions.md](./distributed-transactions.md), [replication.md](./replication.md).

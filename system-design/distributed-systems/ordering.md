# Ordering

Total global order across a distributed log is expensive. Practical systems provide **per-partition / per-key order**.

## What you actually get

| System | Order guarantee |
|--------|-----------------|
| Kafka | Per partition; key → same partition |
| SQS standard | None |
| SQS FIFO | Per message group id |
| Single DB primary | Txn order on that primary |
| Multi-shard | No cross-shard order |

## Production scenario: balance updates reordered

Events: `credit 100`, `debit 100` keyed randomly → debit applied first → transient negative → fraud alert.  
**Fix:** partition key = `accountId` so causal updates serialize.

## Failure focus

- One partition for “global order” → throughput ceiling + hot partition.  
- Assuming consumer group preserves order across partitions — it doesn’t.  
- Compacted topics: order of older values irrelevant; only latest key matters.

## Trade-offs

| Per-key order | Global order |
|---------------|--------------|
| Scales with keys | One sequence bottleneck |
| Enough for most domains | Rarely needed |

## Principal interview angles

- “What is your ordering key and what breaks if it’s wrong?”  
- “Do you need order or only causal idempotent state?”  

Related: [partitioning.md](./partitioning.md), [message-delivery.md](./message-delivery.md), [leader-follower.md](./leader-follower.md).

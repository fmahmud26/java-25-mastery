# Partitioning (Sharding)

Split data/load across nodes so each owns a **subset**. Choose key from **access patterns**.

## Strategies

| Strategy | How | Good for | Pain |
|----------|-----|----------|------|
| Hash | `hash(key) % N` or consistent hash | Even load, point lookups by key | Range scans / joins hard |
| Range | Key ranges per shard | Range queries | Hot ranges (time, celebrity IDs) |
| Directory | Lookup service maps key→shard | Flexibility | Directory HA becomes critical |
| Geo / tenant | Region or customer id | Isolation, compliance | Imbalance |

## Partition key design

Ask: **What is on every query?**  
- User-centric: `userId`  
- Short URL redirect: `code`  
- Orders by merchant dashboard: maybe `merchantId` (accept harder user-global queries)

Secondary indexes become **scatter-gather** unless local to shard or global index service.

## Resharding

| Approach | Why |
|----------|-----|
| Consistent hashing / virtual nodes | Minimize move on cluster change |
| Dual-write + catch-up | Controlled migration |
| New epoch cluster + replay | Heavy but clean |

## Hot partitions

Symptoms: one shard CPU high. Fixes: salt keys (`userId#0..K`), separate hot-entity store, cache, split write fan-out.

## Secondary pitfalls

- Cross-shard transactions (2PC) — avoid; use saga  
- Global unique constraints without dedicated allocator  
- Time-based shards that never age out  

Related: [scalability.md](./scalability.md), [databases.md](./databases.md), [consistency.md](./consistency.md).

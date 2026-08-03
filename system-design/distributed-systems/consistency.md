# Consistency Models

Consistency answers: **after a write, which reads are allowed?** Pick **per API**.

## Models you’ll actually use

| Model | Guarantee | Failure if violated |
|-------|-----------|---------------------|
| Linearizable | One global order; read latest | Double-sell, duplicate id |
| Sequential | Ops of each client in order | Rarely enough alone |
| Read-your-writes | Actor sees own writes | User edits profile, refreshes old |
| Monotonic reads | Never go backward in time | Feed jumps oddly |
| Causal | Cause before effect | Comment before post appears |
| Eventual | Converge if writes stop | Interim wrongness — product must cope |

## Production scenario: checkout

- Inventory decrement: **strong** (conditional update / serializable txn).  
- “Items purchased” counter on homepage: **eventual**.  
- Order confirmation page: **read-your-writes** (read primary or version token).

## Failure focus

- Reading async replica after write → support tickets “my order vanished.”  
- Caching without version → sticky stale.  
- Cross-shard “transactions” assumed atomic when they aren’t.

## Trade-offs

| Stronger consistency | Cost |
|----------------------|------|
| Sync replication / quorum | Write latency, availability on partition |
| Primary reads | Primary load |
| Cross-region sync | Huge latency |

| Weaker consistency | Cost |
|--------------------|------|
| Speed & availability | Merge logic, UX honesty, reconciliation jobs |

## Principal interview angles

- “Name three consistency levels in your design and why each.”  
- “How do you implement read-your-writes without forcing all reads to primary forever?”  

Related: [eventual-consistency.md](./eventual-consistency.md), [cap.md](./cap.md), [replication.md](./replication.md).

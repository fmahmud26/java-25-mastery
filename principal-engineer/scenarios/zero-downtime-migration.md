# Scenario: Migration Must Have Zero Downtime

## Context

Must move `orders` from monolithic Postgres (v11, 12 TB, 20k QPS peak mixed) to a sharded Postgres topology (by `customer_id`) for headroom. Exec mandate: **zero downtime**, no lost orders, no duplicate charges. Prior migration attempt aborted after dual-write caused inventory mismatch.

## Constraints

- RPO = 0 for paid orders; RTO minutes for read path OK if flagged  
- Payment webhooks continue during migration  
- 24/7 global traffic; no full cutover window  
- Idempotency keys exist on create-order for ~8 months (not forever)  
- Foreign systems read orders via API (not direct DB) — helpful  

## Options

| Option | Approach |
|--------|----------|
| **A. Stop-the-world dump/restore** | Maintenance window |
| **B. Dual write to old+new** | App writes both |
| **C. CDC (logical replication) + router** | Replicate then cut writes |
| **D. Event replay from Kafka** | Only if Kafka is SoT (it isn’t) |
| **E. Blue/green DB with DNS flip** | Same schema only |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Simple | Violates zero downtime; long lock |
| B | Familiar | Exactly what burned you — two failure domains |
| C | Single write path during catch-up | Slot lag; need careful cutover |
| D | Elegant | Requires rewriting history as events first |
| E | Fast flip | Doesn’t deliver sharding |

## Decision

**CDC-based expand/cutover with read shadow, not long-lived dual write:**

1. Stand up shard cluster; initial bulk copy.  
2. Continuous CDC from monolith → shards (consistent by `customer_id` routing).  
3. App **reads** canary from shards (compare); **writes** still monolith.  
4. Cutover wave: flip writes per customer cohort through an **order router**; CDC reverse or freeze old.  
5. Payments webhooks addressed to order service (stable), not DB.

Avoid open-ended dual write from app into two clusters.

## Reasoning

Zero downtime with RPO 0 needs **one authoritative write path at any time** per entity, plus verified replication lag. Dual write from app doubles bugs (exactly prior failure). CDC keeps SoT singular until atomic cohort flip.

## Risks

- Replication lag → stale reads if flipped early  
- Non-sharded queries (admin “all orders”) break — need new query path  
- Sequences/IDs across shards  
- Hot customers during cohort flip  
- Schema drift during long CDC  

## Migration

| Phase | Action | Abort |
|-------|--------|-------|
| 0 | Schema parity tests; ID strategy (snowflake / ULID) | — |
| 1 | Bulk copy + CDC; lag SLO < 5s p99 | Lag breaches 30m |
| 2 | Shadow reads 10% traffic; diff tool | Diff > 0.01% |
| 3 | Write flip cohort A (1%); webhook soak | Error budget burn |
| 4 | Expand cohorts; freeze monolith writes per cohort | |
| 5 | Monolith tables read-only → archive | |

Rollback: cohort router points writes back to monolith **only if** CDC reverse is verified or cohort was dual-readable; never flip-flop money without ledger reconcile.

## Success metrics

- User-visible order create availability during migration ≥ SLO  
- Zero duplicate paid orders (reconcile vs PSP)  
- Zero lost paid orders (RPO 0)  
- CDC lag p99 < 5s pre-cutover  
- Diff rate on shadow reads < 0.01% for 7 days  
- Old primary CPU headroom regained post-cutover  

Related: [../topics/migration-strategy.md](../topics/migration-strategy.md), [../topics/reliability.md](../topics/reliability.md).

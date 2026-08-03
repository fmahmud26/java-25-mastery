# Scenario: Shared Database Between Two Teams

## Context

`Order Service` and `Fulfillment Service` are separate deployables (already “microservices”) but share one Postgres `commerce` database. Fulfillment runs heavy analytical updates on `order_lines`; Order team migrations lock tables; fulfillment’s long transactions cause checkout timeouts. Both claim “we’ll split next year.”

## Constraints

- Fulfillment needs historical order line data  
- Order path SLO tight; fulfillment can be eventually consistent minutes  
- Zero-downtime required for split  
- Foreign key web between schemas  

## Options

| Option | Approach |
|--------|----------|
| **A. Keep shared DB; schedule migrations** | |
| **B. Split DB; sync via CDC** | Order owns orders; fulfillment projects |
| **C. Split DB; sync via API calls** | Fulfillment pulls on demand |
| **D. Merge back to monolith** | |
| **E. Shared DB but separate schemas + strict roles** | Step toward B |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | None lasting | Recurring SEVs |
| B | Isolation + right consistency | CDC complexity |
| C | Simple tooling | Chatty; coupling on availability |
| D | Txn simplicity | Undoes org split; politics |
| E | Safety interim | Not full isolation |

## Decision

**E immediately, then B:** Revoke cross-team write grants now; fulfillment read-only on order tables. Stand up fulfillment DB; CDC from Order → fulfillment projection. Remove FKs across ownership lines; enforce in Order service API for state changes fulfillment needs (`mark_shippable`).

## Reasoning

Separate services + shared DB = **distributed monolith with worse failure modes**. Fulfillment’s consistency needs match async projection. Migration scheduling is theater if lock storms continue.

## Risks

- CDC lag during big sales  
- Hidden SQL joins in admin tools  
- Dual-write temptation during transition  

## Migration

1. Inventory grants; kill fulfillment WRITEs on order tables.  
2. Expand: fulfillment DB schema; backfill snapshot.  
3. CDC continuous; validate counts.  
4. Flip fulfillment reads to local DB.  
5. Replace cross-schema FKs with service validation.  
6. Drop fulfillment access to Order DB.  

## Success metrics

- Checkout timeouts from fulfillment locks → 0  
- Cross-DB credentials from fulfillment → 0  
- CDC lag p99 < 30s  
- Migration ownership: Order migrations never block fulfillment deploy  

Related: [../topics/cross-team-architecture.md](../topics/cross-team-architecture.md), [../topics/migration-strategy.md](../topics/migration-strategy.md).

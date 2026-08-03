# Databases

Choose storage from **access patterns**, consistency, and ops — not fashion.

## Decision matrix

| Need | Lean toward | Why |
|------|-------------|-----|
| Rich transactions, joins, constraints | Relational (Postgres) | Invariants in one place |
| Huge key-value / wide rows | Dynamo-style / Cassandra | Partition + HA |
| Document flexibility | Document DB | Evolving schema, hierarchical |
| Full-text / secondary query | Search (OpenSearch) | Inverted index |
| Time series / metrics | TSDB | Compression, retention |
| Blobs | Object store (S3) | Cost, durability |
| Graph relations | Graph DB / carefully modeled SQL | Traversals |
| Ledger / audit | Append-only SQL/event store | Immutability |

## OLTP vs OLAP

Don’t run heavy analytics on primary OLTP. CDC/queue → warehouse/lake. Protects latency SLO.

## Indexes and write amplification

Every secondary index slows writes and costs storage. Estimate `row × (1 + indexes) × growth`.

## Transactions across services

Avoid distributed 2PC as default. Prefer: single aggregate transaction, **outbox**, saga with compensation.

## Connection pooling

App replicas × pool size can crush DB `max_connections`. Central poolers (PgBouncer) or budget pools explicitly.

## PE checklist when proposing a DB

1. Primary key / partition key  
2. Read queries (and whether scatter-gather)  
3. Consistency on read-your-writes  
4. Backup/PITR  
5. Migration story  

Related: [partitioning.md](./partitioning.md), [replication.md](./replication.md), [consistency.md](./consistency.md).

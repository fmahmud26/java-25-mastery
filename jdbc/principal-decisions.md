# Principal Engineer Decisions (JDBC)

## 1) DataSource + pool is mandatory in services

No raw `DriverManager` in business code. Centralize config, metrics, passwords.

## 2) Pool size is a capacity plan

```text
fleet_pools ≤ DB max_connections − headroom
```

Document per service. Alert on pending threads / timeouts.

## 3) Transactions end before remote I/O

Outbox / idempotent state machines for payments. Never “hold row locks while calling Stripe.”

## 4) PreparedStatement is policy

Lint/SAST ban. Dynamic identifiers allowlisted.

## 5) Isolation is a product choice

Default RC is fine for many apps; inventory/ledger need explicit concurrency control (constraints, versions, `FOR UPDATE`), not blind SERIALIZABLE everywhere.

## 6) Batch with chunking and metrics

Order lines batched; monitor `BatchUpdateException`.

## 7) Observability

Pool metrics, query latency histograms, deadlock counters, leak detection in non-prod.

## Anti-decisions

- Pool = 1000 because virtual threads  
- Auto-commit multi-step money  
- Catch `SQLException` log and continue without rollback  
- `SELECT *` huge exports into memory  

### Related

[scenarios.md](./scenarios.md) · [connection-pooling.md](./connection-pooling.md) · [interview.md](./interview.md)

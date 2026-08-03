# Deadlocks (JDBC / DB)

## Mental Model

Two transactions lock resources in opposite order → wait forever → DB aborts one.

```text
Tx1: lock row A → wait row B
Tx2: lock row B → wait row A
```

## Symptoms

`SQLException` / vendor deadlock error; retries spike; latency spikes.

## Inventory / Order Example

```text
Tx1: lock order 10 → lock SKU X
Tx2: lock SKU X → lock order 10
```

## Mitigations

| Tactic | Detail |
|--------|--------|
| Consistent lock order | Always SKU then order (document!) |
| Shorter transactions | Less overlap |
| Lower isolation if safe | Fewer range locks |
| Retry deadlocked tx | Idempotent business ops |
| Avoid lock escalation patterns | Huge batches |

## Debug

DB deadlock graphs (Postgres logs, MySQL `SHOW ENGINE INNODB STATUS`); app statement logs; reduce concurrency of conflicting paths.

### Related

[isolation-levels.md](./isolation-levels.md) · [transaction-bugs.md](./transaction-bugs.md) · [debugging.md](./debugging.md)

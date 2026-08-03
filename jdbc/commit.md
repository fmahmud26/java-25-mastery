# Commit

## Mental Model

`commit()` makes the current transaction’s changes durable (per DB durability settings) and visible per isolation rules.

## Mechanism

```java
conn.setAutoCommit(false);
// DML…
conn.commit(); // success path
```

With **auto-commit true** (often default), each statement is its own transaction — fine for single statements; wrong for multi-step money/inventory.

## Production

**Order processing:** commit after inventory reserve + order insert succeed together.  
If you commit after inventory but fail before order row → inconsistency (bug).

## PE Note

Idempotent retries after commit need business keys — commit means “done” from DB’s view.

### Related

[rollback.md](./rollback.md) · [transactions.md](./transactions.md)

# Transactions

## Mental Model

```text
BEGIN (implicit when auto-commit false)
  statement(s)
COMMIT   → durable effects
ROLLBACK → undo uncommitted work
```

ACID at the DB; JDBC controls the **session** boundary via `Connection`.

## Mechanism

```java
conn.setAutoCommit(false);
try {
    debit(conn, from, amount);
    credit(conn, to, amount);
    conn.commit();
} catch (Exception e) {
    conn.rollback();
    throw e;
} finally {
    conn.setAutoCommit(true); // if returning to pool, prefer pool reset
}
```

With pools, ensure connection is reset (auto-commit, isolation) on return — Hikari does this when configured properly.

## Production Scenarios

| Domain | Tx boundary |
|--------|-------------|
| **Payment** | Insert payment attempt + update order status atomically; external PSP **outside** or via outbox |
| **Order** | Create order lines + reserve inventory |
| **Inventory** | Conditional update stock; commit only if rowcount OK |

## Rules

- Keep transactions **short** (no remote HTTP inside).  
- One logical business operation ≈ one transaction (unless sagas).  
- Know isolation ([isolation-levels.md](./isolation-levels.md)).

## Bugs

Partial commit, missing rollback, auto-commit left on → [transaction-bugs.md](./transaction-bugs.md).

### Related

[commit.md](./commit.md) · [rollback.md](./rollback.md) · [deadlocks.md](./deadlocks.md)

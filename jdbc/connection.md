# Connection

## Mental Model

```text
Connection = session to the DB
  - default auto-commit often ON
  - holds transaction state when auto-commit OFF
  - must be closed (returned to pool)
```

## Mechanism

```java
try (Connection c = dataSource.getConnection()) {
    c.setAutoCommit(false);
    try {
        // work
        c.commit();
    } catch (SQLException e) {
        c.rollback();
        throw e;
    }
}
```

| API | Role |
|-----|------|
| `createStatement` / `prepareStatement` | SQL |
| `setAutoCommit` / `commit` / `rollback` | Tx |
| `setTransactionIsolation` | Isolation |
| `setNetworkTimeout` / client info | Ops |

## Production Rules

- Never hold a connection across external HTTP calls (payment PSP).  
- One connection per unit of work when possible; don’t share across threads.  
- `Connection` from a pool is usually a **proxy** — `close()` returns it.

## Scenarios

**Payment:** open connection → insert payment row + update order in one tx → commit → then call PSP (or outbox pattern).  
**Inventory:** short connection for `UPDATE stock SET qty = qty - ? WHERE … AND qty >= ?`.

## Leaks

Missing `close` / swallowed exceptions → [connection-leaks.md](./connection-leaks.md).

### Related

[transactions.md](./transactions.md) · [connection-pooling.md](./connection-pooling.md)

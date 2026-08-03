# Isolation Levels

## Mental Model

Isolation controls what concurrent transactions can see — trade **consistency** vs **concurrency**.

```text
READ UNCOMMITTED  →  READ COMMITTED  →  REPEATABLE READ  →  SERIALIZABLE
   more anomalies                         fewer anomalies
   more concurrency                       more locking / aborts
```

## JDBC API

```java
conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
```

| Constant | Typical intent |
|----------|----------------|
| `TRANSACTION_READ_UNCOMMITTED` | Allow dirty reads (rare in prod) |
| `TRANSACTION_READ_COMMITTED` | No dirty reads — common default |
| `TRANSACTION_REPEATABLE_READ` | Stable read rows (DB-specific phantoms) |
| `TRANSACTION_SERIALIZABLE` | Strongest — more conflicts |

**Critical:** Postgres, MySQL/InnoDB, Oracle differ in exact anomaly prevention. Read your DB docs — don’t memorize JDBC names as universal behavior.

## Production Choices

| Scenario | Common choice |
|----------|----------------|
| Payment capture row | RC + tight constraints / selective locking |
| Inventory decrement | Conditional `UPDATE` + RC often enough |
| Financial reports snapshot | RR or serializable / snapshot as DB allows |

## Anomalies

See [isolation-anomalies.md](./isolation-anomalies.md).

### Related

[transactions.md](./transactions.md) · [deadlocks.md](./deadlocks.md) · [principal-decisions.md](./principal-decisions.md)

# JDBC — Production Deep Guide

JDBC is how Java talks to relational databases. ORMs still sit on it — Principal engineers debug **connections, SQL, transactions, and pools** here.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Mental map

```text
App → DataSource (pool) → Connection → PreparedStatement → ResultSet
                         ↘ transactions: commit / rollback / isolation
```

## Study path

1. [jdbc-architecture](./jdbc-architecture.md) · [driver](./driver.md) · [connection](./connection.md)  
2. [statement](./statement.md) · [preparedstatement](./preparedstatement.md) · [resultset](./resultset.md)  
3. [transactions](./transactions.md) · [commit](./commit.md) · [rollback](./rollback.md) · [isolation-levels](./isolation-levels.md)  
4. [batch-operations](./batch-operations.md) · [connection-pooling](./connection-pooling.md)  
5. Failures: [sql-injection](./sql-injection.md) · [connection-leaks](./connection-leaks.md) · [pool-exhaustion](./pool-exhaustion.md) · [transaction-bugs](./transaction-bugs.md) · [deadlocks](./deadlocks.md) · [isolation-anomalies](./isolation-anomalies.md)  
6. Practice: [scenarios](./scenarios.md) · [debugging](./debugging.md) · [principal-decisions](./principal-decisions.md) · [interview](./interview.md)

## One-line PE rule

**Close what you open, bind what you interpolate, keep transactions short, and size the pool to the database — not to thread fashion.**

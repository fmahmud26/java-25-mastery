# Rollback

## Mental Model

`rollback()` aborts the current transaction — uncommitted changes vanish.

## Mechanism

```java
conn.setAutoCommit(false);
try {
    // work
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
    throw e;
}
```

`rollback(Savepoint)` for partial undo inside a long tx (use sparingly).

## Production

**Payment:** DB changes roll back if ledger write fails; do not leave “paid” without payment row.  
After rollback, connection can start a new transaction; don’t assume ResultSets still valid.

## Pitfalls

- Catching exception and forgetting rollback with auto-commit off → connection poisoned / pool contamination.  
- Rolling back only in some catch branches.

### Related

[commit.md](./commit.md) · [transaction-bugs.md](./transaction-bugs.md)

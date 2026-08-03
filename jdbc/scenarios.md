# Production Scenarios

## 1) Payment

**Goal:** record payment attempt + update order without lying about money.

```text
Recommended shape:
  insert payment INTENT (unique idempotency key) — commit
  call PSP
  update payment + order — commit (idempotent handler)
```

Avoid: PSP call inside open DB transaction. Use PreparedStatement for all writes. Pool sized for peak **DB** work, not PSP latency.

**Bugs to hunt:** double charge (lost update), injection on search, rollback forgotten.

---

## 2) Order Processing

Create order header + lines (batch) + reserve inventory in **one** transaction.

```java
conn.setAutoCommit(false);
insertOrder(conn, order);
batchInsertLines(conn, lines);
for (Line l : lines) reserveStock(conn, l); // conditional update
conn.commit();
```

On failure: rollback entire order. High concurrency: consistent lock order on SKUs.

---

## 3) Inventory

```sql
UPDATE inventory SET qty = qty - ?
 WHERE sku = ? AND qty >= ?
```

Check `executeUpdate() == 1`. Isolation alone may not save you — use conditional updates.

---

## 4) High Concurrency

Many app threads/VTs → small pool → waiters. Metrics: pool active/pending, query p99, DB locks.

**PE move:** fix SQL and pool math before “add more pods.”

### Related

[transaction-bugs.md](./transaction-bugs.md) · [pool-exhaustion.md](./pool-exhaustion.md) · [deadlocks.md](./deadlocks.md)

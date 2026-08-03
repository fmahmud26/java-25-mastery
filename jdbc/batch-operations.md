# Batch Operations

## Mental Model

Send many parameterized executions as one batch to cut network round-trips.

## Mechanism

```java
try (PreparedStatement ps = conn.prepareStatement(
        "insert into order_lines(order_id, sku, qty) values (?, ?, ?)")) {
    for (Line line : lines) {
        ps.setLong(1, orderId);
        ps.setString(2, line.sku());
        ps.setInt(3, line.qty());
        ps.addBatch();
    }
    int[] counts = ps.executeBatch();
}
```

Prefer batch inside an explicit transaction for all-or-nothing line inserts.

## Production

**Order processing:** insert 50 lines via batch vs 50 round-trips.  
**Inventory:** batch adjustments carefully — still need correct per-SKU predicates.

## Pitfalls

- Huge batches → memory / DB locks; chunk (e.g. 500–1000).  
- Ignoring `executeBatch` counts / `BatchUpdateException`.  
- Driver `rewriteBatchedStatements` (MySQL) — understand vendor behavior.

### Related

[preparedstatement.md](./preparedstatement.md) · [scenarios.md](./scenarios.md)

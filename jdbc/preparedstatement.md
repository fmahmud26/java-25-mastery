# PreparedStatement

## Mental Model

SQL with `?` placeholders — **code separated from data**. Primary defense against SQL injection; basis for batching.

## Mechanism

```java
String sql = """
    update inventory
       set qty = qty - ?
     where sku = ? and qty >= ?
    """;
try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setInt(1, count);
    ps.setString(2, sku);
    ps.setInt(3, count);
    int updated = ps.executeUpdate();
    if (updated != 1) throw new InsufficientStock(sku);
}
```

| Method | Use |
|--------|-----|
| `executeQuery` | SELECT → ResultSet |
| `executeUpdate` | DML — row count |
| `execute` | Mixed / unknown |
| `addBatch` / `executeBatch` | [batch-operations.md](./batch-operations.md) |

## Internals

Driver sends parameter values out-of-band from SQL text (protocol-dependent). Server may cache plans for identical SQL text.

## Production

- Always for variable predicates.  
- Reuse one `PreparedStatement` inside a loop/batch on the same connection.  
- `ORMs` still must parameterize — verify generated SQL.

## Anti-pattern

```java
// BAD — injection
st.execute("select * from users where name = '" + name + "'");
```

### Related

[sql-injection.md](./sql-injection.md) · [batch-operations.md](./batch-operations.md) · [resultset.md](./resultset.md)

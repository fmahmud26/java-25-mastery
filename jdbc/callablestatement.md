# CallableStatement

For stored procedures / functions: `CallableStatement` extends PreparedStatement with `{call …}` escape syntax and OUT params.

```java
try (CallableStatement cs = conn.prepareCall("{call allocate_inventory(?, ?)}")) {
    cs.setString(1, sku);
    cs.setInt(2, qty);
    cs.execute();
}
```

Prefer app-side SQL + transactions unless the org standardizes on procedures. Same rules: bind params, pool, short tx.

### Related

[preparedstatement.md](./preparedstatement.md) · [transactions.md](./transactions.md)

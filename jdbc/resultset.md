# ResultSet

## Mental Model

```text
Cursor over rows from executeQuery
  next() → move
  getXxx(column) → read
  close with try-with-resources
```

## Mechanism

```java
try (ResultSet rs = ps.executeQuery()) {
    while (rs.next()) {
        long id = rs.getLong("id");
        String status = rs.getString("status");
        // map to domain
    }
}
```

| Concern | Practice |
|---------|----------|
| Direction | Default forward-only |
| Fetch size | `ps.setFetchSize(n)` — tune for large scans |
| Streaming | Don’t load 1M rows into a `List` blindly |
| NULL | `wasNull()` after get for primitives |

## Production

**Order export:** stream `ResultSet` to CSV writer; don’t `SELECT *` without need. Hold connection until stream finishes — or paginate/keyset.

## Pitfalls

Using `ResultSet` after Statement/Connection closed; scrolling result sets without need (extra cost).

### Related

[preparedstatement.md](./preparedstatement.md) · [connection.md](./connection.md)

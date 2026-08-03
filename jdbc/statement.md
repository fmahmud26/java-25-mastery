# Statement

## Mental Model

`Statement` executes static SQL strings. Fine for fixed DDL/admin; **dangerous** for user input.

## Mechanism

```java
try (Statement st = conn.createStatement()) {
    st.executeUpdate("create table if not exists audit (id bigint primary key)");
}
```

## Why Prefer PreparedStatement

| Statement + concat | PreparedStatement |
|--------------------|-------------------|
| Injection risk | Bound parameters |
| Plan less reusable | Often better reuse |
| Type handling manual | Setters |

## Production

Ban string-concatenated user input. Dynamic **identifiers** (ORDER BY column) need allowlists — cannot bind as `?`.

### Related

[preparedstatement.md](./preparedstatement.md) · [sql-injection.md](./sql-injection.md)

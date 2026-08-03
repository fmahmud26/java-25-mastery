# JDBC Architecture

## Mental Model

```text
Application code
    ↓  java.sql / javax.sql
JDBC API (interfaces)
    ↓
JDBC Driver (vendor JAR)
    ↓ network protocol
Database server
```

Your code depends on **interfaces**; the driver supplies implementations.

## Core Pieces

| Piece | Role |
|-------|------|
| `Driver` / `DriverManager` | Register/load drivers; legacy connect |
| `DataSource` | Preferred way to get connections (pools) |
| `Connection` | DB session; transaction boundary |
| `Statement` / `PreparedStatement` / `CallableStatement` | Execute SQL |
| `ResultSet` | Cursor over rows |
| `SQLException` / `SQLWarning` | Errors / warnings |

## Technical Mechanism

```java
try (Connection c = dataSource.getConnection();
     PreparedStatement ps = c.prepareStatement(
             "select id, status from orders where id = ?")) {
    ps.setLong(1, orderId);
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return new Order(rs.getLong("id"), rs.getString("status"));
        }
    }
}
```

Try-with-resources closes ResultSet → Statement → Connection (returns to pool if pooled).

## Production Implications

- App servers / Spring: inject `DataSource`, never scatter `DriverManager.getConnection` in domain code.  
- Driver JAR must match DB major version.  
- Timeouts: connect, socket, query — set explicitly for payments/orders.

## Interview / PE

Draw the stack. Why `DataSource` over `DriverManager`? Where do transactions live?

### Related

[driver.md](./driver.md) · [connection.md](./connection.md) · [connection-pooling.md](./connection-pooling.md)

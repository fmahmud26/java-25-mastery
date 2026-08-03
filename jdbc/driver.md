# JDBC Driver

## Mental Model

The **driver** translates JDBC calls into the database wire protocol and maps SQL types ↔ Java types.

## Types (classic taxonomy)

| Type | Idea |
|------|------|
| Type 4 (common) | Pure Java → DB protocol (e.g. PostgreSQL JDBC, MySQL Connector/J) |
| Others | Bridge/native — rarely what you pick for new systems |

## Mechanism

```text
jdbc:postgresql://host:5432/db
jdbc:mysql://host:3306/db
```

URL format is vendor-specific. Modern drivers auto-register via `ServiceLoader` (`META-INF/services/java.sql.Driver`).

```java
// Prefer DataSource configuration over Class.forName in app code
HikariConfig cfg = new HikariConfig();
cfg.setJdbcUrl("jdbc:postgresql://db:5432/orders");
cfg.setUsername("app");
cfg.setPassword(secret);
```

## Production

- Pin driver version; upgrade deliberately (type mapping / auth changes).  
- TLS (`sslmode`) for anything beyond localhost.  
- Rewrite batch / fetch size properties are driver-specific — read vendor docs.

## Failure Modes

Wrong driver / URL → `SQLException` at connect. Auth plugin mismatches (MySQL) → connect failures under load after idle.

### Related

[connection.md](./connection.md) · [jdbc-architecture.md](./jdbc-architecture.md)

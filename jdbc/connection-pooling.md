# Connection Pooling

## Mental Model

```text
Physical connections are expensive and scarce.
Pool lends Connection proxies; close() returns them.
```

## Mechanism (HikariCP-style)

```java
HikariConfig cfg = new HikariConfig();
cfg.setJdbcUrl(url);
cfg.setUsername(user);
cfg.setPassword(pass);
cfg.setMaximumPoolSize(20);
cfg.setMinimumIdle(5);
cfg.setConnectionTimeout(1_000);
cfg.setValidationTimeout(500);
DataSource ds = new HikariDataSource(cfg);
```

## Sizing (PE)

```text
Σ (pods × poolSize) + admin < database max_connections
throughput ≈ connections / mean_query_time   (rough)
```

Virtual threads do **not** justify 10k pool size — DB will melt. See [pool-exhaustion.md](./pool-exhaustion.md).

## Practices

| Do | Don’t |
|----|-------|
| Try-with-resources always | Hold across PSP HTTP |
| Fail-fast `connectionTimeout` | Infinite wait for connection |
| Leak detection in staging | Ignore pool metrics |
| Validate connections | Assume forever-healthy sockets |

## High Concurrency

Many app threads/VTs wait on a **small** pool — correct. Admission control = pool + timeouts.

### Related

[connection.md](./connection.md) · [pool-exhaustion.md](./pool-exhaustion.md) · [principal-decisions.md](./principal-decisions.md)

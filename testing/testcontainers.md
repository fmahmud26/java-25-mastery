# Testcontainers

Run **real** dependencies in Docker from JUnit — Postgres, Kafka, LocalStack, etc.

## Mental Model

```text
@Testcontainers → start container → wire JDBC/URL → test → discard
```

```java
@Container
static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("orders");

@BeforeAll
static void datasource() {
    System.setProperty("jdbc.url", postgres.getJdbcUrl());
    // or DynamicPropertySource in Spring
}
```

## Why

Mocks don’t enforce check constraints, isolation, or SQL dialect quirks.

## Practices

- Pin image versions  
- Reuse containers where supported to speed CI  
- Parallelism: careful with fixed ports  
- CI needs Docker  

## Service Example

Order + inventory schema migrations applied; repository IT against Postgres 16.

### Related

[tools/testcontainers.md](./tools/testcontainers.md) · [integration-testing.md](./integration-testing.md)

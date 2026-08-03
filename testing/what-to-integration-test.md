# What Should Be Integration Tested?

## Yes — where fakes lie

| Target | Why |
|--------|-----|
| SQL / repositories | Dialect, constraints, transactions |
| HTTP adapters | Serialization, status handling, headers |
| Messaging listeners | Ack/nack, deserialization |
| Migrations + schema | Flyway/Liquibase against real DB |
| Security config slices | Filters actually wired |
| Idempotency / unique keys | DB enforces what mocks won’t |

```java
@Testcontainers
@Test
void reservesStockConditionally() {
    // real Postgres via Testcontainers
    int updated = repo.reserve("SKU-1", 2);
    assertEquals(1, updated);
}
```

## Scope control

One module’s outbound adapter + real dependency — not the entire microservice mesh every time.

## When e2e instead

Full user journey across many services — few tests, critical paths only.

### Related

[integration-testing.md](./integration-testing.md) · [testcontainers.md](./testcontainers.md) · [contract-testing.md](./contract-testing.md)

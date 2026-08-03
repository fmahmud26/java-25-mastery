# Integration Testing

Prove **adapters + real infrastructure** work together.

## Mental Model

```text
App code + real Postgres/Kafka/LocalStack
  (often Testcontainers)
  → assert state in DB / messages published
```

```java
@SpringBootTest
@Testcontainers
class OrderRepositoryIT {
    @Container
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:16");

    @Test
    void insertsOrderWithLines() {
        orderRepo.save(order);
        assertEquals(2, jdbc.queryForObject(
            "select count(*) from order_lines where order_id=?", Integer.class, order.id()));
    }
}
```

## Practices

- Isolate schemas / clean data between tests  
- Prefer Testcontainers over shared flaky “dev DB”  
- Keep suite parallel-safe  
- Tag `IT` separately in CI if slower  

## Service Example

Inventory `UPDATE … WHERE qty >= ?` under concurrency — integration test catches lost updates mocks miss.

### Related

[what-to-integration-test.md](./what-to-integration-test.md) · [testcontainers.md](./testcontainers.md)

# Test Doubles

Umbrella term (Gerard Meszaros) for stand-ins replacing real dependencies.

| Double | Role |
|--------|------|
| **Dummy** | Passed but never used |
| **Stub** | Returns canned data |
| **Fake** | Working lightweight impl (in-memory repo) |
| **Mock** | Verifies interactions |
| **Spy** | Partial real + verify |

```java
// Stub
when(payments.charge(any())).thenReturn(ChargeResult.ok(txId));

// Fake
class InMemoryOrderRepo implements OrderRepository {
    private final Map<OrderId, Order> store = new HashMap<>();
    // real behavior, no SQL
}
```

## Guidance

Prefer **fakes** for repositories you own when logic is storage-shaped. Prefer **stubs** for remote I/O. Use **mocks** when the **interaction** is the specification (e.g. “must publish event once”).

### Related

[mocking.md](./mocking.md) · [what-not-to-mock.md](./what-not-to-mock.md)

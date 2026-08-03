# What Should NOT Be Mocked?

## Don’t mock

| Thing | Why |
|-------|-----|
| **Your domain entities / value objects** | You’re testing fiction |
| **The class under test** | Pointless |
| **Simple DTOs / records** | No behavior |
| **Real DB for SQL correctness** | Mock JDBC lies about constraints |
| **Clock only if you forget to inject** | Prefer fake clock, not mock everything |
| **Third-party types you don’t understand** | Mocking unfamiliar APIs encodes wrong assumptions |

## Mock (or stub) at boundaries you own

| Boundary | Example |
|----------|---------|
| Ports | `PaymentGateway`, `InventoryClient` |
| Clock / UUID / Random | Deterministic tests |
| Feature flags | Toggle paths |

## Rule of thumb

> If the bug would be “wrong SQL / wrong JSON / wrong transaction,” don’t mock that layer — integrate it.  
> If the bug would be “wrong business branch,” unit-test with stubs for IO.

### Related

[mocking.md](./mocking.md) · [test-doubles.md](./test-doubles.md) · [how-tests-fail-in-production.md](./how-tests-fail-in-production.md)

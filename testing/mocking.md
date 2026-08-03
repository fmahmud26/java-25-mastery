# Mocking

Simulate collaborators and optionally **verify** calls (Mockito, etc.).

## Mechanism

```java
@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
    @Mock PaymentGateway payments;
    @Mock InventoryPort inventory;
    @InjectMocks CheckoutService checkout;

    @Test
    void chargesAfterReserve() {
        when(inventory.reserve(any())).thenReturn(Reservation.ok());
        when(payments.charge(any())).thenReturn(ChargeResult.ok("tx"));

        checkout.checkout(cmd);

        verify(inventory).reserve(any());
        verify(payments).charge(any());
    }
}
```

## Do / Don’t

| Do | Don’t |
|----|-------|
| Mock ports/interfaces at edges | Mock concrete domain entities |
| Stub return values for state tests | Over-`verify` call order/getters |
| Reset clear intent | `mock(Everything.class)` |
| Fail on unnecessary stubbing (strict) | Silent wrong stubs |

## Failure Mode

Tests green, production red — mocked gateway never matched real HTTP/SQL. See [how-tests-fail-in-production.md](./how-tests-fail-in-production.md).

### Related

[tools/mockito.md](./tools/mockito.md) · [test-doubles.md](./test-doubles.md) · [what-not-to-mock.md](./what-not-to-mock.md)

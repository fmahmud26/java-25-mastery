# What Should Be Unit Tested?

## Yes — high value

| Target | Why |
|--------|-----|
| Domain rules | Pricing, eligibility, state transitions |
| Pure functions / mappers with logic | Deterministic, cheap |
| Validation | Edge cases, boundary values |
| Algorithms | Retry/backoff policy math, parsers |
| Error mapping | Exception → domain error |

```java
@Test
void cannotShipUnpaidOrder() {
    Order order = Order.newDraft(items);
    assertThrows(IllegalStateException.class, order::ship);
}
```

## Characteristics of a good unit test

- No real network/DB/filesystem (unless filesystem is the unit under test with temp dirs)  
- Stable clock/random injected when needed  
- Names describe **behavior**  
- Fails for one clear reason  

## Thin wrappers — usually skip or smoke

Getters, pure framework config, generated code — low ROI unless they hide logic.

### Related

[unit-testing.md](./unit-testing.md) · [what-not-to-mock.md](./what-not-to-mock.md)

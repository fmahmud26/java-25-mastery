# Assertions

Make failures **readable** — assert on business meaning.

## JUnit

```java
assertEquals(expected, actual);
assertTrue(order.isPaid());
assertThrows(InsufficientStock.class, () -> inventory.reserve(sku, 99));
```

## AssertJ (fluent)

```java
assertThat(order.status()).isEqualTo(PAID);
assertThat(lines).extracting(Line::sku).containsExactly("A", "B");
```

## Practices

| Do | Don’t |
|----|-------|
| Assert observables | Assert mocks were called *instead of* state (when state matters) |
| Custom messages when helpful | Huge expected dumps without structure |
| One behavior per test | Twenty unrelated asserts |

### Related

[tools/assertj.md](./tools/assertj.md) · [unit-testing.md](./unit-testing.md)

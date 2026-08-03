# Parameterized Tests

Run the same test logic across many inputs — great for rules tables.

```java
@ParameterizedTest
@CsvSource({
    "0, false",
    "17, false",
    "18, true",
    "67, true",
    "68, false"
})
void employeeAgeRules(int age, boolean eligible) {
    assertEquals(eligible, EmployeePolicy.isEligible(age));
}
```

Also: `@ValueSource`, `@MethodSource`, `@EnumSource`.

## When to use

Boundary tables, enum × behavior matrices, validation catalogs.

## When not

Totally different arrange/act flows — separate tests read better.

### Related

[unit-testing.md](./unit-testing.md) · [tools/junit.md](./tools/junit.md)

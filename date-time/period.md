# Period

Calendar amount — years, months, days (not exact seconds).

## Mental Model

```text
Period.ofMonths(1) + Jan 31 → Feb 28/29 (calendar math)
≠ Duration.ofDays(30)
```

## Java 25 Examples

```java
Period trial = Period.ofDays(14);
Period contract = Period.ofYears(1);

LocalDate start = LocalDate.of(2026, 1, 31);
LocalDate end = start.plus(Period.ofMonths(1)); // 2026-02-28

Period age = Period.between(dob, LocalDate.now(zone));
int years = age.getYears();
```

## Production Systems

| System | Use |
|--------|-----|
| Orders | Return window “30 days” as calendar policy |
| Payments | Statement cycles, card expiry month |
| International | Age checks, residency periods |
| Jobs | “First day of next month” with adjusters + Period |

```java
LocalDate invoiceMonthEnd = LocalDate.now(zone)
        .with(TemporalAdjusters.lastDayOfMonth());
```

## Common Bug

Billing “every 30 days” with Period.ofDays(30) vs monthly anniversary — product ambiguity becomes money bugs. Define civil vs elapsed explicitly.

### Related

[duration.md](./duration.md) · [localdate.md](./localdate.md)

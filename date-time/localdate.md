# LocalDate

Date-only (year-month-day) — **no time, no zone**.

## Mental Model

```text
2026-08-03  =  a calendar day in ISO chronology
not “midnight UTC”, not an Instant
```

## Java 25 Examples

```java
LocalDate today = LocalDate.now(ZoneId.of("Asia/Dhaka")); // explicit zone for “today”
LocalDate due = LocalDate.of(2026, 8, 10);
LocalDate parsed = LocalDate.parse("2026-08-03");

due.plusDays(2);
due.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
boolean overdue = LocalDate.now(clock).isAfter(due);
```

Inject `Clock` in production code for testability: `LocalDate.now(clock)`.

## Production Systems

| System | Use |
|--------|-----|
| Orders | Delivery date, return window end date |
| Payments | Settlement **business date** (cutover calendars) |
| International users | DOB, local holiday calendars |
| Scheduled jobs | “Run on calendar date X” (still schedule trigger via Instant) |

```java
public boolean isReturnOpen(Order o, LocalDate today) {
    return !today.isAfter(o.deliveredOn().plusDays(30));
}
```

## Common Bug

`LocalDate.now()` without zone → depends on JVM default; server in UTC vs office in Dhaka → wrong “business today.”

## Interview Trap

“Is LocalDate an Instant at midnight?” — **No.** Combining with time requires an explicit zone to become an Instant.

### Related

[localtime.md](./localtime.md) · [period.md](./period.md) · [utc.md](./utc.md)

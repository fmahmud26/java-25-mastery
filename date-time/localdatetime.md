# LocalDateTime

Date + time **without** zone/offset — a naive wall clock.

## Mental Model

```text
2026-08-03T14:30:00  =  numbers on a clock face, location unknown
Dangerous as “event time” in distributed systems
```

## Java 25 Examples

```java
LocalDateTime ldt = LocalDateTime.of(2026, 8, 3, 14, 30);
LocalDateTime parsed = LocalDateTime.parse("2026-08-03T14:30:00");

ZonedDateTime zdt = ldt.atZone(ZoneId.of("Asia/Dhaka"));
Instant instant = zdt.toInstant();
```

## When OK vs Dangerous

| OK | Dangerous |
|----|-----------|
| UI form before user zone applied | DB column for payment capture time |
| Recurring local schedule template | Cross-region audit logs |
| “Meeting at 3pm” before attaching city | Microservice sync timestamps |

## Production — orders

Prefer: store `Instant paidAt`; show `paidAt.atZone(userZone)`.  
If you must store local civil time (cinema showtimes), also store `ZoneId`.

## Incident Pattern

Server writes `LocalDateTime.now()` in JVM default zone; another service parses as UTC → **hours of payment skew**, reconciliation breaks.

### Related

[instant.md](./instant.md) · [zoneddatetime.md](./zoneddatetime.md) · [timezone-bugs-and-incidents.md](./timezone-bugs-and-incidents.md)

# UTC

Coordinated Universal Time — the reference timeline for systems. In Java, **`Instant` and `ZoneOffset.UTC`** are your tools.

## Mental Model

```text
Store / transmit / compare  → UTC (Instant)
Display / business calendar → convert with ZoneId
```

## Java 25 Examples

```java
Instant utcNow = Instant.now(clock);
OffsetDateTime odt = utcNow.atOffset(ZoneOffset.UTC);
String wire = utcNow.toString(); // ...Z

// Explicit UTC wall fields when a protocol demands them
LocalDateTime utcFields = LocalDateTime.ofInstant(utcNow, ZoneOffset.UTC);
```

## Why UTC in Production

| Benefit | Detail |
|---------|--------|
| No DST gaps/overlaps | Sorting and diffs are safe |
| Multi-region agreement | All services share one meaning |
| Audit integrity | “What time did charge happen?” unambiguous |

## Production Patterns

```text
API inbound: parse Instant / OffsetDateTime → normalize to Instant
DB:          timestamptz / Instant
Logs:        ISO-8601 UTC
UI:          atZone(userZone) + formatter
Jobs:        cron in ZoneId → compute next Instant
```

## Anti-Patterns

- JVM default zone as implicit UTC  
- Mixing `LocalDateTime.now()` into audit tables  
- Storing offset strings without normalizing (`+06:00` vs `Z` chaos)

## Incident

Payment service in `Asia/Dhaka` writes local wall times; fraud service in `UTC` compares strings → false positives overnight.

### Related

[instant.md](./instant.md) · [time-zones.md](./time-zones.md) · [timezone-bugs-and-incidents.md](./timezone-bugs-and-incidents.md)

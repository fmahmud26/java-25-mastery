# OffsetDateTime

Date-time with a **fixed offset** from UTC (`+06:00`, `Z`) — not full region DST rules.

## Mental Model

```text
2026-08-03T14:30:00+06:00  =  instant + offset used to label it
Offset ≠ ZoneId (no historical DST transitions by region name)
```

## Java 25 Examples

```java
OffsetDateTime odt = OffsetDateTime.parse("2026-08-03T14:30:00+06:00");
Instant instant = odt.toInstant();
OffsetDateTime utc = instant.atOffset(ZoneOffset.UTC);

OffsetDateTime nowDhaka = OffsetDateTime.now(ZoneId.of("Asia/Dhaka"));
```

## Production Systems

| System | Use |
|--------|-----|
| APIs / JSON | Wire format with offset |
| Audit logs | Sometimes store offset as received |
| Payments | PSP payloads with offset → convert to Instant immediately |

Prefer converting to **Instant** at the boundary; don’t do arithmetic in mixed offsets without normalizing.

## OffsetDateTime vs ZonedDateTime

| | OffsetDateTime | ZonedDateTime |
|--|----------------|---------------|
| DST rules | No (fixed offset) | Yes (`Asia/Tokyo`, …) |
| Adding days across DST | Offset stays | Zone rules apply |
| Good for | Interchange | Local scheduling |

## Common Bug

Adding `Period.ofDays(1)` across a DST transition using OffsetDateTime with a summer offset — won’t adjust like zone rules would.

### Related

[zoneddatetime.md](./zoneddatetime.md) · [instant.md](./instant.md) · [time-zones.md](./time-zones.md)

# ZonedDateTime

Date-time with a **time region** (`ZoneId`) — applies DST and historical offset rules.

## Mental Model

```text
LocalDateTime + ZoneId → ZonedDateTime ↔ Instant
Gaps (spring forward) and overlaps (fall back) are real
```

## Java 25 Examples

```java
ZoneId zone = ZoneId.of("America/New_York");
ZonedDateTime zdt = ZonedDateTime.of(2026, 3, 8, 2, 30, 0, 0, zone);
// DST gap — Java adjusts per resolver (default: later offset / valid time)

Instant instant = zdt.toInstant();
ZonedDateTime userLocal = instant.atZone(ZoneId.of("Asia/Dhaka"));

ZonedDateTime next = zdt.plusDays(1); // zone rules respected
```

```java
// Schedule “every day 09:00 London”
LocalTime nine = LocalTime.of(9, 0);
ZonedDateTime nextFire = LocalDate.now(zone).atTime(nine).atZone(zone);
if (nextFire.toInstant().isBefore(Instant.now(clock))) {
    nextFire = nextFire.plusDays(1);
}
Instant trigger = nextFire.toInstant();
```

## Production Systems

| System | Use |
|--------|-----|
| Scheduled jobs | Cron-in-zone → next Instant |
| International users | Display + local business rules |
| Orders | “Deliver by local evening” windows |
| Payments | Rarely for capture time (use Instant); yes for statement cutover in bank local zone |

## DST Gaps & Overlaps

| Event | Issue |
|-------|-------|
| Spring forward | Local times that don’t exist |
| Fall back | Local times that exist twice |

Use `ZoneOffsetTransition` / careful with `withLaterOffsetAtOverlap()` when parsing ambiguous local times.

## Common Bug

Storing ZonedDateTime but stripping zone on serialization → silent wrong Instant.

### Related

[time-zones.md](./time-zones.md) · [instant.md](./instant.md) · [timezone-bugs-and-incidents.md](./timezone-bugs-and-incidents.md)

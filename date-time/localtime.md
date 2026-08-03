# LocalTime

Time-of-day only — **no date, no zone**.

## Mental Model

```text
14:30:00  =  wall-clock time on some unknown day/region
```

## Java 25 Examples

```java
LocalTime open = LocalTime.of(9, 0);
LocalTime close = LocalTime.of(17, 30);
LocalTime now = LocalTime.now(ZoneId.of("Europe/London"));

boolean openNow = !now.isBefore(open) && now.isBefore(close);
Duration untilClose = Duration.between(now, close); // careful past midnight
```

## Production Systems

| System | Use |
|--------|-----|
| Scheduled jobs | “Daily at 02:00 **in Zone X**” — store LocalTime + ZoneId |
| Orders | Store cutoff time (local) |
| International | Business hours per market |

```java
record MarketHours(ZoneId zone, LocalTime open, LocalTime close) {
    boolean isOpen(Instant instant) {
        LocalTime t = LocalTime.ofInstant(instant, zone);
        return !t.isBefore(open) && t.isBefore(close);
    }
}
```

## Common Bug

Comparing LocalTimes across zones without converting Instant first — “2am” in two regions aren’t the same moment.

### Related

[localdate.md](./localdate.md) · [localdatetime.md](./localdatetime.md) · [zoneddatetime.md](./zoneddatetime.md)

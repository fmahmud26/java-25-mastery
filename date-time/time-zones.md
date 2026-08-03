# Time Zones (`ZoneId` / `ZoneOffset`)

Region rules vs fixed offsets — the root of most production date bugs.

## Mental Model

```text
ZoneId  "Asia/Dhaka"     → rules + history (DST where applicable)
ZoneOffset "+06:00"      → fixed number only
ZoneId.of("UTC") / ZoneOffset.UTC
```

## Java 25 Examples

```java
ZoneId userZone = ZoneId.of(user.timezone()); // from profile IANA id
ZoneId deploy = ZoneId.systemDefault();       // don’t rely on this for business

Instant event = Instant.now(clock);
ZonedDateTime local = event.atZone(userZone);

ZoneRules rules = ZoneId.of("America/New_York").getRules();
List<ZoneOffsetTransition> transitions = rules.getTransitions(); // rare debug
```

## Production Guidance

| Do | Don’t |
|----|-------|
| Store IANA ids (`America/New_York`) | Store `EST` / `GMT+6` abbreviations |
| Convert at display edge | Run all servers forced to user local zone |
| Keep tzdb updated (OS/JDK) | Assume DST rules never change |
| Test critical zones | Assume Dhaka DST (it doesn’t) without checking |

## Systems

- **International users:** profile `ZoneId`; render Instant with it  
- **Scheduled jobs:** cron expression + zone  
- **Distributed services:** all nodes compare Instants only  
- **Payments:** settlement calendars often bank-local dates + Instant timestamps  

## Failure Modes

Missing tzdata update → wrong offset after government DST law change. Wrong id (`US/Eastern` legacy vs `America/New_York`) — prefer canonical.

### Related

[zoneddatetime.md](./zoneddatetime.md) · [utc.md](./utc.md) · [timezone-bugs-and-incidents.md](./timezone-bugs-and-incidents.md)

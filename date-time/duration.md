# Duration

Elapsed **time** on the timeline — seconds/nanos based (hours, minutes, days-as-24h).

## Mental Model

```text
Duration = machine elapsed (Instant to Instant)
Period   = calendar elapsed (LocalDate to LocalDate)
```

## Java 25 Examples

```java
Duration timeout = Duration.ofSeconds(30);
Duration sla = Duration.ofMinutes(5);
Duration gap = Duration.between(started, Instant.now(clock));

if (gap.compareTo(sla) > 0) metrics.slaBreach();

Instant deadline = Instant.now(clock).plus(timeout);
Thread.sleep(timeout.toMillis()); // prefer higher-level schedulers
```

`Duration.ofDays(1)` = **24 hours**, not “calendar day” (DST can make civil days ≠ 24h).

## Production Systems

| System | Use |
|--------|-----|
| Payments | Capture timeout, idempotency TTL, refund window as elapsed |
| Distributed | Lease duration, lock TTL, RPC deadline |
| Audit | “Retain raw payloads for 90 days” may be Period — policy choice |
| Jobs | Fixed-rate delays |

```java
public boolean withinIdempotencyWindow(Instant firstSeen, Instant now) {
    return Duration.between(firstSeen, now).compareTo(Duration.ofHours(24)) <= 0;
}
```

## Common Bug

Using Duration for “+1 month subscription” — use Period / `plusMonths` on dates.

### Related

[period.md](./period.md) · [instant.md](./instant.md)

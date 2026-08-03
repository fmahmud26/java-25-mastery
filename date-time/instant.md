# Instant

Absolute point on the timeline (UTC-based epoch). **Default type for events** in distributed systems.

## Mental Model

```text
Instant ≈ machine time (nanoseconds since 1970-01-01T00:00:00Z)
Same everywhere — no DST
```

## Java 25 Examples

```java
Instant now = Instant.now(clock);
Instant paidAt = Instant.parse("2026-08-03T08:30:00Z");
Instant fromMillis = Instant.ofEpochMilli(1_725_000_000_000L);

Duration latency = Duration.between(start, Instant.now(clock));
boolean expired = Instant.now(clock).isAfter(token.expiresAt());

ZonedDateTime forUser = paidAt.atZone(ZoneId.of("America/New_York"));
```

## Production Systems

| System | Use |
|--------|-----|
| Payments | `capturedAt`, `refundedAt`, idempotency window |
| Orders | `createdAt`, `paidAt`, `shippedAt` |
| Audit logs | Every security/mutation event |
| Distributed services | Spans, outbox `created_at`, lease expiry |
| Scheduled jobs | Next fire time as Instant (compute from zone rules) |

```java
public record PaymentCaptured(String paymentId, long cents, Instant at) { }

// TTL
if (Duration.between(payment.createdAt(), Instant.now(clock)).compareTo(Duration.ofMinutes(30)) > 0) {
    throw new PaymentExpiredException(payment.id());
}
```

## Storage

DB: `TIMESTAMP WITH TIME ZONE` / epoch millis mapping to Instant. JSON: ISO-8601 with `Z`.

## Common Bug

Serializing Instant as LocalDateTime string without `Z` → consumers assume local.

### Related

[utc.md](./utc.md) · [duration.md](./duration.md) · [zoneddatetime.md](./zoneddatetime.md)

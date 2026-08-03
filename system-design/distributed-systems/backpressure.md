# Backpressure

When downstream is saturated, **signal upstream to slow or drop** instead of buffering without bound (OOM / multi-hour lag / useless work).

## Mechanisms

| Layer | Signal |
|-------|--------|
| TCP | Windowing |
| HTTP | 429 / 503 + Retry-After |
| Queue | Lag metrics → autoscale → refuse ingest |
| Reactive streams | Request(n) credit |
| Thread pool | Queue bound + reject policy |
| Kafka | Consumer lag; producer buffer limits |

## Production scenario: notification blast

Marketing enqueues 50M emails; workers 100. Lag → 3 days; disk fills; transactional OTP shares topic → OTP delayed hours.

**Fix:** separate topics/quotas; ingress rate limit; priority lanes; bound producer; shed marketing first.

## Policies

| Policy | When |
|--------|------|
| Block / slow producer | Cannot lose (with care) |
| Drop oldest / sample | Telemetry |
| Reject new (fail fast) | User-facing with 429 |
| Spill to cold storage | Logs |

## Trade-offs

| Unbounded buffer | Hides problem until catastrophic |
| Aggressive reject | User-visible errors earlier — usually better |

## Principal interview angles

- “What happens when consumers stop for an hour?”  
- “Which traffic do you shed first?”  

Related: [scenarios/queue-overload.md](./scenarios/queue-overload.md), [retry.md](./retry.md), [fault-tolerance.md](./fault-tolerance.md).

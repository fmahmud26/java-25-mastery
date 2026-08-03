# Notification Platform

Multi-channel delivery at scale with preferences, retries, and provider quotas.

## Requirements

**Functional:** template send, multi-channel (email/SMS/push/in-app), preferences/opt-out, scheduled/digest, delivery status.  
**Non-functional:** high ingest; per-provider rate limits; at-least-once to providers; p99 accept API fast; eventual delivery.  
**Non-goals:** ESP IP warming team tooling as core design.

## Capacity estimation

```text
200M notifications/day ≈ 2.3k QPS avg → peak ×20 (campaigns) ≈ 46k QPS ingest
Fan-out 1.5 channels avg → ~70k provider QPS peak (needs shaping)
Payload 2KB → Kafka multi GB/day easy
Status updates similar order of magnitude
```

## Architecture

```text
Producers → Ingest API → Kafka (notifications)
                        → Preference service check (sync cache)
Consumers → Render → Channel routers → Provider adapters
                                   ├─ Email ESP
                                   ├─ SMS gateway
                                   └─ Push (FCM/APNs)
Status webhooks → Kafka → status store
Scheduler → digest topics
```

## Components

| Component | Role | Why |
|-----------|------|-----|
| Ingest API | Validate + enqueue | Buffer campaigns |
| Preference service | Opt-out | Compliance |
| Template service | Render | Safe escaping |
| Channel workers | Send | Scale per channel |
| Provider adapters | Quotas/CB | Vendor isolation |
| Status store | Delivery state | Support UX |

## Data flow

Accept → persist intent (or Kafka as buffer with dual-write care) → consumers respect prefs → render → rate-limited send → record attempt → webhook updates.  
**Priority lanes:** OTP/transactional vs marketing (separate topics/quotas).

## Data storage

| Data | Store |
|------|-------|
| Templates | SQL/doc + cache |
| Preferences | SQL + Redis | 
| Delivery attempts | Wide store / SQL partitioned by time |
| Kafka | Durability buffer |
| Suppression lists | Redis/SQL |

## Scaling

- Partition Kafka by `userId` (order per user) or `tenantId`  
- Autoscale consumers on lag  
- Per-provider token buckets (global Redis)  
- Isolate marketing from transactional clusters  

## Failure modes

| Failure | Mitigation |
|---------|------------|
| Provider 429 | Respect Retry-After; shape traffic; CB |
| Poison template | DLQ; don’t block partition forever |
| Preference stale | Short TTL; fail safe toward skip on uncertainty for marketing |
| Spike campaign | Admit to Kafka; control egress |
| Duplicate sends | Idempotency key per notification×channel |

## Observability

- Ingest QPS, lag by topic, send success, provider latency, opt-out skips  
- Alert: transactional lag SLO (e.g. OTP p99 < 10s) vs marketing lag  

## Security

- PII minimization; encrypt payloads at rest  
- Template injection safety  
- Tenant isolation; spoofing prevention on sender domains  
- Consent/audit for SMS  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Kafka buffer | Absorb spikes | Sync send in caller — couples product SLO to Twilio |
| Separate txn/marketing | Protect OTP | One queue — noisy neighbor |
| At-least-once + dedupe | Reality | Exactly-once fantasy to SMS |

## Evolution

1. Single channel email + queue  
2. Multi-channel + prefs + status  
3. Digests, experimentation, global multi-region egress  
4. Bring-your-own provider per tenant  

Related: [message-queues](../distributed-systems/message-queues.md), [rate-limiting](../distributed-systems/rate-limiting.md), [retry](../distributed-systems/retry.md).

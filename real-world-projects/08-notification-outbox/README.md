# 08 — Notification Outbox Platform

**Tier:** Scale path · **Demonstrates:** outbox pattern, concurrency/VT workers, persistence ports, observability, resilience, scalability thinking

## Problem

Product services must notify users (email/SMS) **reliably** after domain commits — without dual-write gaps (DB success + lost message). Implement the **transactional outbox** pattern in-process (DB transaction simulated), with workers, retries, idempotent delivery, and metrics. This is the bridge from monolith commits to async fan-out at scale.

## Requirements

**Functional**
- `recordAndEnqueue(NotificationIntent)` in one “transaction” with domain stub  
- Publisher drains outbox → channel adapters (Email/SMS fakes)  
- Idempotent delivery per `notificationId+channel`  
- Retry with backoff; DLQ after N  

**Non-functional**
- At-least-once to channels; exactly-once **effect** via dedupe  
- VT workers; bounded poll batch  
- Metrics: outbox lag age, sent, failed, dlq  
- Preferential lane for OTP vs marketing (priority field)  

## Architecture

```text
ProductService (demo)
  — same txn → DomainRepo + OutboxRepository.append
OutboxPublisher (VT loop)
  → claim batch FOR UPDATE SKIP LOCKED (simulated)
  → NotificationSender port (EmailSender, SmsSender)
  → mark SENT / retry / DLQ
```

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| Outbox table | Fixes dual-write | `kafka.send` after commit only |
| Claim/lease rows | Multi-worker safe | Single timer thread forever without lease |
| Idempotent sender key | Provider redelivery | Fire-and-forget |
| Priority field | OTP vs blast | One queue drowning OTP |

## Design decisions

- Outbox status: `NEW|IN_FLIGHT|SENT|DEAD`  
- **Lease timeout** recovers crashed workers  
- Marketing load cannot starve OTP (separate poll or priority sort)  
- In-memory “txn” = synchronized compound write for demo; document JDBC txn mapping  

## Implementation plan

1. OutboxRecord + repository  
2. Append API used by product demo  
3. Publisher loop with claim  
4. Senders with failure injection  
5. Retry/DLQ  
6. Metrics + demo main  

## Failure scenarios

| Failure | Handling |
|---------|----------|
| Crash after send before mark SENT | Redelivery; sender dedupe |
| Crash after append before publish | Publisher eventually sends |
| Provider 429 | Backoff; don’t DLQ immediately |
| Poison payload | DLQ after N; alert |
| Worker death mid-lease | Lease expiry reclaim |

## Testing strategy

- **Run:** `bash run-tests.sh` — happy path SENT; fail → retry → DEAD  
- Append + kill publisher before send → restart drains  
- ADR: [../../principal-engineer/portfolio/adr-003-outbox-not-dual-write.md](../../principal-engineer/portfolio/adr-003-outbox-not-dual-write.md)

## Performance considerations

- Batch claim size vs latency  
- VT workers capped by pool of DB connections  
- Avoid per-message full table scan — indexed status+priority+id  

## Scaling strategy

- Real Postgres `SKIP LOCKED`; partition outbox by time  
- Kafka after outbox for multi-consumer fan-out  
- Per-channel rate limits (distributed)  
- Horizontal publisher replicas  

## Interview discussion

“Outbox is how I get reliable events without 2PC. Workers are idempotent and leased; priority protects OTP. At scale this becomes CDC, but the invariant starts here.”

**Follow-ups:** Outbox vs listen-to-yourself CDC? How do you measure lag SLO?

## Skeleton

See `src/notify/` for outbox + publisher starter.

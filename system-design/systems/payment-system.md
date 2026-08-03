# Payment System

Orchestration + ledger around PSPs. Correctness over cleverness.

## Requirements

**Functional:** authorize/capture/charge, refund, webhook handling, status query, merchant config.  
**Non-functional:** no double-charge; auditability; p99 on intent create moderate; high durability; PCI scope minimized via tokenization.  
**Non-goals:** rebuilding card networks; full accounting ERP.

## Capacity estimation

```text
5M payments/day ≈ 58 QPS avg → peak ×15 ≈ 900 QPS
Each payment: several writes (intent, attempts, ledger lines) → ~5k DB writes/s peak
Payload small; PSP RTT dominates latency (100–300ms+)
Retention: 7+ years ledger → plan cold storage
Webhook spikes when PSP retries
```

## Architecture

```text
Merchant/Checkout → API Gateway → Payment API
                                  ├─ Idempotency store
                                  ├─ Payment DB (aggregates + ledger)
                                  ├─ Outbox → Kafka
                                  ├─ PSP adapters (Stripe/Adyen/…)
                                  └─ Webhook ingress (verify signatures)
Reconciliation workers ← Kafka / PSP reports
```

## Components

| Component | Role | Why |
|-----------|------|-----|
| Payment API | Intent/charge/refund | Use-case owner |
| Idempotency service | Key → payment | Dedupe clients |
| Provider router | Choose PSP | Failover, geo, cost |
| Ledger | Append-only entries | Audit/disputes |
| Webhook handler | External truth | Async outcomes |
| Reconciler | Match PSP vs ledger | Timeout uncertainty |

## Data flow

**Charge:** validate → reserve idempotency key → create payment PENDING → call PSP with key → on sync success CAPTURED + ledger; on timeout leave PENDING → reconcile.  
**Webhook:** verify → apply monotonic state transition → ledger if needed → notify merchant (outbox).  
**Refund:** check remaining capturable → provider refund → ledger.

## Data storage

| Data | Store | Notes |
|------|-------|-------|
| Payment + attempts | OLTP SQL | Strong txn per payment aggregate |
| Idempotency | SQL unique / Redis+DB | Durable truth in SQL |
| Ledger | Append-only tables | Never update amounts in place |
| Outbox | Same DB txn as write | CDC/publisher |
| Tokens | PSP / vault | Not raw PAN in your DB |

**Consistency:** money states monotonic; read-your-writes for merchant dashboards (primary).

## Scaling

- Partition payments by `merchantId` or `paymentId` hash  
- Separate webhook tier (noisy)  
- PSP CB + bulkheads per provider  
- Async merchant notifications  

## Failure modes

| Failure | Risk | Mitigation |
|---------|------|------------|
| Client retry | Double charge | Idempotency key |
| PSP timeout after charge | Unknown state | PENDING + reconcile; **no blind retry new charge** |
| Webhook before HTTP response | Race | Monotonic state machine |
| Duplicate webhooks | Double side-effect | Event id dedupe |
| PSP outage | Downtime | Router failover; fail closed on money |
| Replay attack webhooks | Fraud | Signature + timestamp skew window |

## Observability

- Payment success rate, PSP latency p99, pending age histogram  
- Idempotency hit ratio, reconcile backlog  
- Ledger imbalance alerts  
- Trace across API→PSP; alert on pending > N minutes  

## Security

- Least privilege; tokenize cards; encrypt PII  
- Webhook signature verification mandatory  
- Audit logs immutable; break-glass access  
- Rate limit intent creation per merchant  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| PENDING + reconcile | Safe under uncertainty | Auto-retry charge on timeout |
| Ledger + payment aggregate | Disputes & SOX-ish audit | Balance field only |
| Outbox | No lost events | Dual-write DB+Kafka |
| Fail closed | Money safety | Fail open to another PSP without rules |

## Evolution

1. Single PSP + SQL ledger + idempotency  
2. Router + webhooks + reconciler  
3. Multi-region: home-region writes; global read replicas  
4. Stored credentials / network tokens; marketplace split settlements  

Related: [idempotency](../distributed-systems/idempotency.md), [reliability](../fundamentals/reliability.md), [circuit-breakers](../distributed-systems/circuit-breakers.md).

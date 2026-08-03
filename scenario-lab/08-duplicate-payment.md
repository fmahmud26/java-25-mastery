# Duplicate Payment — at-least-once meets non-idempotent charge

## Incident

Finance flags ~0.3% duplicate card captures after a Kafka consumer rebalance storm. Customers see double charges for the same `orderId`. Payments team uses a Java 25 consumer with manual offset commit “after charge succeeds.” PSP (payment service provider) confirms two `charge` API calls with different idempotency keys for one order.

## Symptoms

- Duplicate captures correlated with consumer restarts / rebalances
- Logs show `charge` invoked twice for same `orderId`
- Offset commits sometimes absent between duplicates
- No obvious DB unique constraint failures (or constraint missing)
- Metrics: `payment.capture.success` > `order.paid` unique counts

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| Messaging | Kafka, `enable.auto.commit=false`, at-least-once processing |
| PSP | REST charge API supports idempotency keys — **caller must supply stably** |
| DB | Orders DB; payment ledger table without unique `(order_id)` |

## Metrics

```
kafka.consumer.rebalances     spike 14:02–14:20
payment.duplicate.suspect     +120 orders
psp.charges.per_order.p99     1 → 2
consumer.records.retried      up
```

Illustrative: rebalances + duplicates ⇒ replay of work without idempotent side effects.

## Logs

```
2026-08-03T14:05:11.002Z INFO  PaymentConsumer - received orderId=ORD-8891 attempt=1 key=pay-8891-a9f
2026-08-03T14:05:11.440Z INFO  PspClient - charge ok orderId=ORD-8891 chargeId=ch_1
2026-08-03T14:05:12.010Z WARN  PaymentConsumer - rebalance revoke; commit skipped orderId=ORD-8891
2026-08-03T14:05:40.220Z INFO  PaymentConsumer - received orderId=ORD-8891 attempt=1 key=pay-8891-b22
2026-08-03T14:05:40.880Z INFO  PspClient - charge ok orderId=ORD-8891 chargeId=ch_2
```

Note different idempotency keys `a9f` vs `b22` for the same order.

## Initial Hypotheses

1. At-least-once redelivery after rebalance without idempotent charge
2. Producer double-publish of payment events
3. Client double-submit (UI) — possible but check Kafka duplicates first
4. Retry wrapper generating new idempotency keys per attempt
5. Clock/ordering bug marking unpaid then charging twice

## Questions

- Where should idempotency live: DB, PSP key, or both?
- What assumption does “commit after success” make about crashes mid-flight?
- What would you check first: consumer logs, PSP keys, or order ledger uniqueness?
- What if traffic ×100 — more rebalances, more duplicates?
- How does this intersect with [system-design/](../system-design/) exactly-once illusions?

## Investigation

1. **Prove double side effect**  
   Join `orderId` to PSP charges; confirm two successes.

2. **Trace keys**  
   Code that builds idempotency key — `UUID.randomUUID()` per attempt? Must be **deterministic** from `orderId` (or payment intent id).

3. **Consumer semantics**  
   Rebalance listener: are offsets committed on revoke? Processing during revoke?

4. **DB constraints**  
   Is there a unique payment intent row? Insert-before-charge pattern?

5. **Producer audit**  
   Kafka offsets / producer metrics for double produce of same event.

6. **Timeline**  
   Align rebalance metrics with duplicate windows.

7. **Reproduce**  
   Kill consumer mid-charge in staging; ensure single PSP capture.

## Root Cause

Consumer uses at-least-once delivery. On rebalance revoke, the in-flight message was **not** committed after a successful PSP charge, so another consumer instance reprocessed the record. The client generated a **new random idempotency key** per processing attempt, so the PSP treated the retry as a new charge. Ledger lacked a uniqueness guard on `order_id`.

## Resolution

- **Immediate:** refund/reconcile duplicates; pause consumer if still storming.
- **Fix:** stable idempotency key = `orderId` (or `paymentIntentId`); DB unique constraint + “insert intent row first”; commit offsets only after durable local record of success; handle already-charged as success.
- Consider outbox + transactional patterns for state transitions.

## Prevention

- Idempotency review for every money path
- Chaos: kill -9 consumer during charge in staging
- Metric: `psp.charges / distinct(order_id)` alert
- Rebalance-safe processing guidelines in team runbooks

## Principal Engineer Discussion

- Exactly-once end-to-end is a marketing phrase — what’s the practical envelope (Kafka EOS + idempotent business ops)?
- Should payments be synchronous HTTP from checkout vs async consumer? Trade-offs for UX and duplication.
- Who owns reconciliation when PSP and ledger diverge?
- Design an immutable payment intent state machine suitable for Principal design interviews.

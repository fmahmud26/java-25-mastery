# Scenario: Payment Timeout — Charged or Not?

## Production story

Checkout calls PSP; client hits 30s timeout; PSP actually captured funds at t=28s. User retries. Without care: double charge or order marked failed while money taken.

## What’s failing

Uncertainty under at-least-once + slow dependency. Classic distributed **unknown outcome**.

## Bad responses

- Retry charge with new id  
- Mark order FAILED because HTTP timed out  
- Trust only sync response  

## Principal response

1. Idempotency key on first attempt; persist payment PENDING before PSP call.  
2. Retry **same** key → same payment object; PSP gets same key.  
3. On timeout leave PENDING; reconcile via PSP query/webhooks.  
4. Never show “paid” until provider confirmed; never “failed” until known not charged.  
5. Ledger append-only; alert on PENDING age.

## Trade-offs

Latency UX (“processing…”) vs wrong financial state. Correctness wins.

## Interview probes

- Where is the idempotency row committed relative to the PSP call?  
- What if webhook arrives before HTTP response?  

Related: [../idempotency.md](../idempotency.md), [../failure-handling.md](../failure-handling.md).

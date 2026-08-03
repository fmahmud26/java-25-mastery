# Idempotency

**Same logical request → same side effect**, even under duplicates (retries, redelivery, double-click).

## Why mandatory in distributed systems

Timeouts hide success. Queues redeliver. Clients retry. Without idempotency you get double charge, double email, double inventory decrement.

## Mechanisms

| Mechanism | Production use |
|-----------|----------------|
| `Idempotency-Key` + unique store | Payments, order create |
| Natural unique key | `INSERT` with PK; booking id |
| Dedupe table `event_id` | Consumers |
| Ledger append keyed by business id | Money |
| Provider-side keys | Stripe idempotency |

## Production scenario: timeout after PSP charge

Client retries → second charge without key.  
**Correct:** first request stored key→paymentId; retry returns same payment; PSP also gets key.  
**State PENDING** if unknown; reconcile — never blind new charge.

## Failure focus

- Keys too short TTL → replay charges after expiry  
- Deduping **after** side effect without txn → gap window  
- “Idempotent” GET mistaken for safe POST without keys  
- Non-idempotent compensate (refund twice)

## Trade-offs

| Buy | Sell |
|-----|------|
| Safe retries | Storage for keys; key design; API discipline |

## Principal interview angles

- “Walk a payment timeout with and without idempotency.”  
- “Where is the dedupe record committed relative to the side effect?”  

Related: [retry.md](./retry.md), [message-delivery.md](./message-delivery.md), [scenarios/payment-unknown-outcome.md](./scenarios/payment-unknown-outcome.md).

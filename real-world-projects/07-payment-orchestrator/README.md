# 07 — Payment Orchestrator

**Tier:** Resilience · **Demonstrates:** idempotency, resilience (timeouts/CB), persistence of payment state, observability, VT

## Problem

Orchestrate charges against a flaky PSP simulator: clients retry, PSP sometimes times out **after** capturing, webhooks arrive out of order. Your system must **never double-charge** and must leave payments in a reconcilable `PENDING` state under uncertainty.

## Requirements

**Functional**
- `charge(idempotencyKey, amount, merchantId)` → payment view  
- Simulate PSP: success / timeout-unknown / decline  
- Webhook `payment.captured|failed` applies monotonic transitions  
- Query by payment id / idempotency key  

**Non-functional**
- Idempotent create (unique key)  
- Timeout budget + circuit breaker around PSP  
- Append-only attempt log  
- Metrics: charges, duplicates, pending_age, cb_open  
- Clean architecture: domain payment aggregate + ports  

## Architecture

```text
HTTP/API adapter
  → PaymentService (app)
       → IdempotencyStore / PaymentRepository
       → PspClient (port) → FlakyPspSimulator
       → CircuitBreaker
       → Clock, Metrics
Webhook adapter → PaymentService.applyProviderEvent
Reconciler worker (VT) → PSP fetch status for PENDING
```

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| Explicit PENDING | Models unknown outcome | Binary success/fail from HTTP alone |
| Idempotency record first | Safe client retries | Charge then save |
| CB + timeout | Protect pools | Infinite retries |
| Attempt log | Audit | Mutable status only |
| In-memory + interface | Local demo | Skipping persistence port |

## Design decisions

- Persist `INIT|PENDING|CAPTURED|FAILED` with monotonic rules (never CAPTURED→PENDING).  
- PSP call **after** idempotency insert; on timeout stay PENDING.  
- Webhook and reconciler share same transition function.  
- Retry to PSP uses **same** provider idempotency key.

## Implementation plan

1. Domain: Payment, PaymentAttempt, states  
2. Ports: PaymentRepository, PspClient  
3. PaymentService.charge / applyEvent / reconcile  
4. Flaky simulator + CB wrapper  
5. Demo main: double submit, timeout path, webhook  
6. Metrics printer  

## Failure scenarios

| Failure | Expected |
|---------|----------|
| Double client submit | One PSP charge; same payment returned |
| Timeout after capture | PENDING → webhook/reconcile → CAPTURED once |
| CB open | Fast fail new charges; no thread stampede |
| Duplicate webhook | No-op |
| Decline | FAILED; new key required for retry |

## Testing strategy

- **Run:** `bash run-tests.sh` (JDK 25) — idempotency, UNKNOWN→PENDING, webhook capture, decline  
- Concurrent identical keys → one capture  
- Simulator timeout + late webhook  
- ADR: [../../principal-engineer/portfolio/adr-002-payment-unknown-outcome.md](../../principal-engineer/portfolio/adr-002-payment-unknown-outcome.md)

## Performance considerations

- VT for webhook + reconcile workers  
- Bound reconciler poll rate  
- Don’t hold locks across PSP I/O  

## Scaling strategy

- Shard payments by merchantId  
- Outbox for merchant notifications (project 08)  
- Multi-PSP router with bulkheads  

## Interview discussion

“This is the distributed unknown-outcome problem in code: idempotency, PENDING, reconcile, and monotonic webhooks. Microservices don’t matter until this is right.”

**Follow-ups:** Exactly-once to the ledger? How long do you retain idempotency keys?

## Skeleton

See `src/payment/` — extend `PaymentService` and simulator per plan.

# Partial Failure

## Mental Model

In distributed calls, **some** dependencies succeed and others fail — the system must define consistency.

```text
Inventory reserved OK
Payment timed out (unknown)
Notify not sent
→ What is order state?
```

## Networking Angle

Timeouts create **uncertainty**: request may have succeeded on the server after the client gave up. TCP reset vs HTTP 500 vs client timeout are different evidence — none alone proves “no side effect” for POST.

## Patterns

| Pattern | Use |
|---------|-----|
| Idempotency keys | Safe retry of POST charge |
| Saga / compensating tx | Undo inventory reserve |
| Outbox | Reliable follow-up events |
| Explicit state machine | PENDING_PAYMENT → PAID / FAILED |
| Read-your-status | Query PSP/ledger after unknown |

## Java Backend

Don’t pretend a timed-out `HttpClient.send` means “payment did not charge.” Query status or rely on idempotent replay. Portfolio drill: [../real-world-projects/07-payment-orchestrator/](../real-world-projects/07-payment-orchestrator/).

## Production Scenario — unknown payment

Client times out at 3s; PSP charged at 3.1s. Naive “mark failed + retry without key” double-charges. Correct: persist `PENDING_UNKNOWN`, reconcile, idempotent capture.

## When Not to Over-Engineer

Read-only fan-out where any success path can degrade gracefully (show partial UI) — still document what the user sees.

### Related

[timeout-failures.md](./timeout-failures.md) · [retries.md](./retries.md) · [scenarios.md](./scenarios.md) · [../system-design/distributed-systems/idempotency.md](../system-design/distributed-systems/idempotency.md)

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

Timeouts create **uncertainty**: request may have succeeded on the server after the client gave up.

## Patterns

| Pattern | Use |
|---------|-----|
| Idempotency keys | Safe retry of POST charge |
| Saga / compensating tx | Undo inventory reserve |
| Outbox | Reliable follow-up events |
| Explicit state machine | PENDING_PAYMENT → PAID / FAILED |

## Java Backend

Don’t pretend a timed-out `HttpClient.send` means “payment did not charge.” Query status or rely on idempotent replay.

### Related

[timeout-failures.md](./timeout-failures.md) · [retries.md](./retries.md) · [scenarios.md](./scenarios.md)

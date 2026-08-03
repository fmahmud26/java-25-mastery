# Error-Handling Strategy

Playbook beyond syntax — **decide** per failure mode.

## Decision Framework

```text
Is this expected business outcome?  → Result / status code (often no throw)
Is it transient?                    → retry / backoff / 503
Is it permanent / poison?           → DLQ / 4xx / don’t retry
Is it programming bug?              → 500 + alert + fix
Is the process dying (Error)?       → fail fast / isolate
```

## Bad Patterns (ban in review)

| Pattern | Why it hurts |
|---------|----------------|
| Empty catch | Silent corruption |
| Catch log continue | Hidden outage |
| `catch (Exception)` everywhere | No classification |
| Throw away cause | Un-debuggable |
| Retry on all exceptions | Duplicate payments |
| Return null on failure | NPE elsewhere |
| Catch Error and continue | Unstable JVM |

## Improved Shape (payment capture)

```text
validate → 400 validation errors (Result/exception per style)
idempotency lookup → return prior result if seen
psp call:
  declined → CaptureResult.declined (no retry)
  timeout/5xx → PaymentTransientException → retry if idempotent
  unknown → alert + safe fail
persist outcome → translate SQL failures
emit metrics: outcome=success|declined|transient|error
```

## Scenario Matrix

| Scenario | Don’t | Do |
|----------|-------|-----|
| Payment failure | Retry declines | Idempotent retry timeouts only |
| DB failure | Show SQL to client | Translate; retry transient; circuit breaker |
| Network timeout | Infinite retry | Bounded backoff + jitter |
| File failure | Pretend OK | Quarantine + alert |
| Third-party API | Map all to 500 | Map 4xx vs 5xx; budget retries |

## Principal — Resilience

Strategy pairs with **bulkheads** (isolate PSP pool), **circuit breakers**, **timeouts**, **idempotency keys**, **outbox**. Exceptions are signals those mechanisms consume — not a substitute for them.

### Related

[retry-decisions.md](./retry-decisions.md) · [logging-and-observability.md](./logging-and-observability.md) · [exception-propagation.md](./exception-propagation.md)

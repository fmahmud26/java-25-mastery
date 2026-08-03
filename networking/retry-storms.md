# Retry Storms

## Problem

Many clients retry together when a dependency is slow/down → traffic spike → dependency cannot recover.

```text
100 pods × 10 in-flight × 3 retries = 30× load
```

## Causes

- No backoff/jitter  
- Retry on non-idempotent calls  
- Timeout too aggressive + high retry count  
- No circuit breaker / bulkhead  
- Shared retry amp across fan-out  

## Mitigations

| Control | Effect |
|---------|--------|
| Exponential backoff + jitter | Spread load |
| Max attempts (small) | Bound amplification |
| Circuit breaker | Stop calling while sick |
| Bulkhead / concurrency cap | Limit in-flight per dep |
| Idempotency keys | Safe payment retries |
| Retry-After / 429 honor | Cooperate with peer |

## PE

Default retry policy is a **product decision** reviewed like an API change.

### Related

[retries.md](./retries.md) · [scenarios.md](./scenarios.md) · [principal-decisions.md](./principal-decisions.md)

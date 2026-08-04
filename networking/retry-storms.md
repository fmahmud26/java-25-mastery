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
- Synchronized retry waves after deploy  

## Mitigations

| Control | Effect |
|---------|--------|
| Exponential backoff + jitter | Spread load |
| Max attempts (small) | Bound amplification |
| Circuit breaker | Stop calling while sick |
| Bulkhead / concurrency cap | Limit in-flight per dep |
| Idempotency keys | Safe payment retries |
| Retry-After / 429 honor | Cooperate with peer |
| Shed at ingress | Protect core paths |

## Production Scenario — black Friday cascade

PSP slow; every checkout retries ×3; PSP RPS ×3; timeouts worsen; CB never installed. Fix: CB + bulkhead + idempotent retries only; game-day the policy.

## Detection

Dependency RPS ≫ successful business ops; retry metric ratio high; peer 429/503 climb together with your timeouts.

## PE

Default retry policy is a **product decision** reviewed like an API change. See also [../reactive-programming/production-pitfalls.md](../reactive-programming/production-pitfalls.md) for operator-level retry amp.

### Related

[retries.md](./retries.md) · [scenarios.md](./scenarios.md) · [principal-decisions.md](./principal-decisions.md) · [../scenario-lab/13-service-timeout.md](../scenario-lab/13-service-timeout.md)

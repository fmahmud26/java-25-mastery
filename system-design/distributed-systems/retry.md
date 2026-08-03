# Retries

Retry **transient** failures to improve reliability — or **amplify outages** if unbounded.

## Retry vs not

| Retry | Do not retry |
|-------|--------------|
| Timeouts, 503, connection reset | 400 validation, 401/403 |
| Explicitly transient | Business decline (NSF) |
| Idempotent operations | Non-idempotent without key |

## Production scenario: dependency outage

Payment PSP down → 5k app nodes × 3 retries × no jitter = 15k QPS hammer → prolongs outage; thread pools exhaust → checkout fails everywhere (cascade).

**Defense:** retry budget (e.g. max +10% traffic), full jitter, circuit breaker, bulkheads, deadlines.

## Deadline budgets

Total 200ms → retries must fit. Prefer fail within budget over 3 slow retries that blow p99.

## Trade-offs

| Buy | Sell |
|-----|------|
| Survive blips | Load amplification; latency; complexity with idempotency |

## Principal interview angles

- “What’s your retry budget during a 503 storm?”  
- “Which exceptions are retryable on this client?”  

Related: [backoff.md](./backoff.md), [idempotency.md](./idempotency.md), [scenarios/retry-storm.md](./scenarios/retry-storm.md), [failure-handling.md](./failure-handling.md).

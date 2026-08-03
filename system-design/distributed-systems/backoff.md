# Backoff

Space retries so recovery isn’t crushed by synchronized clients.

## Strategies

| Strategy | Behavior | Failure mode |
|----------|----------|--------------|
| Constant | Fixed delay | Thundering herd |
| Exponential | 1, 2, 4… capped | Synchronized spikes without jitter |
| Exp + **full jitter** | Random in `[0, exp]` | Preferred default |
| Decorrelated jitter | AWS-style | Good in practice |

## Production scenario: synchronized cron + retry

Cert expiry blip → all workers fail → wait exactly 30s → all retry together → second outage wave.

**Fix:** jitter; spread schedules; CB so most callers fail fast.

## Caps

Max delay, max attempts, overall deadline, then DLQ / user error / Retry-After.

## Trade-offs

| Longer backoff | Slower recovery UX |
| Heavy jitter | Harder to reason about p99 |
| No backoff | Outage amplifier |

Related: [retry.md](./retry.md), [scenarios/retry-storm.md](./scenarios/retry-storm.md), [backpressure.md](./backpressure.md).

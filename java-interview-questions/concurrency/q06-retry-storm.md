# Retry storm during partial outage

## Question

2000 nodes × 3 retries × no jitter against a 503 dependency. Outage lengthens after dependency recovers. Staff asks you to propose standards.

## Difficulty

Staff

## Expected answer

Retry budgets, full jitter backoff, circuit breakers, idempotency, don’t retry non-idempotent blindly. Cap amplification; prefer fail-fast + degrade.

## Reasoning

Synchronized retries create thundering herds that prevent recovery.

## Follow-up

How do you express a retry budget as % of traffic?

## Common mistake

“More retries = more resilience.”

## Principal-level discussion

Encode defaults in platform HTTP clients; mandate idempotency keys for money; page on CB open duration; teach amplification math in onboarding.

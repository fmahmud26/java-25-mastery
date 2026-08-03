# CompletableFuture without timeouts

## Question

A CF pipeline calls three services with `.get()` and no timeout. One dependency hangs. Impact?

## Difficulty

Senior

## Expected answer

Caller threads block indefinitely → pool exhaustion cascade. Always `orTimeout`/`completeOnTimeout`/`get(timeout)`, propagate deadlines, cancel where possible.

## Reasoning

Unbounded waits convert dependency failure into systemic latency.

## Follow-up

Difference between `orTimeout` and `completeOnTimeout`?

## Common mistake

Only setting HTTP client connect timeout but blocking forever on CF get.

## Principal-level discussion

Deadline propagation as org standard (headers + CF timeouts). Chaos tests for hung deps. Review scorecards for `.get()` without timeout.

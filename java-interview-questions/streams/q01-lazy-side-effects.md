# Side effects inside `map`

## Question

A developer puts HTTP calls inside `stream.map` to “enrich” DTOs. Latency and duplicates spike. What’s wrong?

## Difficulty

Mid

## Expected answer

Streams encourage pure transforms; side effects in `map` are hard to reason about (laziness, short-circuit, parallel). Prefer explicit loops/services for I/O, or staged pipelines with clear concurrency (CF/VT) and idempotency.

## Common mistake

Assuming each element is mapped exactly once always (not true with some short-circuit/parallel misuse).

## Follow-up

Where do side effects belong if you still use streams?

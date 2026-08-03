# Retrying POST /payments

## Question

Client times out on `POST /payments` and retries automatically. Risk and correct design?

## Difficulty

Senior

## Expected answer

Duplicate charges unless idempotency keys. Retries require idempotent semantics server-side; clients must replay same key.

## Reasoning

Timeout ≠ failure; at-least-once delivery.

## Follow-up

Where is the idempotency record stored?

## Common mistake

Retrying all methods equally.

## Principal-level discussion

Platform HTTP client retries only safe methods by default; money APIs mandate Idempotency-Key; contract tests.

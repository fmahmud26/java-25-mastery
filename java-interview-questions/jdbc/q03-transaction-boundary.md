# Transaction spanning HTTP

## Question

`@Transactional` method calls an external PSP and holds the DB transaction open during HTTP. Failure modes?

## Difficulty

Senior

## Expected answer

Long-held connections, pool exhaustion, lock contention, unclear rollback semantics on partial external success. Keep transactions short; don’t remote call inside DB txn; use idempotency + outbox.

## Reasoning

DB txn duration should match local ACID work, not network RTT.

## Follow-up

How do you still get atomic “order + outbox”?

## Common mistake

“Bigger transaction = safer.”

## Principal-level discussion

Ban remote calls in transactional boundaries via review; paved saga/outbox examples; pool SLOs.

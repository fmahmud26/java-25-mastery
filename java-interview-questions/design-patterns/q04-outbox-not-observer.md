# Observer isn’t an outbox

## Question

Team uses in-process Observer to “emit OrderCreated” to email/inventory listeners. Under crash, listeners miss events. Better pattern?

## Difficulty

Senior

## Expected answer

Transactional outbox / reliable messaging. Observer is process-local and not durable. Persist intent + publish reliably.

## Reasoning

Reliability requires storage + retry, not just callbacks.

## Follow-up

Choreography saga vs orchestrator?

## Common mistake

Equating design-pattern Observer with Kafka consumer groups.

## Principal-level discussion

Standardize outbox/CDC; teach when in-process events are OK (UX only); review dual-write risks in ADRs.

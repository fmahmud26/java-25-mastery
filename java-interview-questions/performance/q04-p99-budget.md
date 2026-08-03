# Building a p99 latency budget

## Question

p99 is 250ms against 100ms SLO. Staff asks you to lead the breakdown. How?

## Difficulty

Staff

## Expected answer

Trace spans: app, DB, deps, queueing, GC. Attribute % of budget; fix top contributor; set sub-budgets and alerts. Don’t tune GC if DB wait is 180ms.

## Reasoning

Latency is a sum of parts; optimization without attribution wastes time.

## Follow-up

How do queueing delays appear in metrics?

## Common mistake

Averaging (p50) to declare victory.

## Principal-level discussion

Org-wide latency budgets per hop; error budgets; refuse new sync deps without budget headroom.

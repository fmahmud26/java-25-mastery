# Phantom reads under concurrency

## Question

Report totals change within one transaction reading the same query twice under default isolation. Which phenomenon and mitigation?

## Difficulty

Senior

## Expected answer

Possible phantoms/non-repeatable depending on isolation. Raise isolation (RR/Serializable) with cost, or use snapshots (MVCC), or accept and design differently. Know DB defaults (e.g., Postgres RC).

## Reasoning

Isolation trades concurrency for anomalies prevented.

## Follow-up

Dirty read vs non-repeatable vs phantom?

## Common mistake

Assuming all DBs default to Serializable.

## Principal-level discussion

Choose isolation per use case; document; load-test contention; prefer idempotent writes over max isolation everywhere.

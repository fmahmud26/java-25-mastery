# ADR-003 — Outbox Instead of Dual-Write

- **Status:** Accepted  
- **Date:** 2026-08-03  
- **Implements:** [../../real-world-projects/08-notification-outbox/](../../real-world-projects/08-notification-outbox/)

## Context

Product write + “publish notification” invited classic dual-write: DB commit succeeds, publish fails → silent gap.

## Decision

Append **outbox record in the same logical transaction** as the domain write. Separate publisher claims with lease, retries with backoff, marks DEAD after max attempts. No fire-and-forget Observer as durability.

## Alternatives rejected

| Option | Why rejected |
|--------|----------------|
| Dual write DB + Kafka | Partial failure gap |
| In-process Observer only | Process crash loses event |
| “Retry in API thread” forever | Couples latency to provider |

## Consequences

- Publisher lag becomes an SLO (`outbox_oldest_age`).  
- Need claim semantics to avoid double-send (at-least-once → consumer idempotent).  
- Demo uses in-memory txn lock; production maps to DB transaction.

## Success metrics

- After inject-publish-failure: domain row exists **and** outbox eventually SENT or DEAD — never “lost.”  
- Tests cover claim/retry/dead ([run-tests.sh](../../real-world-projects/08-notification-outbox/run-tests.sh)).

Related: [../refusals.md](../refusals.md) §3 · [../../system-design/distributed-systems/scenarios/dual-write-gap.md](../../system-design/distributed-systems/scenarios/dual-write-gap.md)

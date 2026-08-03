# ADR-002 — Payment Unknown Outcome → PENDING + Reconcile

- **Status:** Accepted  
- **Date:** 2026-08-03  
- **Implements:** [../../real-world-projects/07-payment-orchestrator/](../../real-world-projects/07-payment-orchestrator/)

## Context

PSP may capture after the client sees a timeout. Retries without discipline double-charge. Product wants a sync “success” response.

## Decision

On `UNKNOWN_TIMEOUT`, persist payment as **PENDING** (never CAPTURED). Require **idempotency key** uniqueness. Apply webhooks/reconcile with monotonic transitions only. Duplicate `charge(same key)` returns existing payment (no second PSP attempt from our orchestrator’s create path).

## Alternatives rejected

| Option | Why rejected |
|--------|----------------|
| Timeout → CAPTURED | Lies; breaks ledger |
| Blind retry charge | Double-capture risk |
| Only webhook, no PENDING | Operator cannot see in-flight money |

## Consequences

- UX must show pending; support tooling for PENDING age.  
- Reconciler mandatory; metric `pending_age`.  
- Tests must lock idempotency + timeout path ([run-tests.sh](../../real-world-projects/07-payment-orchestrator/run-tests.sh)).

## Success metrics

- Zero double-capture for one idempotency key in chaos tests.  
- PENDING always reconcilable to CAPTURED/FAILED.  
- Duplicate hits increment without new PSP create.

Related: [../refusals.md](../refusals.md) §4 · [../scenarios/connection-pool-exhaustion.md](../scenarios/connection-pool-exhaustion.md)

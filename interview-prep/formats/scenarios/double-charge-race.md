# Scenario: Double charge under retries

## Prompt

Mobile app retries `POST /charge` on timeout. Some customers charged twice. Design the service behavior.

## Spine hints (don’t read until you’ve answered)

**Problem:** duplicate money side effect under at-least-once.  
**Context:** client timeouts; PSP may have succeeded.  
**Options:** idempotency key; GET-only status; disable retries (bad).  
**Decision:** require Idempotency-Key; persist before PSP; PENDING on unknown.  
**Result:** duplicateHits metric; reconcile job; zero double captures in soak.

Deep: [../../../system-design/distributed-systems/scenarios/payment-unknown-outcome.md](../../../system-design/distributed-systems/scenarios/payment-unknown-outcome.md)

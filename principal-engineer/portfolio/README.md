# Portfolio Evidence Index

Artifacts that turn this repo from a **curriculum** into **Principal-prep evidence**: measured labs, ADRs, automated checks, hard refusals.

| Artifact | Proves |
|----------|--------|
| [../experiments/EVIDENCE.md](../experiments/EVIDENCE.md) | Owned numbers on JDK 25 (not empty Observed stubs) |
| [adr-001-virtual-threads-adoption.md](./adr-001-virtual-threads-adoption.md) | VT decision with measurements + abort criteria |
| [adr-002-payment-unknown-outcome.md](./adr-002-payment-unknown-outcome.md) | Payment correctness under uncertainty |
| [adr-003-outbox-not-dual-write.md](./adr-003-outbox-not-dual-write.md) | Dual-write refused; outbox chosen |
| [adr-004-no-microservice-for-god-table.md](./adr-004-no-microservice-for-god-table.md) | Scale path without premature split |
| [closed-loop-allocation-gc.md](./closed-loop-allocation-gc.md) | Perf: measure → change → GC evidence |
| [../refusals.md](../refusals.md) | Explicit “what we will not ship” |
| [../../real-world-projects/07-payment-orchestrator/run-tests.sh](../../real-world-projects/07-payment-orchestrator/run-tests.sh) | Idempotency / unknown-outcome tests |
| [../../real-world-projects/08-notification-outbox/run-tests.sh](../../real-world-projects/08-notification-outbox/run-tests.sh) | Outbox claim/retry/dead tests |
| [../../real-world-projects/06-url-shortener-service/run-tests.sh](../../real-world-projects/06-url-shortener-service/run-tests.sh) | Rate limit + shorten invariants |

These are **portfolio decisions for this mastery repo**, not fictional company postmortems. In interviews, say: “Here’s what I measured and what I refuse.”

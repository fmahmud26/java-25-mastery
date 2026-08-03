# Track: Distributed Systems Interviews

## What they test

Partial failure, consistency per API, delivery semantics, idempotency, retries/backpressure — **failure-first**.

## PCR-OTDR emphasis

- Options are protocols (outbox vs dual-write, CP vs AP for *this* op)  
- Result = lag, PENDING age, duplicate rate  

## Practice sources

- [../../system-design/distributed-systems](../../system-design/distributed-systems/)  
- Bank scenarios via concurrency/networking/jdbc Senior+  
- Formats: [../formats/scenarios/payment-unknown.md](../formats/scenarios/payment-unknown.md) · [../formats/debugging/dual-write-gap.md](../formats/debugging/dual-write-gap.md) · [../formats/deep-dive/exactly-once.md](../formats/deep-dive/exactly-once.md)

## Loop

Take a scenario → draw crash points → pick mechanism → name metric.

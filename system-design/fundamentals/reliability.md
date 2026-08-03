# Reliability

**Definition:** correctness under stress — the system does what it promises **even when parts fail**, including “don’t double-charge.”

Availability ≠ reliability: a flapping API that returns 200 with wrong balances is available and unreliable.

## Reliability building blocks

| Concern | Mechanism |
|---------|-----------|
| Durability | Replication, fsync policy, backups |
| Exactly-once **effect** | Idempotency keys + dedupe store |
| Partial failure | Timeouts, retries with jitter, CB, saga compensation |
| Data integrity | Checksums, constraints, ledger immutability |
| Change safety | Canaries, progressive delivery, feature flags |

## Error budgets

If SLO is 99.9%, budget is 0.1% failure. Spend budget on velocity; freeze risky changes when burned. This is how Principal teams prioritize.

## Dependency reliability

Treat every network call as: may timeout, may duplicate, may reorder. Design client with **deadline budgets** (e.g., 200ms total → 80ms to pay, 50ms to inventory).

## Testing reliability

Chaos (kill AZ, inject latency), replay production traffic to staging, idempotency contract tests, backup restore drills (see DR).

Related: [idempotency](../distributed-systems/idempotency.md), [retry](../distributed-systems/retry.md), [consistency.md](./consistency.md).

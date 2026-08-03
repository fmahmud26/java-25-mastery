# Fault Tolerance

Ability to **continue correct service** (possibly degraded) despite faults — not merely “retry until it works.”

## Building blocks

| Block | Role |
|-------|------|
| Redundancy | Multi-AZ, replicas, N+1 |
| Isolation / bulkhead | Limit blast radius |
| Timeout + CB | Fail fast |
| Idempotency | Safe redo |
| Backpressure | Survive load |
| Graceful degradation | Protect core journey |
| Reconciliation | Fix uncertainty (money) |
| Chaos / game days | Prove assumptions |

## Tolerance vs correctness

A system that stays “up” while double-charging is **fault intolerant to truth**. Availability without invariants is not PE success.

## Production scenario: multi-AZ “tolerant” payments

App multi-AZ; DB single-AZ primary. AZ loss → total write outage.  
**Lesson:** redundancy must cover the **state** tier, not only stateless pods. State RPO/RTO dominate.

## Trade-offs

| More redundancy | Cost, complexity, consistency surface |
| Faster failovers | Flapping risk |
| Stronger durability | Latency |

## Principal interview angles

- “Which single AZ failure takes you down?”  
- “What invariant do you refuse to degrade?”  

Related: [failure-handling.md](./failure-handling.md), [replication.md](./replication.md), [cap.md](./cap.md).

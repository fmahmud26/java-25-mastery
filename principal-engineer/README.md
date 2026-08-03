# Principal Engineer — Technical Leadership Through Depth

This folder teaches **how Principals decide**, not generic “be a leader” advice.

You influence by: naming bottlenecks, quantifying risk, choosing boundaries, and sequencing migrations that survive contact with production.

Complementary tracks: [system-design](../system-design/) (design a system) · [low-level-design](../low-level-design/) (design objects) · [performance-engineering](../performance-engineering/) (measure/fix).

## How to use

1. Read [topics/](./topics/) until you can argue each lever with numbers and failure modes.  
2. Work every [scenarios/](./scenarios/) file aloud using the template.  
3. Practice [influence.md](./influence.md) — how technical decisions land across teams.  
4. Own the evidence: [refusals.md](./refusals.md) · [portfolio/](./portfolio/) · [../experiments/EVIDENCE.md](../experiments/EVIDENCE.md).

## Scenario template (required)

```text
Context → Constraints → Options → Trade-offs → Decision
→ Reasoning → Risks → Migration → Success metrics
```

Every option must be technically real (data model, consistency, deploy topology, cost drivers) — not “align stakeholders.”

## Topics

| Topic | Path |
|-------|------|
| Architectural decision making | [topics/architectural-decision-making.md](./topics/architectural-decision-making.md) |
| Technical strategy | [topics/technical-strategy.md](./topics/technical-strategy.md) |
| Trade-offs | [topics/trade-offs.md](./topics/trade-offs.md) |
| Scalability | [topics/scalability.md](./topics/scalability.md) |
| Reliability | [topics/reliability.md](./topics/reliability.md) |
| Operational excellence | [topics/operational-excellence.md](./topics/operational-excellence.md) |
| Technical debt | [topics/technical-debt.md](./topics/technical-debt.md) |
| Architecture evolution | [topics/architecture-evolution.md](./topics/architecture-evolution.md) |
| Migration strategy | [topics/migration-strategy.md](./topics/migration-strategy.md) |
| Platform engineering | [topics/platform-engineering.md](./topics/platform-engineering.md) |
| Engineering standards | [topics/engineering-standards.md](./topics/engineering-standards.md) |
| Observability | [topics/observability.md](./topics/observability.md) |
| Incident management | [topics/incident-management.md](./topics/incident-management.md) |
| Cross-team architecture | [topics/cross-team-architecture.md](./topics/cross-team-architecture.md) |
| Technical influence | [topics/technical-influence.md](./topics/technical-influence.md) |
| System boundaries | [topics/system-boundaries.md](./topics/system-boundaries.md) |
| Long-term maintainability | [topics/long-term-maintainability.md](./topics/long-term-maintainability.md) |

## Scenarios

Org / architecture cards and **technical incident cards** (VT, pool, CPU, memory, GC, threads, latency):

See full index: [scenarios/README.md](./scenarios/README.md)

| Scenario | File |
|----------|------|
| Scale 100× | [scenarios/scale-100x.md](./scenarios/scale-100x.md) |
| Overlapping service ownership | [scenarios/overlapping-services.md](./scenarios/overlapping-services.md) |
| Legacy blocks growth | [scenarios/legacy-blocks-growth.md](./scenarios/legacy-blocks-growth.md) |
| Zero-downtime migration | [scenarios/zero-downtime-migration.md](./scenarios/zero-downtime-migration.md) |
| Microservices may be wrong | [scenarios/microservices-not-required.md](./scenarios/microservices-not-required.md) |
| Cost up 5× | [scenarios/cost-up-5x.md](./scenarios/cost-up-5x.md) |
| Reliability declining | [scenarios/reliability-declining.md](./scenarios/reliability-declining.md) |
| Shared database, two teams | [scenarios/shared-database.md](./scenarios/shared-database.md) |
| Platform adoption stall | [scenarios/platform-adoption-stall.md](./scenarios/platform-adoption-stall.md) |
| VT no gain | [scenarios/virtual-threads-no-gain.md](./scenarios/virtual-threads-no-gain.md) |
| Pool exhaustion | [scenarios/connection-pool-exhaustion.md](./scenarios/connection-pool-exhaustion.md) |
| CPU saturation | [scenarios/cpu-saturation.md](./scenarios/cpu-saturation.md) |
| Memory growth | [scenarios/memory-growth.md](./scenarios/memory-growth.md) |
| GC pause growth | [scenarios/gc-pause-growth.md](./scenarios/gc-pause-growth.md) |
| Thread explosion | [scenarios/thread-explosion.md](./scenarios/thread-explosion.md) |
| Latency cliff | [scenarios/latency-cliff.md](./scenarios/latency-cliff.md) |

Practice with [../interview-prep/tracks/principal-engineer.md](../interview-prep/tracks/principal-engineer.md) · [../scenario-lab/](../scenario-lab/) · [../cheat-sheets/](../cheat-sheets/)

## Portfolio evidence (not just notes)

| Artifact | Path |
|----------|------|
| Hard refusals | [refusals.md](./refusals.md) |
| ADRs + closed loops | [portfolio/](./portfolio/) |
| Measured labs index | [../experiments/EVIDENCE.md](../experiments/EVIDENCE.md) |
| Project tests | `real-world-projects/0{6,7,8}-*/run-tests.sh` |


## What “Principal” sounds like

- “The bottleneck is the `orders` primary at 40k QPS writes; sharding by `merchant_id` costs cross-merchant reporting — here’s the read path.”  
- “We’ll strangler the billing module behind an interface; dual-run 30 days; cutover on idempotent replay.”  
- “Microservices don’t fix the God table; they distribute the join pain.”  

Not: “We should align and empower squads to iterate.”

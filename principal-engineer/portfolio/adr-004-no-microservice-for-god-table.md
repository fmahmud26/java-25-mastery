# ADR-004 — Do Not Microservice a Write Hotspot First

- **Status:** Accepted (strategy)  
- **Date:** 2026-08-03  

## Context

Leadership asks for microservices + K8s when traffic may grow ~100×. Single primary DB and sync dependencies likely dominate.

## Decision

**Refuse premature service split.** Sequence: telemetry → cache/async offload → pool/index discipline → read replicas → shard/cell **only** when write forecast proves need. Extract services after module seams and metrics exist.

## Alternatives rejected

| Option | Why rejected now |
|--------|------------------|
| 15 microservices in 9 months | Doesn’t remove God table; adds dual-write risk |
| Vertical only forever | Eventually hits ceiling — but buy time with measurement |
| Shard in week 1 | Migration risk without capacity proof |

## Consequences

- Org narrative must tolerate “modular monolith first.”  
- Invest in idempotency/outbox before distributed sagas.  
- Cost: fewer teams deploying independently short-term.

## Success metrics

- Checkout p99 holds at 10× load test before any split.  
- Primary CPU model <70% at 30× or shard spike approved.  
- Sync deps on checkout path reduced.

Related: [../scenarios/scale-100x.md](../scenarios/scale-100x.md) · [../refusals.md](../refusals.md) §1

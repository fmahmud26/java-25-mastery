# Reliability (PE lens)

Reliability is an **error budget and dependency graph** problem, not a vibe.

## PE responsibilities

- Define SLIs that match user journeys (checkout success ≠ CPU green)  
- Budget dependencies on the critical path  
- Mandate idempotency, timeouts, CB on money/inventory paths  
- Kill retry storms via standards  

## Declining reliability — typical technical causes

- Rising sync fan-out on request path  
- Growing p99 from shared DB  
- On-call fatigue → slower remediation  
- Unowned services (see cross-team)  
- Schema/API changes without compatibility  

## Decision style

Prefer **degrade** over binary hard-down when safe (read-only mode, disable recommendations). Never degrade payment truth.

Related: [operational-excellence.md](./operational-excellence.md), [scenarios/reliability-declining.md](../scenarios/reliability-declining.md).

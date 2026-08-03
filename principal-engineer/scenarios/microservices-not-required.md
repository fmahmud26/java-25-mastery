# Scenario: Engineering Wants Microservices — Problem May Not Require Them

## Context

200-person eng org, 8 product teams sharing a modular monolith (reasonable package structure, one DB). Lead time ~1 week. Pain: deploy queue (one broken test blocks all), and occasional noisy-neighbor CPU from report jobs. A vocal staff group proposes “60 microservices in 2 quarters” mirroring a FAANG talk. Kubernetes already exists for the monolith.

## Constraints

- One DB primary still at 35% peak CPU — not the ceiling  
- No team has owned a production distributed saga yet  
- Checkout SLO is currently green  
- Hiring plan doesn’t add 8 platform SREs  
- Compliance audit mid-year wants clearer data ownership (real need)  

## Options

| Option | Approach |
|--------|----------|
| **A. 60-service split** | Domain teams each get services |
| **B. Modular monolith + deploy isolation** | Separate deployables only where needed (workers) |
| **C. Extract 2–3 seams** | Reporting worker, identity, notifications |
| **D. Soft modules + platform golden path** | Keep one binary; enforce boundaries in CI |
| **E. Rewrite in different language/stack** | |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Team autonomy narrative | Ops load, latency, distributed debugging; premature |
| B | Unblock deploys for batch | Partial complexity |
| C | Targets real pains | Requires discipline at APIs |
| D | Cheap boundary enforcement | Still shared release train |
| E | Resume-driven | Massive risk |

## Decision

**D + B + selective C:** Keep a modular monolith as default. Enforce module boundaries (ArchUnit/dependency rules). Extract **async workers** (reports) and **notification** first. Revisit service split when a module has independent scale, independent failure domain, or compliance boundary that CI modules cannot satisfy.

## Reasoning

Microservices solve **independent deploy/scale/failure/ownership with network contracts**. Current evidence shows **release contention and batch CPU**, not domain transaction distribution needs. Splitting without sagas/idempotency skills converts a deploy problem into a reliability problem. Compliance can start with schema ownership + module APIs, not 60 repos.

## Risks

- Boundary rules rot without CI fail-the-build  
- “Temporary” shared DB access from extracted services  
- Political perception PE is “anti-modern”  
- Real growth later still needs cells/services — delay too long  

## Migration

1. Publish module map + forbidden dependencies; CI gate.  
2. Move report jobs to separate deployable sharing DB read replica.  
3. Notification outbox → worker service (first networked boundary with contract tests).  
4. Scorecard quarterly: deploy queue time, SEV from coupling, DB CPU.  
5. Only if scorecard fails: design service extract with single-writer tables.  

## Success metrics

- Median time-to-prod for a module change ↓ ≥40% without 60 services  
- Report jobs no longer starve checkout CPU (pool isolation proven)  
- Checkout SLO unchanged or better  
- Number of sync service hops on checkout remains 0 until justified  
- Compliance: documented data owners per schema area  

Related: [../topics/system-boundaries.md](../topics/system-boundaries.md), [../topics/technical-strategy.md](../topics/technical-strategy.md).

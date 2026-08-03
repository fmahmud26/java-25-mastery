# System Boundaries

A boundary is where **consistency, ownership, and deploy cadence** can differ.

## Draw boundaries around

| Glue | Boundary candidate |
|------|--------------------|
| Same transactional invariants | One service/module (orders+lines) |
| Different scale profile | Separate (search vs checkout) |
| Different regulatory domain | Separate (PII vault vs marketing) |
| Different on-call skill | Careful split (payments) |

## Smell: wrong boundary

- Distributed transaction across three services for one user click  
- “Microservice” that cannot deploy without three others  
- Two writers to one table  

## Seam first

Before split: extract interface/module + metrics. If you cannot measure the seam, you cannot split safely.

Related: [cross-team-architecture.md](./cross-team-architecture.md), [scenarios/microservices-not-required.md](../scenarios/microservices-not-required.md).

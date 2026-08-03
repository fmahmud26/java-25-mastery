# Technical Debt

Debt is a **mortgage on future throughput** with interest paid in incidents and slow change.

## Classify (technical, not moral)

| Type | Example | Interest |
|------|---------|----------|
| Deliberate short-term | Skip index for launch | Query latency until added |
| Accidental | God table `entities` | Every feature touches it |
| Entropic | No owner, copy-paste clients | Divergent behavior |
| Compatibility | Dual writes forever | Double failure modes |

## PE management

1. Quantify: eng-days lost / incident hours / error budget burn attributable.  
2. Budget capacity (e.g. 20% time) against highest interest.  
3. Prefer **strangler interfaces** over big-bang rewrites.  
4. Give debt a ticket with **revisit metric** (“p99 > 200ms or kill dual-write”).  

## Not debt

- Choosing Postgres over niche DB for a CRUD app  
- A monolith with clear modules  

Related: [architecture-evolution.md](./architecture-evolution.md), [long-term-maintainability.md](./long-term-maintainability.md).

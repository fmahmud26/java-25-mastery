# Trade-offs

Every design buys something by selling something else. Principals make the **currency** explicit.

## Common currencies

| Buy | Often sell |
|-----|------------|
| Latency | Cost, freshness, complexity |
| Consistency | Availability, multi-region write UX |
| Isolation (services) | Ops load, distributed failure modes |
| Delivery speed now | Maintainability, incident rate later |
| Generic platform | Fit for one team’s path |

## How to argue a trade-off

1. Name the SLO or invariant at risk.  
2. Quantify both sides (p99, $, eng-weeks, error budget).  
3. State the **reversibility**.  
4. Pick and document the loser you’re accepting.

## Example (technical)

“Cache product prices (TTL 60s): buy p99 −40ms and −70% DB CPU; sell ≤60s staleness and invalidation bugs. Acceptable because checkout re-prices from inventory service at pay time.”

Related: [architectural-decision-making.md](./architectural-decision-making.md), [scalability.md](./scalability.md).

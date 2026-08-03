# Incident Management (Technical)

Incidents are production’s design review — Principals improve **system and process** from them.

## During

| Role focus | Technical action |
|------------|------------------|
| Incident lead | Protect error budget; coordinate |
| Technical lead | Hypotheses from SLIs/traces; mitigate first |
| Comms | Customer impact truth |

Mitigate before root-cause theater: shed load, disable feature flag, failover, rollback.

## After (technical blameless)

- Timeline with metrics, not feelings  
- Root cause chain: trigger → amplifier → missing defense  
- Actions: code, capacity, standard, ownership — with owners/dates  
- Delete actions that are “be more careful”  

## PE systemic fixes

Retry budgets, bulkheads, queue bounds, canary — not “train the on-call better” as the only action.

Related: [reliability.md](./reliability.md), [scenarios/reliability-declining.md](../scenarios/reliability-declining.md).

# Operational Excellence

Make production change **boring**: deploy, observe, roll back, capacity, toil reduction.

## Technical pillars

| Pillar | PE lever |
|--------|----------|
| Deploy | Progressive delivery, automatic rollback on SLO burn |
| Capacity | Load test against peak model; autoscaling with limits |
| Toil | Automate certificate, reindex, shard add runbooks |
| On-call | Actionable alerts only; runbooks linked to dashboards |
| Change failure | Canaries, feature flags, schema expand/contract |

## Metrics that matter

- Change failure rate, MTTR, deploy frequency **with** SLO burn correlation  
- Percent of pages with runbook + owning team  
- Toil hours/week on platform vs product  

## Anti-patterns

- “Hero” fixes without systemic controls  
- Dashboards nobody watches  
- Prod-only config known by one engineer  

Related: [observability.md](./observability.md), [incident-management.md](./incident-management.md), [engineering-standards.md](./engineering-standards.md).

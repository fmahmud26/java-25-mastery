# Migration Strategy

Migrations fail from **data planes and traffic planes**, not from bad PowerPoints.

## Patterns

| Pattern | Use when | Risk |
|---------|----------|------|
| Big bang | Tiny data, maintenance window OK | High blast radius |
| Dual write | New store must see live writes | Dual failure; needs delete date |
| Dual read / shadow | Validate new path | Stale compare bugs |
| Expand/contract | Schema & API | Longer calendar time |
| Blue/green | Stateless compute | Sticky state issues |
| Canary % | Progressive confidence | Needs good SLIs |
| Replay from log | Event-sourced / CDC | Ordering, idempotency |
| Strangler | Legacy replacement | Facade becomes permanent |

## Zero-downtime checklist

1. Backward/forward compatible artifacts  
2. Idempotent replay  
3. Lag monitors on dual-run  
4. Abort criteria (error budget, diff rate)  
5. Rollback that doesn’t corrupt (especially money)  

Related: [scenarios/zero-downtime-migration.md](../scenarios/zero-downtime-migration.md), [architecture-evolution.md](./architecture-evolution.md).

# Long-Term Maintainability

Maintainability is **cost of correct change over years** — coupling, clarity, and delete-ability.

## Technical predictors

| Healthy | Unhealthy |
|---------|-----------|
| Modules with one reason to change | Everything imports `core.util` |
| Schemas versioned | Silent field reuse |
| Dead code deleted | Feature flags forever |
| Tests at boundaries | 90% brittle UI tests |
| Onboarding < 1 day to run service | Tribal setup |

## PE investments that compound

- Delete unused paths (reduce surface)  
- Contract tests at team boundaries  
- Idempotent, observable defaults in templates  
- Document irreversible choices in ADRs  

**Filled ADRs in this repo:** [../portfolio/](../portfolio/)

## Rewrite temptation

Rewrites pay off only with a strangler and parity metrics. Greenfield without traffic shift plan is a second system, not a replacement.

Related: [technical-debt.md](./technical-debt.md), [architecture-evolution.md](./architecture-evolution.md).

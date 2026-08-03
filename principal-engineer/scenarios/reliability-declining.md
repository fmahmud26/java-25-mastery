# Scenario: Reliability Is Declining

## Context

Over 6 months: error budget burn for checkout went from 20% → 110% of monthly budget. MTTR up. Pages 3×. No single SEV-1 root cause — death by a thousand deps: new recommendation sync call, flaky search, payment retries without budget, shared DB CPU, and alert noise so on-call ignores early signals. Headcount shipped many features.

## Constraints

- Feature freeze politically hard beyond 2 weeks  
- Cannot remove payments  
- Must show improvement in one quarter for board  
- Three teams touch checkout path  

## Options

| Option | Approach |
|--------|----------|
| **A. Feature freeze + war room forever** | |
| **B. Error budget policy** | Burn → only reliability work |
| **C. Cut critical path** | Remove/async nonessential deps |
| **D. More retries/timeouts everywhere** | “Make resilient” |
| **E. Rewrite checkout** | |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Focus | Political capital; temporary |
| B | Systemic prioritization | Feature delay when burning |
| C | Latency & failure reduction | Product resists losing sync personalization |
| D | Feels proactive | **Amplifies outages** |
| E | Fantasy clean slate | 2-quarter risk |

## Decision

**B + C immediately; standards for retries/CB; no D.** Declare checkout an error-budget-governed surface. Remove sync recommendation/search from checkout critical path (feature-flag async or cached). Enforce retry budgets + CB via platform library. Fix DB top queries. Two-week triage freeze on non-budget work for checkout owners when burn > 100%.

## Reasoning

Reliability declined from **rising fan-out and retry amplification**, not lack of microservices. More retries without budgets worsen correlated failure. Error budgets convert reliability into an engineering priority signal. Critical path length is the architecture.

## Risks

- Product demands sync recommendations back without SLO proof  
- Alert rewrites miss real pages  
- Teams game SLIs  
- DB fix deferred while flags only mask  

## Migration

1. Publish checkout dependency graph from traces (honest).  
2. Feature-flag off nonessential sync calls behind budget.  
3. Roll platform HTTP client defaults: timeout, jittered retry cap, CB.  
4. Page only on SLO burn / customer journey fail — kill CPU noise alerts.  
5. Weekly burn review with EMs; budget exceeded → reliability backlog only.  
6. Load test checkout monthly against peak.  

## Success metrics

- Monthly error budget burn ≤ 80% for 3 consecutive months  
- Checkout sync deps ≤ 3 (pay, inventory, fraud)  
- p99 checkout ↓ to SLO; retry storm metric near 0  
- Pages/week ↓ ≥50%; MTTR ↓ ≥30%  
- Zero SEVs from unbounded retries  

Related: [../topics/reliability.md](../topics/reliability.md), [../topics/incident-management.md](../topics/incident-management.md), [../topics/engineering-standards.md](../topics/engineering-standards.md).

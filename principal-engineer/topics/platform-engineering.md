# Platform Engineering

Internal products that reduce **marginal cost of correct production systems**.

## What platforms own (technical)

| Surface | Examples |
|---------|----------|
| Golden paths | Service template: metrics, tracing, CI, deploy, CB defaults |
| Shared data planes | Kafka, identity, secrets, feature flags |
| Multi-tenant infra | Ingress, mesh optional, certs |
| Guardrails | Policy as code, baseline dashboards |

## What platforms must not own

Product business rules, one team’s unique schema, “centralize all SQL.”

## Adoption is the product metric

If teams bypass the platform, either the path is wrong or incentives are wrong. Measure: % new services on golden path; time-to-first-prod; toil tickets.

## Thin vs thick platform

Start with **paved road + defaults**; avoid mandatory mega-framework that freezes language/runtime choices without SLO proof.

Related: [scenarios/platform-adoption-stall.md](../scenarios/platform-adoption-stall.md), [engineering-standards.md](./engineering-standards.md).

# Scenario: Cost Increased 5×

## Context

Cloud bill $180k → $900k/month over 2 quarters. Revenue +40%. Product launched video features + multi-region “active-active” prematurely. Observability vendor ingest + Kafka retention + over-provisioned Kubernetes requests dominate. Finance wants cut in 60 days without a major outage.

## Constraints

- Cannot abandon EU residency region  
- p99 API SLO must hold  
- Video delivery quality is customer-visible  
- No “delete prod backups” heroics  
- Engineers 2 dedicated for FinOps + PE sponsorship  

## Options

| Option | Approach |
|--------|----------|
| **A. Across-the-board 50% downsize** | Shrink all clusters |
| **B. Attribution then target** | Unit cost per journey; kill top drivers |
| **C. Reserved/savings plans only** | Buying commitments |
| **D. Pull back multi-region writes** | Regional primary + DR  
| **E. Drop observability** | Sample 1% everything |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Fast $ | Random outages |
| B | Sustainable cuts | Needs 1–2 weeks measurement |
| C | Easy discount | Locks wasteful shape |
| D | Large $ + complexity↓ | Weaker regional UX for writes |
| E | Vendor $ | Blind incidents |

## Decision

**B then D then right-size:** Build cost per request / per GB-video / per region. Act on top three drivers:
1. Object storage + egress for video → CDN tiering, lifecycle to cold, compress.  
2. Dual active-active DB → **single-writer region + async DR** unless product proves write latency need.  
3. K8s: requests/limits from real usage; cluster autoscaling; separate spot for batch.  
4. Observability: keep full RED for critical paths; sample debug traces; hot log retention 7→3 days with lake.

Commitments (RIs) **after** shape correction.

## Reasoning

5× cost with 1.4× revenue is almost always **architecture and retention**, not “need more discounts.” Active-active is a consistency/product tax. Blind downsizing hits reliability. Observability cuts without journey tiers recreate the reliability-decline scenario.

## Risks

- Mis-attributed costs → cutting the wrong layer  
- DR-only region fails RTO in drill  
- CDN cache misconfig → origin egress spike  
- Engineers optimize micro without killing idle environments  

## Migration

| Week | Action |
|------|--------|
| 1 | Tagging + cost allocation by service/team; top 10 line items |
| 2 | Idle non-prod teardown; lower log retention; fix K8s requests |
| 3–4 | Video lifecycle + CDN; measure egress |
| 4–6 | Demote active-active to primary+DR with drill |
| 6–8 | Savings plans on stable baseline |

## Success metrics

- Bill ≤ $450k/month within 60 days (≤2.5× original, aligned to growth) without SLO breach  
- Cost / 1k API calls and cost / GB delivered trending down  
- Idle resource % < 15%  
- RPO/RTO still meet documented DR targets (drill evidence)  
- Trace/log coverage retained on checkout/pay journeys  

Related: [../topics/trade-offs.md](../topics/trade-offs.md), [../topics/operational-excellence.md](../topics/operational-excellence.md).

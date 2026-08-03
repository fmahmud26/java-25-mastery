# Scenario: Legacy Architecture Blocks Growth

## Context

10-year monolith: `core` module is a 2M-LOC ball of mud. New “Subscriptions” product needs recurring billing, proration, tax — but every attempt edits `OrderService` (8k-line class) and the `documents` god table (orders, invoices, credit notes as `type` discriminators). Deployments require 15-team regression; mean time to add a price point is 6 weeks. Competitors ship in days. Exec asks for “platform rewrite in 12 months.”

## Constraints

- Revenue on monolith; downtime window ≤ minutes rare  
- Billing correctness audited annually  
- Team can staff 2 squads for modernization, not 10  
- Must ship MVP subscriptions in 4 months  

## Options

| Option | Approach |
|--------|----------|
| **A. Big-bang rewrite** | New stack parallel system |
| **B. Modularize in place** | Package seams, no new deployables |
| **C. Strangler for billing** | New Billing service; façade from monolith |
| **D. Only extract read models** | CQRS reporting; writes stay |
| **E. Freeze monolith; new products outside** | Dual UX/systems |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Clean story | 12–24 mo parity hell; dual spend |
| B | Lower risk | May not free deploy cadence enough |
| C | Ship subscriptions against clean model | Integration + migration of historical invoices |
| D | Faster reports | Doesn’t unblock write complexity |
| E | Speed for new | Split brain customer/billing truth |

## Decision

**C with B-lite:** Introduce a **Billing** bounded context (new service + ledger schema). Monolith becomes a client via anti-corruption adapter. Do **not** rewrite checkout/catalog yet. Inside monolith, extract `OrderService` interfaces only where Billing touches.

## Reasoning

Growth is blocked by **coupled billing invariants in a god table**, not by “monolith” per se. Rewrite of everything optimizes for engineering preference. Strangler optimizes for audited money paths and time-to-MVP. Historical documents migrate behind the façade asynchronously.

## Risks

- Dual writes of invoice state during transition  
- Tax/proration bugs → revenue leakage  
- Façade becomes permanent dumping ground  
- Team continues adding features to `documents`  

## Migration

1. Define Billing API: `CreateSubscription`, `Invoice`, `RecordPayment` with idempotency.  
2. New ledger DB (append-only); no use of `documents` types for new subs.  
3. Monolith adapter: subscription purchase calls Billing; old one-off orders unchanged.  
4. Shadow: for pilot accounts, generate invoices both ways; diff amounts.  
5. Cutover pilots → expand cohorts.  
6. Backfill: export historical subs-related rows via batch; freeze those types.  
7. Hard gate in code review: new billing types forbidden in `documents`.  

## Success metrics

- Subscription MVP live in ≤4 months with Billing as SoT for those invoices  
- Change lead time for pricing rules ≤ 1 week  
- Billing-related SEV rate ≤ baseline of monolith order path  
- % new billing code in `documents` = 0 after gate  
- Rewrite proposal deferred until strangler covers ≥70% billing GMV  

Related: [../topics/architecture-evolution.md](../topics/architecture-evolution.md), [../topics/technical-debt.md](../topics/technical-debt.md).

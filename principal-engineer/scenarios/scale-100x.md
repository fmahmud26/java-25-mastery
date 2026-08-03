# Scenario: The System Needs to Scale 100×

## Context

B2B SaaS checkout + order API. Today ~500 QPS peak, single-region. Sales commits to a marketplace deal implying ~50k QPS peak in 9 months (~100×). Monolith on 12 app nodes; one Postgres primary (64 cores) at ~55% CPU peak; Redis cache ~80% hit on catalog reads; payments via external PSP. Leadership asks: “Do we need microservices and Kubernetes everywhere?”

## Constraints

- Checkout p99 ≤ 300ms in-region; error budget 99.9%  
- Strong correctness on inventory reservation and payment idempotency  
- 9-month runway; 3 senior engineers can be dedicated  
- Cannot freeze product for a rewrite  
- Reporting (finance) needs merchant-level aggregates daily  

## Options

| Option | Technical approach |
|--------|-------------------|
| **A. Vertical + tune** | Bigger DB, more app nodes, fix N+1, raise cache hit |
| **B. Scale-out monolith** | Stateless pods × N; read replicas; CQRS for reports; queue non-critical work |
| **C. Shard early** | Shard Postgres by `merchant_id`; app becomes shard-aware |
| **D. Microservices split** | Extract inventory, catalog, checkout into services now |
| **E. Cell architecture** | Partition tenants into cells (app+DB) by merchant cohort |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Time | Hits hardware ceiling; single blast radius |
| B | ~10–30× with cache/async | Won’t reach 100× if writes bound primary |
| C | Write scale | Cross-merchant joins/reporting pain; migration hard |
| D | Team autonomy narrative | Distributed latency, dual-write risk; **doesn’t remove God table** |
| E | Blast radius + scale | Ops complexity; cross-cell features hard |

## Decision

**Sequence B → measure → C or E only on the proven bottleneck.** Near term: treat as write/read path engineering on the monolith, not a service decomposition program.

Concrete plan:
1. Break peak model: read vs write QPS, inventory row contention, PSP RTT.  
2. Push catalog/session to cache; move notifications/analytics off request path.  
3. Postgres: pooling, covering indexes, read replicas for GET order status.  
4. Inventory hot SKUs: row-level CAS + optional per-SKU queue if flash-sale shaped.  
5. If primary write CPU still projects >70% at 30×, design **merchant_id sharding** or **cells** with a spike — not 15 microservices.

## Reasoning

100× is a **capacity curve**, not an org-chart problem. Microservices add network hops and failure modes; they help when **independent scale/ownership** is proven. Here the ceiling is likely DB writes + sync dependencies. Extract services only after module seams and metrics exist.

## Risks

- Marketplace traffic is spikier than 100× average → need load-shed and rate limits  
- Sharding too late under panic → rushed dual-write bugs  
- Cache stale prices → must re-validate at pay  
- PSP becomes bottleneck → bulkheads + async capture where product allows  

## Migration

| Wave | Work | Abort signal |
|------|------|--------------|
| 0 | Telemetry: DB CPU, lock waits, cache hit, checkout span breakdown | — |
| 1 | Async offload + cache + pool budgets | p99 worsens |
| 2 | Read replicas + report pipeline via CDC | Replica lag > SLO |
| 3 | Spike shard/cell on write forecast | Shard spike fails idempotency tests |
| 4 | Dual-run shard router for pilot merchants | Diff/error budget burn |

## Success metrics

- Checkout p99 ≤ 300ms at projected 10× in load test (gate for sales launch)  
- Primary CPU < 50% at 10×; modeled < 70% at 30× or shard plan approved  
- Sync dependencies on checkout path ≤ 3  
- No increase in double-charge/over-sell incidents  
- Cost per 1k checkouts trend flat-to-down after wave 1  

Related: [../topics/scalability.md](../topics/scalability.md), [../topics/system-boundaries.md](../topics/system-boundaries.md).

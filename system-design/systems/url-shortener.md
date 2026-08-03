# URL Shortener

Principal-level design for high-QPS redirects with durable mappings and async analytics.

## Requirements

**Functional**
- Create short link → returns code (optional custom alias, expiry, creator)
- Redirect `GET /{code}` → 302/301 to long URL
- Optional: click analytics, enable/disable, bulk create

**Non-functional**
- Redirect p99 low (e.g. ≤20–50ms in-region cache hit)
- Extreme read:write (often 100:1+)
- Codes globally unique; predictable codes discouraged for private links
- Multi-AZ; DR for mapping store

**Non-goals (MVP):** full CRM, A/B landing pages, global anycast day one.

## Capacity estimation

Example prompt numbers (adjust aloud):

```text
100M redirects/day ≈ 1.2k QPS avg → peak ×10 ≈ 12k QPS
1M creates/day ≈ 12 QPS avg (writes small)
Code: 7 chars base62 ≈ 62^7 ≈ 3.5e12 space (enough)
Record ≈ 500B (url, meta) × 10B links ≈ 5TB raw → ~15TB with indexes/replicas
Cache: top 20% keys may serve 80% traffic → size hot set explicitly
Bandwidth: 12k QPS × 1KB ≈ 12MB/s trivial vs DB IOPS
```

## Architecture

```text
Client → DNS/CDN → L7 LB → API (stateless)
                         ├─ Redis (code → url, TTL)
                         ├─ Primary DB (mappings) + read replicas
                         ├─ ID service (Snowflake / range allocator)
                         └─ Kafka: click events → analytics pipeline
Admin/UI → API (authz) → DB
```

CDN optional for branded domains; many designs terminate TLS at LB and cache in Redis.

## Components

| Component | Role | Why |
|-----------|------|-----|
| API | shorten + redirect | Stateless scale-out |
| ID allocator | Unique numeric ids → base62 | Avoids hash collision loops at scale |
| Redis | Hot redirect path | Latency + DB protection |
| DB | Source of truth | Durability, expiry, aliases |
| Kafka + workers | Clicks, aggregates | Keep redirect path thin |
| Rate limiter | Create abuse | Protect write path |

## Data flow

**Create:** authz → validate URL → allocate id / check alias → persist → optionally warm cache → return short URL.  
**Redirect:** cache get → on miss DB → fill cache → **async** enqueue click → 302.  
**Why async clicks:** must not add Kafka RTT to p99 redirect.

## Data storage

| Data | Store | Key | Notes |
|------|-------|-----|-------|
| Mapping | SQL/NoSQL | `code` PK / partition | `long_url`, `expires_at`, `owner`, `status` |
| Alias uniqueness | Unique constraint | alias | Separate from random codes |
| Cache | Redis | `c:{code}` | TTL min(policy, expiry) |
| Analytics | OLAP / TS | code, time | From Kafka; eventual |

**Consistency:** create must be strongly unique; redirect may tolerate brief cache staleness on disable (TTL or explicit invalidate).

## Scaling

- API horizontal behind LB  
- Shard DB by `hash(code)` when single primary saturates  
- Redis cluster; L1 Caffeine on API for hottest codes  
- Kafka partitions by `code` for analytics  
- Separate write API pool from redirect pool (noisy neighbor)

## Failure modes

| Failure | Effect | Mitigation |
|---------|--------|------------|
| Redis down | Latency↑ | Fall through to DB; CB; scale replicas |
| DB primary down | Creates fail | Multi-AZ failover; redirects from cache/replica if policy allows |
| Cache stampede | DB melt | Singleflight per code |
| ID service down | Creates fail | Prefetch id ranges on API nodes |
| Kafka down | Lost/lagged analytics | Buffer locally carefully or accept lag; don’t block redirect |
| Hot celebrity code | Hot shard | L1 cache + replicate key |

## Observability

- Redirect QPS, p50/p99, cache hit ratio, DB miss QPS  
- Create success/fail, alias conflict rate  
- Kafka click lag  
- Trace: redirect path spans  
- Alert: hit ratio drop, DB CPU, error budget on redirect

## Security

- Authn for create; abuse rate limits; malware URL scanning queue  
- Don’t allow open redirect to `javascript:` / dangerous schemes  
- Predictability: enough entropy; private links require unguessable codes  
- Admin actions audited  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| ID → base62 | Unique without collision retry storms | Hash(url) only — collisions + non-idempotent length |
| Cache-aside Redis | Fit read skew | DB-only — won’t meet p99 at peak |
| Async analytics | Protect redirect SLO | Sync write click to DB |
| 302 vs 301 | 302 easier updates; 301 CDN-cache sticky | Blind 301 for all |

## Evolution

1. MVP: single region, SQL + Redis, random ids  
2. Prefetch id ranges; read replicas  
3. Shard + Redis cluster; Kafka analytics  
4. Multi-region: regional caches; write region affinity for creates; global code namespace via coordinated allocator or region-prefixed codes  

Related: [caching](../fundamentals/caching.md), [partitioning](../fundamentals/partitioning.md), [rate-limiting](../distributed-systems/rate-limiting.md).

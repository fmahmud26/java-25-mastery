# Rate Limiter (Service)

Distributed rate limiting as a platform capability for API gateways and services.

## Requirements

**Functional:** `Allow(key, cost) → {allow, remaining, reset}`; policies per route/tenant; bulkheads.  
**Non-functional:** very low added latency (p99 ≤1–2ms local, ≤5ms with Redis); accurate enough globally; high QPS.  
**Non-goals:** bot ML scoring.

## Capacity estimation

```text
Every API call checks limiter → equal to edge QPS
500k QPS peak checks → Redis must shard; or local approximation
State per key: ~100B × 50M active keys ≈ 5GB (+ replicas)
```

## Architecture

```text
Client → API Gateway / Envoy filter → Rate Limit Service (optional)
                                   ↘ or sidecar/local + Redis
Rate Limit Service → Redis Cluster (counters / token buckets)
Config control plane → policies (push to NLB nodes)
```

Two common PE designs:
1. **Central RLS** (Lyft pattern) — Envoy → gRPC ratelimit service → Redis  
2. **Library + Redis** — app embeds algorithm  

## Components

| Component | Role | Why |
|-----------|------|-----|
| Policy config | Limits per key template | Dynamic |
| Decision engine | Token bucket / sliding window | Algorithm swap |
| Redis store | Shared counters | Cross-node accuracy |
| Local cache of policies | Avoid config chat | |
| Optional local token cache | Cut Redis QPS | Approximate |

## Data flow

Extract key(s) → fetch policy → atomic consume tokens in Redis (Lua) → return allow/deny → metrics.  
Deny → 429 + Retry-After.

## Data storage

| Data | Store |
|------|-------|
| Counters / buckets | Redis hashed by key |
| Policies | etcd/SQL + push | 
| Audit (optional) | Kafka sampled |

## Scaling

- Redis Cluster hash slots; avoid single global key  
- Shard big tenants; separate Redis for txn vs marketing if needed  
- Gateway-level first pass + service second pass  
- Local stale-allow with sync correction under extreme QPS (document error)

## Failure modes

| Failure | Choice | Why |
|---------|--------|-----|
| Redis down | Fail-closed (auth) / fail-open (reads) | State explicitly |
| Hot key `global` | Split / approximate / dedicated | |
| Clock skew | Redis server time in Lua | |
| Config push lag | Version policies; default secure | |

## Observability

- Allow/deny rates by policy, Redis p99, fallback mode active  
- Alert: deny spike, Redis saturation, accidental fail-open  

## Security

- Limits on login/password reset (credential stuffing)  
- Separate anonymous IP limits vs authed user  
- Protect RLS itself from amplification  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Redis Lua token bucket | Atomic + fast | Get/set race |
| Fixed window at edge | Cheap | Boundary burst — call it out |
| Hybrid local+global | Latency | Global only at 1M QPS — cost |

## Evolution

1. Gateway fixed window in-memory (single node)  
2. Redis global  
3. Hierarchical limits + cost weights  
4. Multi-region: regional limits + global quota async  

Related: [rate-limiting](../distributed-systems/rate-limiting.md), [caching](../fundamentals/caching.md), [latency](../fundamentals/latency.md).

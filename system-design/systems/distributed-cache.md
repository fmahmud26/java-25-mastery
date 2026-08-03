# Distributed Cache

Shared caching layer (Redis-like cluster) used by many services — design as a product, not a Map.

## Requirements

**Functional:** get/set/del, TTL, optional pub/sub invalidation, scan-avoidance APIs.  
**Non-functional:** low latency, high QPS, horizontal scale, replication for HA, predictable eviction.  
**Non-goals:** replacing primary DB durability for money.

## Capacity estimation

```text
1M QPS gets, 10% sets; 200B avg value; working set 2TB
Memory ≈ working set × replication factor (e.g. 2–3) + overhead (~1.5×)
Network: 1M × 200B ≈ 200MB/s payload + protocol overhead
Hot key: 50k QPS on one key must not melt one shard
```

## Architecture

```text
Apps → client library (pool, timeout, CB) → Redis Cluster proxies/nodes
                                         ↘ L1 in-process cache (optional)
Control: monitoring, hot-key detection, failover
Invalidation: app writes DB → event → delete cache key
```

## Components

| Component | Role | Why |
|-----------|------|-----|
| Shard map / slots | Partition keys | Scale |
| Primary + replicas | HA reads optional | Failover |
| Client smart routing | Lower hop latency | |
| L1 Caffeine | Hot keys / stampede | |
| Invalidation bus | Freshness | |

## Data flow

**Cache-aside:** read L1→Redis→DB; fill back. Write DB then invalidate (or versioned key).  
**Stampede:** singleflight in app or lock in Redis for loader.

## Data storage

| Concern | Approach |
|---------|----------|
| Values | Memory; optional Redis on Flash |
| Durability | AOF/RDB optional — usually ephemeral OK |
| TTL | Per key; idle eviction LRU/LFU |

**Consistency:** eventual with TTL; never treat cache as SoT for payments.

## Scaling

- Hash slots; reshard with care  
- Read replicas for read-heavy (stale OK)  
- Split clusters by domain (session vs catalog) to isolate failure  
- Compress large values; store blob pointer in S3 if huge  

## Failure modes

| Failure | Mitigation |
|---------|------------|
| Node down | Replica promote; client retry other replica |
| Network split | Continuity depends on cluster bus; apps must TTL+DB fallback |
| Hot key | L1, replicate key, split, or dedicated shard |
| Thundering herd | Singleflight + probabilistic early refresh |
| Cache stampede on deploy | Warm critical keys; jitter TTL |
| Big key | Reject/chunk; monitor |

## Observability

- Hit ratio, eviction rate, memory, CPU, blocked clients, hot keys  
- App: miss latency, CB opens to cache  
- Alert: hit ratio cliff (often means outage or bad deploy)  

## Security

- AUTH/TLS; network isolation  
- No PII in keys if logs print keys  
- Dangerous commands disabled (`FLUSHALL`, `KEYS`)  
- Per-tenant key prefix quotas  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Ephemeral cache | Simplicity | Redis as DB — false durability |
| Invalidate on write event | Fresher | TTL-only for highly mutable profiles — long stale |
| Domain clusters | Blast radius | One giant cluster for all |
| L1 + L2 | Hot key + latency | Redis alone for celebrity keys |

## Evolution

1. Single Redis + cache-aside  
2. Cluster + hit/miss metrics  
3. L1 + invalidation events + hot-key tooling  
4. Multi-region cache (regional) with global SoT DB  

Related: [caching](../fundamentals/caching.md), [partitioning](../fundamentals/partitioning.md), [circuit-breakers](../distributed-systems/circuit-breakers.md).

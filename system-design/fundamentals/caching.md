# Caching

Store expensive results closer/faster. Always state: **what is cached, TTL, invalidation, consistency, stampede**.

## Patterns

| Pattern | Flow | Why | Risk |
|---------|------|-----|------|
| Cache-aside | App reads cache; miss → DB → fill | Simple, common | Stampede on miss |
| Read-through | Cache loads on miss | Encapsulated | Cache becomes logic hub |
| Write-through | Write cache+DB sync | Fresher reads | Write latency |
| Write-behind | Write cache; flush async | Fast writes | Durability risk |
| Refresh-ahead | Proactive reload | Smooth latency | Complexity |

## Where to cache

| Layer | Example | Good for |
|-------|---------|----------|
| CDN / edge | Static, signed URLs | Global read |
| App local (Caffeine) | Hot keys | Ultra-low latency |
| Redis cluster | Shared session, short URL | Cross-instance |
| Materialized view | Search index | Query reshape |

## Invalidation (the hard part)

- TTL-only: simple; stale until expiry  
- Event-driven delete/update: fresher; needs reliable events  
- Versioned keys: `user:42:v7` — avoid purge storms  

**Principal phrasing:** “Accept ≤T staleness; on write publish invalidation; TTL as safety net.”

## Stampede / thundering herd

Many misses on one key → singleflight/request coalescing, probabilistic early expire, or lock per key.

## Hot keys

Local L1 cache + Redis; replicate hot key to many cache nodes; split key; physically isolate celebrity entity.

## Negative caching

Cache “not found” briefly to protect DB from missing-key attacks — with short TTL.

Related: [latency.md](./latency.md), [consistency.md](./consistency.md), [systems/distributed-cache.md](../systems/distributed-cache.md).

# 06 — URL Shortener Service (Clean Architecture + Persistence)

**Tier:** Persistence / scalability · **Demonstrates:** clean architecture, persistence, caching, rate limiting, VT HTTP, observability

## Problem

Build a production-shaped **URL shortener**: create short codes, redirect with low latency, rate-limit creates, persist mappings, cache hot redirects — without turning into an anemic CRUD tutorial. The hard parts are **uniqueness, cache correctness, and abuse**.

## Requirements

**Functional**
- `POST /api/v1/urls` `{ "url": "https://..." }` → `{ "code", "shortUrl" }`  
- `GET /{code}` → `302` to long URL  
- Optional TTL/expiry  

**Non-functional**
- Clean architecture packages (domain / app / adapter)  
- JDBC persistence with unique `code`  
- Cache-aside Redis **or** in-process Caffeine-style map port for local demo  
- Rate limit creates per API key/IP  
- VT-friendly server; metrics: creates, redirects, cache hit, rate-limit rejects  
- p99 redirect path optimized (cache first)  

## Architecture

```text
adapters.http (handlers)
adapters.persistence (JdbcUrlRepository)
adapters.cache (InMemoryRedirectCache)
adapters.ratelimit (TokenBucketLimiter)
        ↓
application (ShortenUrlService, RedirectService)
        ↓
domain (ShortUrl, UrlCode, UrlRepository port, Clock)
```

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| Hexagonal ports | Swap DB/cache in tests | Handlers calling JDBC |
| Base62 from snowflake/sequence | Uniqueness without hash collision loops | Pure hash(url) only |
| Cache-aside on redirect | Read skew | DB every redirect |
| Token bucket port | Teach limiting | “Sleep and hope” |
| H2/SQLite file for local | Zero ops | Require managed Postgres day one (optional profile later) |

## Design decisions

- **Code allocation** in domain service; DB unique constraint is safety net.  
- **Redirect** never blocks on rate limit (only creates).  
- **Cache invalidation** on delete/disable; TTL ≤ expiry.  
- **API key** header for create quota — demonstrates tenancy hook.

## Implementation plan

1. Domain model + `UrlRepository` port  
2. In-memory + JDBC adapters (H2)  
3. `ShortenUrlService` / `RedirectService`  
4. Cache port + decorator around repository reads  
5. Rate limiter around shorten  
6. JDK HttpServer or minimal router wiring on VT  
7. Schema `short_urls(code PK, url, created_at, expires_at)`  
8. Load script for redirects  

## Failure scenarios

| Failure | Handling |
|---------|----------|
| Duplicate code race | Catch unique violation; retry allocate |
| Cache stampede on hot code | Singleflight on load |
| DB down | Creates fail; redirects may still hit cache (document degrade) |
| Abuse create flood | 429 + Retry-After |
| Open redirect schemes | Validate http/https only |

## Testing strategy

- **Run:** `bash run-tests.sh` — shorten/resolve, rate limit, expiry  
- Unit: code encoder; URL allowlist  
- Rate limiter allows N then reject  
- Expiry: redirect empty after TTL  

## Performance considerations

- Redirect path: cache → DB; no rate-limiter lock  
- Connection pool size ≪ VT count  
- Avoid synchronized logging per redirect  

## Scaling strategy

- Shard by `hash(code)`; Redis cluster for cache  
- Separate create vs redirect pools  
- Async click analytics via queue (link to 08)  

## Interview discussion

“Not CRUD: uniqueness under concurrency, cache-aside, and create-path rate limiting. Clean architecture keeps JDBC and HTTP replaceable. Redirect SLO drives the design.”

**Follow-ups:** Custom aliases? Multi-region code namespace? Analytics consistency?

## Skeleton layout

```text
06-url-shortener-service/
  README.md
  src/shortener/domain/...
  src/shortener/application/...
  src/shortener/adapters/...
  run.sh
```

See `src/` for starter ports and domain types; complete services per plan.

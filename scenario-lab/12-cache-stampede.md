# Cache Stampede — TTL alignment thunders the database

## Incident

Every night at 00:00 UTC, `product-api` DB CPUs spike, p99 latency detonates for ~2 minutes, then recovers. Caffeine/Redis cache TTLs for product entities were set to `expireAfterWrite(1h)` with mass priming at deploy / top-of-hour refreshes. A marketing push increased QPS. On-call sees identical keys missing simultaneously.

## Symptoms

- Periodic latency/error spikes aligned to clock boundaries
- Cache hit ratio cliffs to near 0 for a keyspace, then recovers
- DB connections surge in the same window
- Multiple app instances recompute the same expensive keys
- Between spikes, system is healthy

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| Cache | Redis + local Caffeine L1 |
| TTL | 3600s fixed, many keys loaded together (illustrative) |
| QPS | ~3k RPS (illustrative); spike window DB QPS ×20 |
| Loader | `cache.get(key, k -> dbLoad(k))` without single-flight across nodes |

## Metrics

```
cache.hit_ratio           0.98 → 0.05 (for 90s) → 0.97
db.qps                    200 → 4000
http.p99                  30ms → 2.5s
redis.get.miss            spike aligned to :00
```

Illustrative: periodic synchronized misses ⇒ stampede.

## Logs

```
2026-08-03T00:00:00.120Z WARN  ProductRepo - dbLoad productId=P-1
2026-08-03T00:00:00.121Z WARN  ProductRepo - dbLoad productId=P-1
2026-08-03T00:00:00.121Z WARN  ProductRepo - dbLoad productId=P-1
2026-08-03T00:00:00.250Z ERROR HikariPool - Connection not available ...
2026-08-03T00:01:40.000Z INFO  metrics - hit_ratio recovered 0.96
```

Duplicate `dbLoad` for same id across threads/instances.

## Initial Hypotheses

1. Synchronized TTL expiry → cache stampede / thundering herd
2. Redis failover causing mass invalidation
3. Cron job scanning products at midnight
4. GC pause coincidentally at :00 (weak)
5. Deploy cadence hourly (check)

## Questions

- Why do hit-ratio cliffs align to wall clock?
- What is “single-flight” / request coalescing?
- What assumption does identical TTL on co-loaded keys make?
- What if QPS ×100 at expiry — DB meltdown size?
- Probabilistic early expiration vs locking — trade-offs?

## Investigation

1. **Timeline**  
   Overlay hit ratio, DB QPS, clock — periodicity is the tell.

2. **Key events**  
   Log miss storms for popular keys; count concurrent loads per key.

3. **TTL config**  
   Fixed 3600s + bulk fill ⇒ shared expiry.

4. **Cross-instance**  
   Each pod independently loads on miss — N-fold stampede.

5. **Rule out failover**  
   Redis metrics/logs stable.

6. **Thread/DB**  
   Pool exhaustion secondary to stampede (scenario 06 pattern).

7. **Code**  
   Check for `synchronized` per key (local only) vs distributed lock / `singleflight`.

## Root Cause

Large cohorts of cache entries share the same expiry instant. At TTL, concurrent requests across threads **and** pods miss together and all hit the DB for hot keys (no distributed single-flight). The stampede saturates the DB/pool, causing the periodic outage window. Healthy steady state between cliffs hides the design flaw.

## Resolution

- **Immediate:** raise TTL temporarily; rate-limit DB loader; serve stale on error (stale-while-revalidate).
- **Fix:** jitter TTLs; probabilistic early refresh; per-key single-flight (local + Redis lock / “loading” sentinel); soft TTL with background refresh for hottest keys.
- Avoid mass simultaneous warm at deploy without jitter.

## Prevention

- Alert on hit-ratio derivative and periodic DB QPS
- Load test expiry cliffs explicitly
- Cache library defaults review (jitter helpers)
- Runbooks: stampede vs failover differentiation

## Principal Engineer Discussion

- Consistency: serving stale products vs strict freshness for price/stock.
- L1+L2 cache hierarchy: where does coalescing live?
- Is Redis the right tier for this keyspace vs CDN edge?
- How do you explain stampede vs dogpile vs thundering herd in an interview without hand-waving?

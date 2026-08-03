# Database Pool Exhaustion — checkout waits on Hikari

## Incident

`orders-api` latency explodes; clients see 500s with `HikariPool - Connection is not available, request timed out after 30000ms`. CPU is fine. Postgres primary CPU is moderate. A migration to virtual threads for the web tier shipped this morning to “handle more concurrency.” Pool size left at 20.

## Symptoms

- Hikari acquire timeouts
- Active connections stick at `maximumPoolSize`
- Many requests blocked acquiring connections
- DB `pg_stat_activity` shows fewer than expected runnable queries; some `idle in transaction`
- Thread count (virtual) very high; platform carriers normal

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25**, virtual threads enabled for Tomcat/requests |
| Pool | HikariCP `maximumPoolSize=20`, `connectionTimeout=30s` (illustrative) |
| QPS | ~800 RPS (illustrative); was “fine” on platform threads with lower concurrency |
| DB | Postgres primary, `max_connections` 200 shared across services |

## Metrics

```
hikaricp.connections.active     20/20
hikaricp.connections.pending    200–2000
http.p99                        50ms → >30s
vt.count                        tens of thousands (illustrative)
postgres.connections            ~40 from this service (held)
txn.duration.p99                up (some long)
```

Illustrative: pool maxed + VT fan-out ⇒ concurrency exceeds pool, classic exhaustion.

## Logs

```
2026-08-03T09:18:22.441Z ERROR [tomcat-virt-18422] com.zaxxer.hikari.pool.HikariPool - Connection is not available, request timed out after 30000ms
2026-08-03T09:18:22.450Z WARN  [tomcat-virt-18422] OrderService - createOrder failed user=u-991
2026-08-03T09:18:30.002Z INFO  [db-metrics] active=20 idle=0 threadsAwaiting=1530
2026-08-03T09:19:01.110Z WARN  postgres - duration: 18441.221 ms  statement: SELECT ... FOR UPDATE
```

## Initial Hypotheses

1. Pool sized too small for new concurrency (VT amplified concurrent requests)
2. Connection leak (not returned to pool)
3. Long transactions / `FOR UPDATE` holding connections
4. Slow queries under lock contention
5. DB primary actually saturated (CPU/IO) — check before blaming the client

## Questions

- Why can virtual threads make a previously “OK” pool suddenly fail? (They are not “faster”; they raise **concurrency**.)
- What do you check first: Hikari metrics, `pg_stat_activity`, or thread dump?
- What assumption does `maximumPoolSize=20` encode about in-flight work?
- What if traffic ×100 — do you grow the pool to 2000? Why is that dangerous?
- How does this relate to [principal-engineer/scenarios/connection-pool-exhaustion.md](../principal-engineer/scenarios/connection-pool-exhaustion.md) and [virtual-threads/](../virtual-threads/)?

## Investigation

1. **Pool metrics**  
   Active/idle/pending/timeouts. Confirm saturation vs leak (active < max but timeouts ⇒ different).

2. **DB side**  
   `pg_stat_activity`: states `idle in transaction`, long `FOR UPDATE`, wait events. Correlate PIDs to app.

3. **Stacks**  
   `jcmd Thread.print` — thousands of VT waiting in Hikari `getConnection`; others inside JDBC. See [performance-engineering/thread-analysis.md](../performance-engineering/thread-analysis.md).

4. **Transaction boundaries**  
   Find `@Transactional` spanning HTTP calls / sleep / external I/O — holds connections.

5. **Leak check**  
   Hikari `leakDetectionThreshold`; ensure try-with-resources on connections if raw JDBC.

6. **Before/after VT**  
   Same RPS, much higher simultaneous blocked requests → pool math breaks. Read [principal-engineer/scenarios/connection-pool-exhaustion.md](../principal-engineer/scenarios/connection-pool-exhaustion.md) and [principal-engineer/scenarios/virtual-threads-no-gain.md](../principal-engineer/scenarios/virtual-threads-no-gain.md).

7. **JFR / spans**  
   Time spent in `HikariPool.getConnection` vs SQL execution — separates queueing from query slowness.

## Root Cause

Virtual threads allowed **orders of magnitude more concurrent requests**, each needing a JDBC connection. Hikari remained at 20. Combined with a checkout path that holds a connection across a slow `SELECT … FOR UPDATE` and subsequent pricing HTTP call inside a transaction, connections stay checked out far longer than before → pool exhaustion and acquire timeouts. VT did not make SQL faster; they removed the old accidental concurrency limit of the platform thread pool.

## Resolution

- **Immediate:** reduce admission (load shed / limit concurrent requests); roll back VT on this path or introduce a semaphore of size ≈ pool; kill long `idle in transaction` sessions.
- **Fix:** never hold DB connections across remote calls; shrink transaction scope; size pool to DB capacity; **bound** concurrent DB users (semaphore) even with VT.
- Tune pool only after fixing hold time — blind increases fight `max_connections`.

## Prevention

- Pool pending / acquire-timeout alerts
- Integration test: VT + pool size 5 under concurrent load must not deadlock forever
- Arch rule: no remote I/O inside `@Transactional`
- Capacity doc: `max_in_flight_db ≤ sum(pool sizes) ≤ DB max_connections − headroom`

## Principal Engineer Discussion

- Semaphore-in-front-of-pool vs larger pools vs more replicas — trade-offs.
- Is VT default-on for JDBC apps responsible engineering without pool redesign?
- How do you educate teams that “more concurrency” ≠ “more throughput” when a scarce pool is the limiter?
- Multi-tenant DB: who owns connection budgets across services?

# Connection Leak — JDBC connections vanish over a shift

## Incident

`billing-worker` starts the day with a healthy Hikari pool (`idle` ≈ max). By late afternoon, `active` stays high even when job concurrency is low; eventually new jobs fail with connection acquire timeouts. Restart fixes it for hours. A recent change added streaming export via `ResultSet` + pass-to-caller without try-with-resources on all paths. Exception paths skip `close()`.

## Symptoms

- `hikaricp.connections.active` trends up over hours unrelated to load
- `leakDetectionThreshold` warnings (if enabled) cite known stacks
- DB side: many sessions `idle` from this app, held for long durations
- No classic Java heap OOM (this is pool/resource leak)
- Full GC does not return connections

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| Pool | Hikari `maximumPoolSize=30`, `leakDetectionThreshold=60s` (illustrative) |
| Jobs | ~100/min billing exports (illustrative) |
| Pattern | DAO returns open `ResultSet`/connection to service layer on some paths |

## Metrics

```
hikaricp.connections.active    2 → 28 over 6h (load flat)
hikaricp.connections.idle      28 → 2
db.sessions.from_billing       matches active
job.success.rate               drops when active≈max
```

Illustrative: active rises at constant load ⇒ leak, not traffic.

## Logs

```
2026-08-03T10:14:22.001Z WARN  com.zaxxer.hikari.pool.ProxyLeakTask - Connection leak detection triggered for connection ..., stack trace follows
java.lang.Exception: Apparent connection leak detected
    at com.acme.billing.ExportDao.streamInvoices(ExportDao.java:88)
    at com.acme.billing.ExportService.export(ExportService.java:41)
2026-08-03T16:02:11.440Z ERROR HikariPool - Connection is not available, request timed out after 30000ms
```

## Initial Hypotheses

1. Connection / `ResultSet` / `Statement` not closed on error paths
2. Connection held across large stream processing intentionally but unbounded
3. Transaction manager not completing (idle in transaction)
4. Pool misconfigured (`minimumIdle` weirdness) — unlikely with leak traces
5. Finalizer-based close assumptions (anti-pattern)

## Questions

- Why doesn’t GC solve a connection leak?
- What do you enable first in staging: leak detection or heap dump?
- What assumption does “caller will close” make across refactors?
- What if job rate ×100 — time-to-exhaustion?
- How do try-with-resources and pool proxies interact?

Cross-links: [principal-engineer/scenarios/connection-pool-exhaustion.md](../principal-engineer/scenarios/connection-pool-exhaustion.md), scenario 06 (exhaustion from concurrency vs leak).

## Investigation

1. **Distinguish leak vs load**  
   Active↑ while QPS flat ⇒ leak. Active≈concurrency ⇒ sizing.

2. **Hikari leak traces**  
   Trust the stack; find unclosed path.

3. **pg_stat_activity / DB session list**  
   Idle sessions aged hours from billing user.

4. **Code audit**  
   All exits from `streamInvoices`: early returns, exceptions, iterator abandoned mid-stream.

5. **Thread dump**  
   Optional; may show holders if still in mid-export.

6. **JMX pool beans**  
   Confirm not returning to idle.

7. **Do not start with heap dump**  
   Wrong tool unless also suspecting object retention of connection wrappers.

## Root Cause

`ExportDao.streamInvoices` borrowed a connection and handed a live `ResultSet` upward. On client disconnect / mid-stream exceptions / abandoned iterators, **close was skipped**. Connections remained checked out until restart. Leak detection stacks pointed at the DAO; afternoon exhaustion was the pool finally emptying.

## Resolution

- **Immediate:** restart workers; enable/keep leak detection; throttle jobs.
- **Fix:** try-with-resources at ownership boundary; stream with callback/`Stream` that closes on terminal op; never return open JDBC resources across layers; ensure Spring `DataSourceUtils` / `@Transactional` boundaries complete.
- Add integration test that forces exceptions mid-stream and asserts pool idle returns to baseline.

## Prevention

- `leakDetectionThreshold` in all non-prod and carefully in prod
- ArchUnit rules: DAO methods must not return `ResultSet`/`Connection`
- Pool active-vs-QPS anomaly alert
- Prefer JOOQ/Spring templates that own lifecycle

## Principal Engineer Discussion

- Ownership models for resources in Java APIs (who closes?).
- Streaming large ResultSets vs pagination — memory and connection duration trade-offs.
- Should production enable leak detection always? Overhead vs SEV cost.
- Connection leak vs memory leak: teaching juniors the difference with this incident.

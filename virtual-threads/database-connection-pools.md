# Database Connection Pools (VT Reality)

The #1 reason “virtual threads didn’t help.”

## Mental Model

```text
10_000 virtual threads
    ↓ acquire
30 Hikari connections
    ↓
Database max_connections
```

VT removes the **application thread** ceiling, not the **connection** ceiling.

## Mechanics

Blocking `DataSource.getConnection()` parks the VT (good — unmounts). Latency becomes wait-for-connection. Pool exhaustion → timeout exceptions.

## Code

```java
HikariConfig cfg = new HikariConfig();
cfg.setMaximumPoolSize(40);           // match DB capacity / pods
cfg.setConnectionTimeout(1000);       // fail fast
cfg.setPoolName("orders-pool");

// In service — always timeout queries too
```

## Production Scenario — database-heavy service

Before VT: 200 platform threads, pool 40 → 160 threads waiting on pool (wasteful OS threads).  
After VT: 10K VTs, pool 40 → same 40 queries, but cheap waiters. **Throughput unchanged** if DB was already the limit; **efficiency** improved (fewer OS threads). Throughput rises only if you were thread-limited *below* DB capacity before.

## Failure Scenario — connection pool exhaustion

Symptoms: `SQLTransientConnectionException`, rising p99, low DB CPU (app queued).  
Wrong fix: maxPoolSize=10_000.  
Right fix: query performance, pool sizing math, pod count × pool ≤ DB max, caching, timeouts, shed load.

## Sizing Sketch

```text
connections ≈ pods × poolSize ≤ db.max_connections - headroom
throughput ≈ connections / avg_query_latency
```

## Interview / PE

Does VT increase DB throughput? How do you size Hikari with VT? What metrics?

### Related

[downstream-limitations.md](./downstream-limitations.md) · [scenarios.md](./scenarios.md) · [when-vt-do-not-help.md](./when-vt-do-not-help.md)

# Scenario: Database Pool Exhaustion After Scale / VT

## Context

Checkout API opens Hikari pool of 20. After enabling virtual threads (or raising Tomcat threads), p99 spikes; threads wait in `getConnection`; DB CPU still moderate. Product asks for “more connections.”

## Constraints

- Postgres `max_connections` shared across services  
- Strong transactional checkout; cannot drop ACID casually  
- Peak RPS must hold with error budget  

## Options

| Option | Approach |
|--------|----------|
| **A. Raise pool to 200** | Match concurrent waiters |
| **B. Cap in-flight** | Semaphore / load-shed ≈ pool size × utilization model |
| **C. Shorten hold time** | Fix N+1, shrink txn, timeouts |
| **D. Read replicas / split** | Move reads off primary pool |

## Trade-offs

| Option | Buys | Sells |
|--------|------|-------|
| A | Maskes wait | DB thrash, connection storms, multi-service OOM of connections |
| B | Protects DB | Some 429/503 under overload — correct |
| C | Real capacity | Engineering time |
| D | Read scale | Lag / consistency product rules |

## Decision

**C then B; A last and shared-budgeted.** Throughput ≈ `poolSize / mean_hold_time`. VT makes waiters cheap — it does not create connections. Size pool from **DB capacity and query time**, not thread count.

## Reasoning

Pool exhaustion is usually **hold-time × concurrency**, not “need VT” or “need 200 connections.” Prove with pool wait metrics + slow query / txn span breakdown.

## Risks

- Leak (unclosed connections) mistaken for sizing  
- Retry storms amplify pool pressure  
- Multi-service silent raise of pools → Postgres connection limit  

## Migration

| Wave | Work | Abort |
|------|------|-------|
| 0 | Metrics: active/idle/wait, txn duration | — |
| 1 | Timeouts + fix long txns / N+1 | — |
| 2 | Admit ≤ ~pool effective concurrency | Checkout error budget burn |
| 3 | Coordinated pool budget across services | Connection refused at DB |

## Success metrics

- Pool wait p99 within SLO  
- No rise in DB connection errors  
- Hold time down; pool size stable or lower  

Related: [../../jdbc/](../../jdbc/) · [../../virtual-threads/database-connection-pools.md](../../virtual-threads/database-connection-pools.md) · [../../scenario-lab/06-database-pool-exhaustion.md](../../scenario-lab/06-database-pool-exhaustion.md) · [../../scenario-lab/14-connection-leak.md](../../scenario-lab/14-connection-leak.md)

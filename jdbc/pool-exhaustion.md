# Pool Exhaustion

## Problem

Demand for connections exceeds `maximumPoolSize` (or DB `max_connections`) — waiters pile up.

## Symptoms

`SQLTransientConnectionException`: connection not available after timeout; rising p99; low DB CPU (app queued).

## Causes

| Cause | Detail |
|-------|--------|
| Leak | Connections not returned |
| Undersized pool | Traffic × latency needs more |
| Slow queries | Connections held too long |
| Oversize fleet | pods × pool > DB max |
| VT stampede | Cheap threads → many waiters |

## Naive “fix”

`maximumPoolSize=500` → kill the database.

## Real Fix

1. Fix leaks / slow SQL  
2. Size pool from math + load tests  
3. Fail fast (`connectionTimeout`)  
4. Shed load at edge  
5. Scale DB read replicas for read-heavy  

## High Concurrency Scenario

10k VT requests, pool 20 → 9980 wait — expected. Throughput capped by DB, not threads.

### Related

[connection-pooling.md](./connection-pooling.md) · [scenarios.md](./scenarios.md) · [principal-decisions.md](./principal-decisions.md)

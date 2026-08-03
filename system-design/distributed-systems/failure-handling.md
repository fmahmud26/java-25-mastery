# Failure Handling

Distributed failure is **normal**. Handle with a deliberate loop: detect → timeout → isolate → degrade → recover → learn.

## Classes of failure

| Class | Example | First response |
|-------|---------|----------------|
| Crash | OOM kill | Restart, health checks, load shed peers |
| Slow | GC / dep latency | Timeouts, hedging carefully, CB |
| Byzantine-ish | Wrong data | Checksums, schema, poison DLQ |
| Partition | AZ net split | CAP choice per op |
| Cascade | Retry storm | Budgets, bulkheads |
| Poison | Bad message | DLQ, skip, fix |

## Timeouts are load shedding

No timeout = unlimited queueing. Every outbound call needs deadline; propagate remaining budget.

## Degradation menu (safe vs unsafe)

| Safe degrade | Unsafe |
|--------------|--------|
| Skip recommendations | Invent payment success |
| Serve stale catalog cache | Skip authz |
| Read-only mode | Drop durable writes silently |

## Production checklist

1. Timeouts + CB + bulkhead on every dep  
2. Idempotent writes  
3. Explicit PENDING for unknown money states  
4. Lag/error SLOs on async paths  
5. Runbooks tied to dashboards  

Related: [fault-tolerance.md](./fault-tolerance.md), [retry.md](./retry.md), [backpressure.md](./backpressure.md), [scenarios/retry-storm.md](./scenarios/retry-storm.md).

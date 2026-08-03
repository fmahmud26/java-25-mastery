# Downstream Limitations

Everything scarcer than virtual threads. Principal interviews live here.

## Problem

Enable VT → request concurrency jumps → dependencies melt. Threads were never the only scarce resource.

## Mental Model

```text
VT concurrency  ≤  min(
  admitted requests,
  DB pool,
  HTTP conn limits,
  dependency RPS,
  CPU for non-wait work,
  memory
)
```

## Catalog of Ceilings

| Downstream | Limit mechanism |
|------------|-----------------|
| PostgreSQL | `max_connections`, CPU, locks, I/O |
| Hikari | `maximumPoolSize`, timeout |
| PSP / tax API | Rate limits, latency, concurrency |
| Redis | Conn + CPU |
| Disk | IOPS |
| CPU crypto | Cores |
| Edge proxy | Max connections / workers |

## Production Scenario — high-concurrency REST

VT adoption → more concurrent waits → **amplified** load on dependencies. Without bulkheads, VT makes outages **worse** than a capped platform pool (which accidentally limited stampede).

## Failure Scenario — cascade

1. Traffic spike  
2. 50K VTs wait on 40 DB connections  
3. HTTP timeouts fire → retries  
4. Retry storm doubles DB pressure  
5. Circuit never opens → total outage  

Prevention: fail-fast timeouts, no blind retries, bulkheads, load shed at edge.

## Architecture Controls

1. Timeouts everywhere  
2. Bulkheads / semaphores per dependency  
3. Circuit breakers  
4. Bounded admission at edge (max in-flight)  
5. Caching / async where appropriate  
6. Pool sizing across pods  

## PE Decision

Treat “enable VT” and “redesign admission + pool math” as one change set. Ship both or neither under load.

## Interview / PE

Name three limits VT doesn’t remove. How can VT increase outage blast radius? What metric proves the ceiling?

### Related

[database-connection-pools.md](./database-connection-pools.md) · [when-vt-do-not-help.md](./when-vt-do-not-help.md) · [principal-architecture-decisions.md](./principal-architecture-decisions.md)

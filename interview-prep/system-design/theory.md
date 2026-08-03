# System Design — Theory

Senior bar: turn a product prompt into **capacity, APIs, data flow, failure modes, evolution**.

## Core vocabulary

| Term | Meaning |
|------|---------|
| Scalability | Handle growth (vertical/horizontal) |
| Availability | % uptime / successful requests |
| Latency / throughput | Time per request vs work per second |
| Consistency | How replicas agree (strong → eventual) |
| CAP (practical) | Under partition, trade availability vs consistency |
| Idempotency | Safe retries without duplicate effects |

## Interview skeleton (45–60 min)

1. Clarify scope, QPS, data size, SLO, consistency, regions.  
2. API + entities.  
3. High-level diagram: client → LB → services → stores → async.  
4. Deep dive the hard part (shard, cache, exactly-once*, hot key).  
5. Failures: timeout, retry, backpressure, partition.  
6. 10× scale / new features.

## Building blocks

| Block | When |
|-------|------|
| Cache | Read-heavy, tolerate staleness window |
| Shard / partition | Data or write volume exceeds one node |
| Replica | Read scale + HA |
| Queue / stream | Decouple, buffer spikes, async workflows |
| Rate limit | Protect dependency / fair use |
| Circuit breaker | Fail fast when dependency sick |

Related: [scalability.md](../../system-design/fundamentals/scalability.md), [cap-theorem.md](../../system-design/fundamentals/cap-theorem.md), [README.md](../../system-design/README.md).

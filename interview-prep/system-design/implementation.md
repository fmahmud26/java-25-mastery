# System Design — Implementation

Java-centric shape of a design (not code golf).

```text
Client
  → API Gateway / LB
    → App instances (Java 25, VT-friendly blocking I/O OK)
      → Cache (Redis)
      → Primary DB (sharded / partitioned as needed)
      → Outbox → Kafka → workers
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Sync user read | Cache-aside + DB |
| Reliable side effects | Outbox + consumers |
| Spike absorption | Queue with backpressure |
| Cross-region | Replicate; name consistency model |
| Hot key | Local cache + shard key redesign / coalescing |
| Multi-instance lock | DB lease / Redis lock with TTL (careful) |

## Java notes for SD interviews

- Connection pools sized vs concurrency model (VT ≠ infinite DB connections).  
- Timeouts on every remote call; retries only if idempotent.  
- Prefer explicit consistency claims over “exactly-once” slogans.

Related: [java-focused.md](../../system-design/java-focused.md), [caching.md](../../system-design/fundamentals/caching.md), [message-queues.md](../../system-design/distributed-systems/message-queues.md).

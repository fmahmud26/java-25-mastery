# System Design — Internals

Mechanisms interviewers expect you to unpack when they say “how does that work?”

## Data path internals

| Mechanism | Point |
|-----------|-------|
| Cache-aside | App reads cache → miss → DB → fill; TTL/invalidation strategy |
| Write-through / write-behind | Latency vs durability trade |
| Partitioning | Hash/range/directory; rebalancing pain |
| Replication | Leader/follower; lag; failover |
| Kafka-ish log | Partition = ordered unit; consumer group = parallelism |
| Quorum / consensus | For strong leadership (sketch Raft-level, don’t fake details) |

## Failure mechanics

| Pattern | Internals cue |
|---------|---------------|
| Retry + backoff | Jitter; max attempts; idempotency keys |
| Circuit breaker | Closed/open/half-open; error budget |
| Bulkhead | Isolate pools/threads/connections per dependency |
| Distributed lock | Fencing tokens / lease TTL; don’t trust “SET NX” alone for safety |

## Consistency spectrum

Strong → read-your-writes → eventual. Name **where** the user sees staleness.

Related: [partitioning.md](../../system-design/fundamentals/partitioning.md), [replication.md](../../system-design/fundamentals/replication.md), [idempotency.md](../../system-design/distributed-systems/idempotency.md).

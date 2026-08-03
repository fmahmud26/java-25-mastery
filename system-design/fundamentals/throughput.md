# Throughput

**Definition:** successful work per unit time (QPS, msgs/sec, MB/s). Often traded against latency.

## Little’s law (interview gold)

`L = λ × W` — concurrency ≈ arrival rate × service time.  
If λ=10k QPS and W=100ms, you need ~1000 in-flight requests. Size thread pools, DB connections, and queue depth from this.

## Scaling throughput

| Lever | Notes |
|-------|-------|
| More consumers/replicas | Parallelism until partition or lock bound |
| Batching | Higher throughput, higher latency |
| Pipelining | Keep network full |
| Partitioning | Parallel shards |
| Reduce work/request | Projection, compression, sampling |

## Backpressure

When downstream saturates, **signal upstream** (HTTP 429, queue lag, TCP window) instead of unbounded buffering (OOM).

Principal answer: “Consumer lag alert → autoscale → if still behind, shed load / degrade.”

## Throughput vs correctness

At-least-once + idempotent processing sustains throughput under retries. Exactly-once protocols often lower throughput — justify when money/inventory requires it.

Related: [scalability.md](./scalability.md), [message-queues](../distributed-systems/message-queues.md), [latency.md](./latency.md).

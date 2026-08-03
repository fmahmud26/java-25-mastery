# Scalability

**Definition:** absorb growth in traffic, data, or tenants by adding resources **without linear human toil**, while holding SLOs.

## Dimensions

| Axis | Grows | Primary levers |
|------|-------|----------------|
| Traffic | QPS/connections | Horizontal service replicas, cache, CDN, async |
| Data | TB/rows | Partitioning, tiered storage, compaction |
| Tenancy | Customers | Isolation, noisy-neighbor limits, shard by tenant |
| Features | Complexity | Service boundaries, async fan-out |

## Vertical vs horizontal

| | Vertical | Horizontal |
|--|----------|------------|
| **What** | Bigger box | More boxes |
| **Why use** | Simple; strong single-node consistency | Elastic; fault isolation |
| **Fails when** | Hardware ceiling; blast radius | Shared state becomes bottleneck; chatty cross-node |

**Principal stance:** scale **stateless** tiers horizontally first; scale **state** via partition + replica, not infinite vertical SQL.

## Bottleneck-first method

1. Measure or estimate where time/capacity goes (CPU, disk IOPS, lock, network, dependency QPS).  
2. Apply the cheapest lever that moves that bottleneck.  
3. Re-estimate — scaling the wrong tier wastes money.

Common order for read-heavy products: **CDN → app cache → Redis → read replicas → shard primary**.

## Statelessness (why it matters)

If app nodes hold sticky session state in memory, autoscaling and deploys shed users. Put sessions in Redis/DB/JWT; keep nodes disposable.

## Scale cues in interviews

| Symptom | Likely fix |
|---------|------------|
| DB CPU high on reads | Cache, replica, cover index |
| DB CPU high on writes | Batch, queue, shard, reduce indexes |
| Hot partition | Salt key, separate hot entity, local cache |
| Queue lag | More consumers, partition keys, backpressure upstream |
| GC/CPU on app | VT vs platform threads, alloc, payload size |

## Anti-patterns

- “Microservices for scale” when monolith + DB replica would do  
- Sharding before indexing/caching  
- Sync fan-out to 12 dependencies on the user request path  

Related: [throughput.md](./throughput.md), [partitioning.md](./partitioning.md), [caching.md](./caching.md), [load-balancing.md](./load-balancing.md).

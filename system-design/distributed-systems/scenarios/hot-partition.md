# Scenario: Hot Partition Meltdown

## Production story

Flash sale on one SKU. Inventory sharded by `hash(sku_id)` — one shard CPU 100%, lock waits explode; other shards idle. Site-wide checkout timeouts as connection pools block on hot shard.

## What’s failing

Partition key cardinality collapse + sync waiters.

## Bad responses

- Add identical shards (hot key still hashes one place)  
- Global retry storm  
- Cache inventory without atomic decrement protocol

## Principal response

1. Shed load: queue or lottery for that SKU; 429 others politely.  
2. Isolate hot key: dedicated store / in-memory atomic with write-through; or salt `sku#0..N` with careful aggregation.  
3. Per-SKU work queue to serialize decrements without killing DB.  
4. Bulkhead pools so hot SKU can’t take all checkout connections.  
5. Longer term: cell or special “events” inventory service.

## Trade-offs

Fairness vs throughput; complexity of salted keys vs oversell risk if aggregation wrong.

## Interview probes

- Why more Kafka consumers didn’t help?  
- Design non-oversell under 100k QPS one SKU.  

Related: [../partitioning.md](../partitioning.md), [../backpressure.md](../backpressure.md), [../fault-tolerance.md](../fault-tolerance.md).

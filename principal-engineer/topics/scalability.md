# Scalability (PE lens)

Find the **ceiling**, then choose the cheapest lever that raises it without lying about consistency.

## Ceiling hunt

| Layer | Saturation signal | Lever |
|-------|-------------------|-------|
| App CPU/GC | Util, alloc rate | Fix code, scale out, reduce work |
| DB primary | CPU, IOPS, locks | Cache, CQRS, shard, queue writes |
| Hot key/partition | One shard hot | Salt, isolate entity, queue |
| Dependency | p99, 429s | Bulkhead, cache, async |
| People/ops | Pages, toil hours | Platform, automate, reduce surface |

## 100× fallacy

100× traffic rarely needs 100× servers. Often: 10× cache hit improvement + async off critical path + 3× boxes. Prove with a model before a rewrite.

## Strategy link

Scale plans belong in [technical-strategy.md](./technical-strategy.md) as sequenced bets, not a panic weekend.

Related: [../system-design/fundamentals/scalability.md](../../system-design/fundamentals/scalability.md), [scenarios/scale-100x.md](../scenarios/scale-100x.md).

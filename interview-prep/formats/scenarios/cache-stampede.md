# Scenario: Cache stampede

## Prompt

Key expires; 5k VT stampede origin; DB CPU melts. Design mitigation.

## Options

Singleflight, probabilistic early expire, lock per key, serve stale. Trade-offs latency vs freshness. Result: origin QPS cap metric.

## Deep sources

- [../../../scenario-lab/12-cache-stampede.md](../../../scenario-lab/12-cache-stampede.md)
- [../../../system-design/fundamentals/caching.md](../../../system-design/fundamentals/caching.md)
- [../../../system-design/systems/distributed-cache.md](../../../system-design/systems/distributed-cache.md)
- [../../../low-level-design/systems/cache.md](../../../low-level-design/systems/cache.md)
- PE: [../../../principal-engineer/scenarios/latency-cliff.md](../../../principal-engineer/scenarios/latency-cliff.md)

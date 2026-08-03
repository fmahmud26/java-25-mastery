# Cache

**Assumption:** in-process local cache library (Caffeine-like). Distributed Redis is an optional `CacheStore` port, not the first sketch.

## Requirements

- `get/put/invalidate` by key  
- TTL and/or size-based eviction  
- Loader/read-through optional  
- Thread-safe under concurrent reads/writes  
- Stampede control when many miss same key  
- Metrics hooks (hit/miss)  

**Non-goals:** full Redis cluster topology; CAP lecture as the answer.

## Use cases

1. Get with optional load function  
2. Put / putIfAbsent  
3. Invalidate key / namespace  
4. Stats  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `Cache<K,V>` | Capacity/TTL policies honored |
| `Entry` | Value + expiry metadata |
| `EvictionPolicy` | LRU/LFU/size weight |
| `LoadFunction` | Compute on miss |

**Why policy object:** eviction is the changeable algorithm; map storage is commodity.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `LocalCache` | Facade API | |
| `Store` | Concurrent map of entries | |
| `EvictionPolicy` | Choose victim | Strategy |
| `Expiry` | TTL checks | |
| `SingleflightLoader` | Coalesce loads per key | Stampede |

## Interfaces

| Port | Why |
|------|-----|
| `Cache<K,V>` | App-facing |
| `EvictionPolicy` | LRU vs TinyLFU later |
| `Ticker`/`Clock` | TTL tests |
| `RemovalListener` | Side effects |
| `RemoteCacheStore` (optional) | L2 |

## Relationships

```text
LocalCache → Store → ConcurrentHashMap
LocalCache → EvictionPolicy
LocalCache → SingleflightLoader → user loader
```

## SOLID

- **OCP:** new eviction  
- **SRP:** storage ≠ eviction ≠ loading  
- **DIP:** clock/ticker injected  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | Eviction | |
| Decorator | Metrics cache wrapper | |
| Proxy/read-through | Loading cache | |
| Singleflight / request coalescing | Miss storm | |

## Thread safety

- `ConcurrentHashMap` for store  
- Per-key load lock (striped) so only one loader runs  
- Eviction under size limit: approximate LRU OK — say **why** (throughput vs perfect accuracy)  
- Don’t hold key lock while invoking slow listener I/O if avoidable  

## Error handling

| Failure | Behavior |
|---------|----------|
| Loader exception | Propagate; don’t cache failure unless negative-cache policy set |
| Eviction race | Benign overwrite rules documented |
| Null keys/values | Forbid or policy — be explicit |

## Extensibility

| Change | Touch |
|--------|-------|
| Weight-based size | Entry weight + policy |
| Refresh-ahead | Scheduler + loader |
| Two-level cache | Compose local + remote port |

## Testing

- TTL expiry with fake ticker  
- Concurrent miss → loader called once  
- Size bound not exceeded beyond policy tolerance  
- Invalidate visible to subsequent get  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Approximate eviction | Speed | Global lock perfect LRU |
| Singleflight | Protect origin | Cache each miss independently — thundering herd |
| In-process first | LLD scope | Jump to Redis — skips concurrency design |
| No null values default | Ambiguity | Map null = miss |

**Staff emphasis:** **stampede + expiry + concurrency** — if you only draw `Map`, you fail senior bar.

# Rate Limiter

**Assumption:** library used at API gateway / service edge in-process; distributed limiter via port when scaled.

## Requirements

- Allow/deny requests per key (user, IP, API token)  
- Configurable limit + window / refill semantics  
- Thread-safe counters  
- Pluggable algorithms (token bucket, sliding window, fixed window)  
- Optional distributed store for multi-instance  

**Non-goals:** WAF product; bot ML scoring.

## Use cases

1. `tryAcquire(key)` / `tryAcquire(key, cost)`  
2. Hot-reload config for a key tier  
3. Metrics: allowed vs throttled  
4. Compose multiple limiters (per-user AND per-IP)  

## Domain model

| Concept | Invariant |
|---------|-----------|
| `RateLimitPolicy` | limit, period, algorithm params |
| `Limiter` | Decision ALLOW/DENY + optional retry-after |
| `Key` | Tenant dimension |
| `Clock` | Monotonic preferred for intervals |

**Why algorithm interface:** product often starts fixed-window then upgrades without API break.

## Classes

| Class | Responsibility | Why |
|-------|----------------|-----|
| `RateLimiter` facade | API | |
| `TokenBucketLimiter` | Tokens + refill | Smooth traffic |
| `FixedWindowLimiter` | Count per epoch | Simple |
| `SlidingWindowLimiter` | Fairer | Less boundary burst |
| `KeyStateStore` | Per-key state | Local/remote |

## Interfaces

| Port | Why |
|------|-----|
| `RateLimitAlgorithm` | `tryAcquire(state, cost, now)` |
| `KeyStateStore` | Local map vs Redis |
| `Clock` | Tests |
| `LimiterConfigSource` | Dynamic policies |

## Relationships

```text
RateLimiter → KeyStateStore
RateLimiter → RateLimitAlgorithm
CompositeRateLimiter → List<RateLimiter> (AND)
```

## SOLID

- **OCP:** new algorithm  
- **SRP:** policy config ≠ storage ≠ decision  
- **DIP:** store port for distributed  

## Design patterns

| Pattern | Where | Why |
|---------|-------|-----|
| Strategy | Algorithm | |
| Facade | RateLimiter | |
| Composite | Stacked limits | |
| Adapter | Redis store | |

## Thread safety

- Local: `ConcurrentHashMap<Key, State>` with atomic updates (LongAdder / CAS on state record)  
- Token bucket: synchronize per key or use AtomicReference CAS loop  
- Distributed: Lua/atomic increment on Redis — **mention race if naive get/set**  

## Error handling

| Failure | Behavior |
|---------|----------|
| Store unavailable (distributed) | Fail-open vs fail-closed **policy** — state choice explicitly |
| Unknown key tier | Default policy |
| Clock skew (distributed) | Prefer server-side Redis time |

## Extensibility

| Change | Touch |
|--------|-------|
| Sliding window log | New algorithm |
| Quota per day + burst per second | Composite |
| Costly endpoints weight | `cost` param |

## Testing

- Exactly N allows then deny in window with fixed clock  
- Concurrent acquires don’t exceed limit  
- Refill restores tokens  
- Composite denies if either child denies  

## Trade-offs

| Choice | Why | Rejected |
|--------|-----|----------|
| Strategy algorithms | Evolution | Hardcode fixed window only |
| Fail-closed for auth-sensitive APIs | Safety | Always fail-open — abuse during Redis outage |
| Token bucket default | Smooth UX | Fixed window — easy burst at boundary |
| Per-key atomic state | Correctness | Global lock — simple bottleneck |

### Algorithm why-brief

| Algo | Why pick | Weakness |
|------|----------|----------|
| Fixed window | Easy | Boundary burst 2× |
| Sliding window | Smoother | More state |
| Token bucket | Bursts allowed controlled | Tuning refill |
| Leaky bucket | Constant egress | Less common for HTTP |

**Staff phrasing:** “API is `tryAcquire`; algorithm and store are replaceable; I’ll call out fail-open/closed and why fixed window can double at edges.”

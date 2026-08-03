# Parallel Streams

`parallelStream()` / `.parallel()` — split source work across the **common ForkJoinPool** (by default).

> Parallel is an optimization **option**, not a default. Wrong use makes systems slower or unstable.

## What Happens

```text
source → split → ForkJoin tasks → combine
ordering constraints reduce speedup
```

Sequential vs parallel:

| | Sequential | Parallel |
|--|------------|----------|
| Threads | Calling thread | FJP workers |
| Order | Encounter order | May relax unless ordered ops |
| Best for | I/O, small n, simple | Large in-memory CPU work |
| Risk | Low | Starvation, races, wrong reduce |

## Why Useful

CPU-bound map/filter/reduce over large in-memory arrays/lists (analytics, scoring) when profiled.

## Production Example — good vs bad

```java
// Potentially OK — pure CPU over large in-memory data (measure!)
double score = features.parallelStream()
        .mapToDouble(this::cpuHeavyScore)
        .sum();

// BAD — blocking I/O on common pool
orders.parallelStream()
        .map(o -> httpClient.loadCustomer(o.customerId())) // blocks FJP
        .toList();
```

## Parallel Pitfalls (deep)

1. **Blocking I/O** on common pool starves other parallel work. Prefer sequential + virtual threads / dedicated executors.  
2. **Shared mutable state** in lambdas → races.  
3. **Non-associative reduce** → wrong answers.  
4. **Tiny data** → overhead > benefit.  
5. **Ordered short-circuit** (`findFirst`, `limit`) limits parallelism. Prefer `findAny` when order doesn’t matter.  
6. **Nested parallel** / calling parallel from FJP threads — hard to reason.  
7. **Thread-hostile libs** (SimpleDateFormat, non-thread-safe maps) inside parallel ops.

## Performance Implications

Speedup is sublinear and workload-specific. Benchmark with JMH/prod metrics. `groupingByConcurrent` / `toConcurrentMap` for parallel collects.

## Common Mistake

“Sprinkle `.parallel()` for performance” in a web request path doing DB calls — classic production incident.

## Principal Decision

1. Is work CPU-bound and large enough?  
2. Are ops pure and associative where required?  
3. Measured win on target hardware?  
4. If I/O — don’t use parallel streams; redesign concurrency.

### Related

[stateful-operations.md](./stateful-operations.md) · [side-effects.md](./side-effects.md) · [reduce.md](./reduce.md) · [stream-performance.md](./stream-performance.md)

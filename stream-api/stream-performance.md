# Stream Performance

Practical costs — not micro-myths.

## What Matters

| Factor | Impact |
|--------|--------|
| Boxing | `Stream<Long>` vs `LongStream` |
| Allocations | DTOs, pipeline, collectors |
| Stateful ops | sorted/distinct memory |
| Parallel overhead | Split/merge, FJP contention |
| Source | `Files.lines`, DB cursor — I/O bound |
| Short-circuit | Saves work |

## Why Streams Aren’t “Slow by Default”

For multi-stage transforms, clarity often wins and JIT handles simple map/filter well. Problems appear with boxing, parallel misuse, or huge in-memory sorts.

## Production Example — analytics

```java
// Weaker — boxes
long sum = orders.stream().map(Order::totalCents).reduce(0L, Long::sum);

// Stronger
long sum = orders.stream().mapToLong(Order::totalCents).sum();
```

## Guidance

1. Filter early.  
2. Prefer primitive streams for numeric aggregates.  
3. Push heavy filter/sort to DB when data is large.  
4. Parallel only with evidence.  
5. Don’t stream where a loop + index is the hot path (profile first).

## Common Mistake

Optimizing stream style before measuring; parallelizing I/O pipelines.

### Related

[primitive-streams.md](./primitive-streams.md) · [parallel-streams.md](./parallel-streams.md) · [stream-vs-loop.md](./stream-vs-loop.md)

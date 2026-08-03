# Side Effects in Streams

Observable effects beyond producing the stream result — mutation, I/O, logging.

## What Happens

Intermediate ops may run late, skip (short-circuit), or run in parallel threads. Side effects become **non-deterministic** in timing/order/duplication (esp. parallel).

## Why Avoid in Intermediates

Pipelines should express **what** values to compute. Effects belong in explicit terminals or outside the stream.

## Production Example

```java
// Bad — effect in map
orders.stream()
        .map(o -> {
            audit.record(o); // may not run for all if short-circuit later; order unclear if parallel
            return dto(o);
        })
        .toList();

// Good — pure pipeline, then effect
List<OrderDto> dtos = orders.stream().map(this::dto).toList();
dtos.forEach(audit::record);

// forEach is explicitly effectful — OK when intentional
paid.forEach(ledger::post);
```

`peek` — debug only, not production business logic.

## Performance Implications

Effects in parallel → lock contention / races. Logging per element in hot streams → I/O bottleneck.

## Common Mistake

Using `forEach` to build collections; relying on `peek` order; parallel `forEach` for ordered writes.

### Related

[parallel-streams.md](./parallel-streams.md) · [lazy-evaluation.md](./lazy-evaluation.md) · [stream-vs-loop.md](./stream-vs-loop.md)

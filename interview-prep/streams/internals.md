# Streams — Internals

Focus: **lazy pipeline + Spliterator + parallel**.

```text
source Spliterator
   → ReferencePipeline stages (stateless / stateful)
   → terminal evaluates Sink chain (pull)
```

## Must-explain pieces

| Piece | Point |
|-------|-------|
| Lazy | Intermediate ops wrap upstream; no source visit yet |
| Stateless ops | `map`/`filter` — per-element, easy parallel |
| Stateful ops | `sorted`/`distinct`/`limit` — may buffer; barriers in parallel |
| Short-circuit | Terminal can cancel traversal |
| `Collector` | `supplier`, `accumulator`, `combiner`, `finisher` (+ characteristics) |
| Parallel | Fork/Join common pool (default); splits Spliterator |
| Encounter order | Ordered sources preserve order unless `unordered()` |

## Parallel pitfalls

```text
parallel() → split → partial results → combine
```

| Pitfall | Why it hurts |
|---------|----------------|
| Shared mutable state in lambdas | Races |
| Non-associative `reduce` | Wrong answers |
| Tiny lists | Fork/join overhead ≫ work |
| Blocking IO in parallel stream | Saturates FJP; stalls unrelated tasks |
| `findFirst` vs `findAny` | Order constraint costs in parallel |

## `collect` internals sketch

```text
supplier.get() → accumulate each element → (parallel) combine partials → finisher
CONCURRENT / UNORDERED characteristics change strategy
```

Whiteboard until you can explain why `sorted().filter()` may do more work than `filter().sorted()`, and when parallel is safe.

Related: [parallel-streams.md](../../stream-api/parallel-streams.md), [stateful-operations.md](../../stream-api/stateful-operations.md), [collect.md](../../stream-api/collect.md).

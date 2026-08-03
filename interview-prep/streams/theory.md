# Streams — Theory

## Mental model

A **stream** is a pipeline over a data source — not a storage structure.

```text
source → intermediate* → terminal
         (lazy)           (triggers work)
```

## Contracts

| Kind | Examples | Runs now? |
|------|----------|-----------|
| Intermediate | `filter`, `map`, `flatMap`, `sorted`, `distinct` | No (lazy) |
| Terminal | `collect`, `reduce`, `forEach`, `findFirst`, `count` | Yes |
| Short-circuit | `findFirst`, `anyMatch`, `limit` | May skip remaining |

## Laziness

Intermediate ops build a recipe; **no traversal** until a terminal op. Enables fusion and short-circuit (`findFirst` stops early).

## Collect vs reduce

| | `reduce` | `collect` |
|-|----------|-----------|
| Idea | Fold to one immutable-ish result | Mutable container + finisher (`Collector`) |
| Typical | sum, max identity | `toList`, `groupingBy`, `joining` |

## When **not** to use streams

- Simple indexed loops clearer / faster for tiny hot paths  
- Need checked exceptions bubbling naturally  
- Heavy mutable in-place updates  
- Parallel “just because” on small/IO-bound work  
- Relying on side effects / encounter order under `parallel()`

Related chapter: [lazy-evaluation.md](../../stream-api/lazy-evaluation.md), [stream-vs-collection.md](../../stream-api/stream-vs-collection.md).

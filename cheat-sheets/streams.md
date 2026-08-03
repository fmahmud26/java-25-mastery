# Streams — Cheat Sheet

**Sources:** [../stream-api/README.md](../stream-api/README.md) · [lazy-evaluation](../stream-api/lazy-evaluation.md) · [parallel-streams](../stream-api/parallel-streams.md) · [side-effects](../stream-api/side-effects.md) · [java-interview-questions/streams](../java-interview-questions/streams/) · [experiments/stream-lazy-shortcircuit](../experiments/stream-lazy-shortcircuit/) · [experiments/parallel-stream-when-slower](../experiments/parallel-stream-when-slower/)

## Pipeline

```text
source → intermediate* (lazy) → terminal (triggers work)
```

## Intermediate vs terminal (recall)

| Intermediate | Terminal |
|--------------|----------|
| map, filter, flatMap, sorted, distinct, limit, peek | collect, reduce, forEach, findFirst/Any, anyMatch, count |

Short-circuit terminals stop early — [stream-lazy experiment](../experiments/stream-lazy-shortcircuit/).

## Collectors (common)

`toList` / `toSet` / `toMap` · `groupingBy` + downstream · `partitioningBy` · `joining` · `averaging*`  

Details: [stream-api collectors docs](../stream-api/README.md) study path.

## Decision rules (from chapter + bank)

| Situation | Prefer |
|-----------|--------|
| Pure transform / report | Stream pipeline |
| Blocking I/O per element | Loop / VT / CF — **not** `parallelStream` |
| Tiny CPU work | Sequential (parallel overhead) |
| Large CPU-bound associative | Consider parallel — **measure** |
| Side effects / txn | Keep out of `map` |

## Interview triggers

| Prompt | Point to |
|--------|----------|
| Lazy? | Intermediate deferred until terminal |
| findFirst vs findAny | Order vs parallel flexibility |
| Parallel + HTTP | Starves common ForkJoinPool |
| JDBC 5M rows | Cursor/fetch — don’t materialize list |

Bank: [streams Qs](../java-interview-questions/streams/)

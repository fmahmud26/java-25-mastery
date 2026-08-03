# When Virtual Threads Do NOT Solve the Problem

Explicit non-goals — Principal interview gold.

## Cases

| Problem | Why VT fails to fix |
|---------|---------------------|
| Slow SQL | Same queries; need indexes/plan |
| Pool exhaustion | Need pool/query/pod math |
| CPU-bound handler | Needs cores / algorithms |
| Rate-limited vendor | Need backoff/cache/queue |
| Lock held across I/O | Redesign locking |
| Bad algorithm O(n²) | Complexity |
| GC thrash from alloc | Allocation patterns |
| Distributed consistency | Idempotency/DB — not threads |
| Already reactive & fine | Rewrite cost may not pay |

## Scenario — “We switched to VT, p99 unchanged”

DB was saturated at 40 connections before and after. VT only reduced OS thread count. Win operational; not latency.

## Scenario — “VT made us worse”

Removed platform pool cap; stampeded PSP; vendor throttled; cascading timeouts. Fix: reintroduce **admission control**.

## Decision Test

```text
Was the bottleneck “ran out of platform threads while dependency had spare capacity”?
  YES → VT likely helps throughput
  NO  → fix the real bottleneck first
```

## Interview / PE

Give two stories where you rejected VT as the fix. What did you do instead?

### Related

[scenarios.md](./scenarios.md) · [experiments.md](./experiments.md) · [principal-architecture-decisions.md](./principal-architecture-decisions.md)

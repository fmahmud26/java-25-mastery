# Streams — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Request DTO mapping | sequential stream / loop | Clarity; tiny N |
| Report grouping | `groupingBy` | Readable aggregation |
| CSV column parse | stream of lines | Lazy I/O + `limit` |
| CPU-heavy map over large in-memory data | measure `parallel()` | May win if pure/associative |
| DB / HTTP per element | **avoid** parallel stream | Blocking + FJP pollution |
| Shared service counters in lambda | don’t | Races; use proper concurrency |

## Production rules of thumb

- Default **sequential**; parallel is an optimization with constraints.
- Keep lambdas **pure** — no mutating shared collections.
- Prefer `toList()` / immutable collectors at API boundaries.
- For IO concurrency use virtual threads / structured concurrency — not `parallelStream()`.

Related: [side-effects.md](../../stream-api/side-effects.md), [parallel-streams.md](../../stream-api/parallel-streams.md), [../../modern-java-engineering](../../modern-java-engineering/).

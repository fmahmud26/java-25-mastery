# Collections — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Request-scoped lookup | `HashMap` | Single-threaded request |
| Shared cache map | `ConcurrentHashMap` or external Redis | Correctness under concurrency |
| Config keyed by enum | `EnumMap` | Compact, fast |
| Preserve insert order JSON-ish | `LinkedHashMap` | Stable iteration |
| Sorted admin views | `TreeMap` | Range queries |
| Hot path counts | CHM `AtomicLong` values / LongAdder | Less lock churn |
| API return | `List.copyOf` / unmodifiable | Don’t leak mutability |

## Production rules of thumb

- Never expose raw mutable maps from a singleton service without a concurrency story.
- Size hints when you know cardinality (`HashMap.newHashMap(n)`).
- Prefer immutable snapshots across threads over synchronizing a living `HashMap`.

Related: [../../modern-java-engineering/immutability.md](../../modern-java-engineering/immutability.md).

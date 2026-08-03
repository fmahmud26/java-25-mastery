# Terminal Operations

Eager ops that **trigger** the pipeline and produce a result or side effect. Afterward the stream is consumed.

## What Happens

| Category | Examples |
|----------|----------|
| Reduce | `reduce`, `count`, `min`, `max`, sum (primitives) |
| Collect | `collect`, `toList` |
| Match | `anyMatch`, `allMatch`, `noneMatch` |
| Find | `findFirst`, `findAny` |
| Iterate | `forEach`, `forEachOrdered` |

Short-circuit: match/find/limit can stop early.

## Why Useful

Define *what you want out* — list, map, boolean, optional element — in one place.

## Production Example — analytics

```java
boolean anyFraud = txs.stream().anyMatch(Tx::flagged);

Optional<Tx> firstLarge = txs.stream()
        .filter(t -> t.cents() > 1_000_00)
        .findFirst();

Map<String, Long> volumeByMerchant = txs.stream()
        .collect(Collectors.groupingBy(Tx::merchantId, Collectors.counting()));
```

## Performance Implications

Choice of terminal matters: `collect(groupingBy)` vs multiple passes. `forEach` parallel order undefined. Prefer specialized terminals (`sum`) over manual `reduce` when available.

## Common Mistake

Two terminals on one stream; using `forEach` to build a list instead of `toList`/`collect`.

### Related

[lazy-evaluation.md](./lazy-evaluation.md) · [collect.md](./collect.md) · [reduce.md](./reduce.md)

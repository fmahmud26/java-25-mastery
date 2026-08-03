# Modern Collection APIs

**Java 9 factories → 10+ tweaks → 21 sequenced collections** — immutable literals and explicit encounter order.

## Problem Before

```java
List<String> tags = new ArrayList<>();
tags.add("paid");
tags.add("reconcile");
tags = Collections.unmodifiableList(tags);

Map<String, Integer> m = new HashMap<>();
m.put("a", 1);
m.put("b", 2);
```

Verbose, mutable by default, first/last on `LinkedHashSet` awkward.

## The Feature

| API | Since | Notes |
|-----|-------|-------|
| `List.of` / `Set.of` / `Map.of` / `Map.ofEntries` | 9 | Immutable, no nulls |
| `List.copyOf` / `Set.copyOf` / `Map.copyOf` | 10 | Defensive immutable copy |
| `SequencedCollection`, `SequencedSet`, `SequencedMap` | 21 | `getFirst`/`getLast`/`reversed` |
| `LinkedHashSet`/`LinkedHashMap` as sequenced | 21 | Clearer ordered ops |

## How It Works

Factory collections are value-based unmodifiable; mutation → `UnsupportedOperationException`. `Set.of` rejects duplicates. Sequenced types define encounter order operations uniformly.

## Before → After

```java
// After
var tags = List.of("paid", "reconcile");
var fees = Map.of("EU", 20, "US", 30);
var copy = List.copyOf(mutableLines); // snapshot for publication

SequencedMap<String, Long> latestBySku = new LinkedHashMap<>();
latestBySku.putFirst("SKU-1", 10L);
var lastSku = latestBySku.lastEntry();
```

```java
// Stream still fine — but factories beat manual unmodifiable wrappers
return items.stream().map(OrderLine::sku).toList(); // 16+ unmodifiable list
```

## Production Usage

- Return `List.copyOf` from aggregates  
- Config maps via `Map.of` when small and fixed  
- Sequenced maps for “recent first” caches / LRU-ish structures (still need eviction policy)

## Trade-offs

| Pros | Cons |
|------|------|
| Safe by default | No null elements in `*.of` |
| Clear intent | Immutable → need new collection to “change” |
| Sequenced clarity | Requires 21+ |

## When NOT to Use

- Need nulls → different structure or explicit Optional values  
- Need high churn mutation → `ArrayList` then `copyOf` at boundary  
- Huge maps — prefer builders/`HashMap` then copy

## Migration Notes

Replace `Collections.unmodifiableList(Arrays.asList(...))` with `List.of`/`copyOf`. Adopt sequenced APIs when on 21+.

## Interview Questions

- Why `List.of` disallows null?  
- `List.of` vs `List.copyOf`?  
- What problem do sequenced collections solve?  
- Is `toList()` mutable?

### Related

[modern-apis.md](./modern-apis.md) · [optional.md](./optional.md) · [modern-coding-style.md](./modern-coding-style.md)

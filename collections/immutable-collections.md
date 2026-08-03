# Immutable Collections

`List.of` / `Set.of` / `Map.of` / `copyOf` (Java 9+) — unmodifiable, no nulls (for `of`).

## Why

Safe publication of catalogs/config; clear “won’t mutate” API; fewer defensive bugs.

## Pattern — product catalog

```java
Map<String, Product> catalog = Map.copyOf(loadingMap);
// publish volatile / atomic reference to catalog
```

## vs COW / CHM

Immutable snapshot publish often beats live concurrent mutation for read-heavy catalogs.

## Failure

Calling `add` → UOE. Null → NPE on `of`.

## Interview

- `List.of` vs `Collections.unmodifiableList`?  
- `copyOf` on already immutable?  
- **Principal:** atomic catalog refresh design?

### Related

[decision-matrix.md](./decision-matrix.md) · [map.md](./map.md) · [list.md](./list.md)

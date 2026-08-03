# Stateful Operations

Ops that must remember prior elements — may buffer or block until enough data is seen.

## What Happens

| Op | State needed |
|----|----------------|
| `distinct` | Seen set |
| `sorted` | Buffer all (typically) |
| `limit` / `skip` | Counts (harder under parallel + ordered) |

Stateless: `map`, `filter`, `flatMap` (normally).

## Why It Matters

Stateful ops cost memory/latency and reduce parallel efficiency. Place filters **before** `sorted` when possible.

## Production Example

```java
// Expensive: sort all then take 10
orders.stream()
        .sorted(Comparator.comparing(Order::totalCents).reversed())
        .limit(10)
        .toList();

// Often better for top-N: PriorityQueue / partial select / DB ORDER BY LIMIT
```

```java
List<String> uniqueSkus = lines.stream()
        .map(OrderLine::sku)
        .distinct()
        .toList(); // hashes all skus
```

## Performance Implications

`sorted` ≈ O(n log n) + O(n) memory. `distinct` ≈ O(n) memory. Parallel + ordered `limit` can be surprisingly expensive.

## Common Mistake

`distinct` on objects with broken `equals`/`hashCode`; sorting huge streams in-app instead of pushdown to DB.

### Related

[intermediate-operations.md](./intermediate-operations.md) · [sorted.md](./sorted.md) · [distinct.md](./distinct.md) · [parallel-streams.md](./parallel-streams.md)

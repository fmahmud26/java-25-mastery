# Primitive Streams

`IntStream`, `LongStream`, `DoubleStream` — avoid boxing for numeric analytics.

## What Happens

```text
Stream<Order> ──mapToLong(Order::totalCents)──► LongStream ──sum/avg/summaryStatistics──► primitive result
```

Specialize: `filter`/`map`/`reduce` on primitives; `boxed()` back to `Stream<Integer>` etc.; `mapToObj` to objects.

## Why Useful

Revenue sums, counts, histograms, ranges (`IntStream.range`), performance-sensitive analytics over transactions/orders.

## Production Example

```java
long gmv = orders.stream()
        .filter(Order::paid)
        .mapToLong(Order::totalCents)
        .sum();

DoubleSummaryStatistics stats = txs.stream()
        .mapToDouble(t -> t.cents() / 100.0)
        .summaryStatistics();

IntStream.rangeClosed(1, 12)
        .mapToObj(month -> reportService.monthSummary(year, month))
        .toList();
```

## Performance Implications

Eliminates per-element Integer/Long allocations — matters in tight aggregates over large n. Still not free: pipeline objects remain. Don’t box/unbox repeatedly (`mapToLong` then `boxed` then `mapToLong`).

## Common Mistake

`map(Order::totalCents)` yielding `Stream<Long>` then `reduce(0L, Long::sum)` instead of `mapToLong().sum()`; using `DoubleStream` for money (prefer long cents).

### Related

[map.md](./map.md) · [reduce.md](./reduce.md) · [stream-performance.md](./stream-performance.md)

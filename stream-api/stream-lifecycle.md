# Stream Lifecycle

A stream is a **one-shot pipeline**: create → (optionally) transform lazily → consume once with a terminal operation → done.

## What Happens

```text
1. Create   stream() / of / iterate / Files.lines …
2. Build    zero or more intermediate ops (no traversal yet)
3. Run      terminal op pulls elements through the pipeline
4. Exhaust  stream cannot be reused — IllegalStateException if reused
```

```java
Stream<Order> s = orders.stream().filter(Order::paid);
List<Order> paid = s.toList();     // runs pipeline
// s.count();                      // IllegalStateException — already consumed
```

## Why It Matters

Understanding lifecycle prevents “why didn’t filter run?” (no terminal), reuse bugs, and holding open resources (`Files.lines` must be closed — use try-with-resources).

## Production Example

```java
public List<OrderSummary> paidSummaries(List<Order> orders) {
    return orders.stream()              // create
            .filter(Order::paid)        // intermediate
            .map(OrderSummary::from)    // intermediate
            .toList();                  // terminal — lifecycle ends
}
```

```java
try (Stream<String> lines = Files.lines(path)) {
    return lines.filter(l -> l.contains("ERROR")).count();
} // closes file
```

## Performance Implications

Creation is cheap; cost is in terminal traversal + intermediates actually executed. Short-circuit terminals stop early.

## Common Mistake

Storing a stream in a field and reusing it across requests — streams are not collections.

### Related

[lazy-evaluation.md](./lazy-evaluation.md) · [stream-creation.md](./stream-creation.md) · [terminal-operations.md](./terminal-operations.md)

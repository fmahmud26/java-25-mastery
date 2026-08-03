# Stream vs Loop

When to reach for streams vs classic `for`.

## What Happens

| Style | Control | Expression |
|-------|---------|------------|
| Loop | Explicit index/iterator, break/continue, mutable acc | Imperative |
| Stream | Declarative stages, lazy, one-pass | Functional pipeline |

## Why Streams Are Useful

- Compose filter/map/group without temporary lists  
- Parallel option (when appropriate)  
- Readable data transforms for reporting/analytics  

## Why Loops Are Useful

- Early exit with complex conditions  
- Checked exceptions / transactions per element  
- Debuggability and fewer allocations  
- Blocking I/O per item without abusing FJP  

## Production Example — orders

```java
// Loop — fine for simple accumulate
long total = 0;
for (Order o : orders) {
    if (o.paid()) total += o.totalCents();
}

// Stream — clear for multi-stage reporting
long total = orders.stream()
        .filter(Order::paid)
        .mapToLong(Order::totalCents)
        .sum();

Map<String, Long> byCustomer = orders.stream()
        .filter(Order::paid)
        .collect(Collectors.groupingBy(
                o -> o.customerId().value(),
                Collectors.summingLong(Order::totalCents)));
```

## Performance Implications

Streams allocate pipeline objects and often boxes unless using primitive streams. For tiny lists, loops often win. For multi-step reporting, streams often win on **clarity** with comparable speed.

## Common Mistake

Rewriting every loop to streams for “modern” style — including those with nested mutation and I/O — then fighting readability and performance.

## Decision

| Prefer stream when | Prefer loop when |
|--------------------|------------------|
| Transform/filter/group/report | Stateful multi-step algorithms |
| Pure element ops | Need break with side logic |
| Collector fits the result | Per-item try/catch transaction |

### Related

[stream-vs-collection.md](./stream-vs-collection.md) · [side-effects.md](./side-effects.md) · [stream-performance.md](./stream-performance.md)

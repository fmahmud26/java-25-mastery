# Intermediate Operations

Lazy, typically return a new `Stream`. May be **stateless** (map/filter) or **stateful** (sorted/distinct/limit).

## What Happens

They build the pipeline graph. Stateless ops can process element-at-a-time; stateful may need to see many/all elements first.

| Op | Role | Stateful? |
|----|------|-----------|
| map / filter / flatMap / peek | Transform/select | Usually no |
| distinct / sorted | Dedupe / order | Yes |
| limit / skip | Slice | Yes (esp. ordered) |

## Why Useful

Express multi-step transforms without allocating every intermediate collection manually.

## Production Example — customers

```java
Stream<CustomerDto> dtos = customers.stream()
        .filter(Customer::active)
        .filter(c -> c.region().equals("EU"))
        .map(CustomerDto::from)
        .sorted(Comparator.comparing(CustomerDto::name));
// still lazy until terminal
```

## Performance Implications

Long chains of pure stateless ops are fine. `sorted` on huge streams = O(n log n) + memory. `distinct` hashes elements — cost + memory.

## Common Mistake

`peek` for business logic; relying on encounter order after `unordered()` / parallel without care.

### Related

[terminal-operations.md](./terminal-operations.md) · [stateful-operations.md](./stateful-operations.md) · [map.md](./map.md) · [filter.md](./filter.md)

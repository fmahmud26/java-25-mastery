# Optional and Streams

How `Optional` interacts with stream pipelines (Java 9+ `Optional.stream()`, flatMapping optionals).

## What Happens

```text
Optional<T> ──stream()──► Stream<T>   (0 or 1 element)
Stream<Optional<T>> ──flatMap(Optional::stream)──► Stream<T>
```

Terminal finds return `Optional`. Don’t use Optional as a stream element type in APIs chronically — flatten early.

## Why Useful

Lookup→stream pipelines; drop missing values cleanly; bridge Optional-based repos to collectors.

## Production Example

```java
Optional<Customer> cust = customerRepo.find(id);

List<Order> orders = cust.stream()
        .flatMap(c -> c.orders().stream())
        .filter(Order::paid)
        .toList();

List<String> emails = ids.stream()
        .map(customerRepo::find)
        .flatMap(Optional::stream)
        .map(Customer::email)
        .toList();

Optional<Order> firstPaid = orders.stream()
        .filter(Order::paid)
        .findFirst();
```

## Performance Implications

`Optional.stream()` is cheap for 0/1. Mapping every id to Optional then flatMapping is fine for moderate N; for huge N, batch repos beat N optional lookups.

## Common Mistake

`optional.get()` without check after `findFirst`; wrapping everything in Optional inside streams; `orElse(null)` reintroducing nulls into pipelines.

### Related

[flatmap.md](./flatmap.md) · [find-first.md](./find-first.md)

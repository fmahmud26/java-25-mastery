# findFirst

First element in encounter order as `Optional` — short-circuit.

```java
Optional<Order> firstPaid = orders.stream().filter(Order::paid).findFirst();
```

Prefer `findAny` under parallel when order irrelevant. See [any-match.md](./any-match.md).

### Related

[find-any.md](./find-any.md) · [optional-and-streams.md](./optional-and-streams.md)

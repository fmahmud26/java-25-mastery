# peek

Intermediate for **debugging** side effects — not for production business logic.

```java
.filter(Order::paid)
.peek(o -> log.debug("paid {}", o.id()))  // debug only
.map(OrderDto::from)
```

Under parallel/short-circuit, invocations are unreliable for metrics. Prefer explicit steps.

### Related

[side-effects.md](./side-effects.md)

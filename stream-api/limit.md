# limit

Stateful short-circuiting slice — at most `n` elements.

```java
List<Order> top = orders.stream()
        .sorted(Comparator.comparingLong(Order::totalCents).reversed())
        .limit(10)
        .toList();
```

**Perf:** With `sorted`, still sorts all unless redesign. Parallel + ordered `limit` reduces speedup.  
**Mistake:** In-app pagination over millions — push to DB.

### Related

[skip.md](./skip.md) · [stateful-operations.md](./stateful-operations.md)

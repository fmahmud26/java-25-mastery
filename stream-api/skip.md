# skip

Drop first `n` elements — stateful.

```java
orders.stream().sorted(...).skip(page * size).limit(size).toList();
```

Prefer DB `OFFSET/LIMIT` (or keyset pagination) for large datasets — skipping in-memory still costs upstream work.

### Related

[limit.md](./limit.md)

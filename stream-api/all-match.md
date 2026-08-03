# allMatch

True if every element matches (vacuous true on empty). Short-circuits on first failure.

```java
boolean allSettled = txs.stream().allMatch(Tx::settled);
```

### Related

[any-match.md](./any-match.md) · [none-match.md](./none-match.md)

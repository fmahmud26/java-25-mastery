# noneMatch

True if no element matches. Short-circuits on first match.

```java
boolean noFraud = txs.stream().noneMatch(Tx::flagged);
```

### Related

[any-match.md](./any-match.md)

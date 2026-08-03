# min

```java
Optional<Tx> smallest = txs.stream().min(Comparator.comparingLong(Tx::cents));
```

See [count.md](./count.md). Empty stream → empty Optional.

### Related

[max.md](./max.md) · [reduce.md](./reduce.md)

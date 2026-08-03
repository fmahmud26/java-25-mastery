# findAny

Any matching element as `Optional` — nondeterministic under parallel; often faster than `findFirst` then.

```java
Optional<Tx> sample = txs.parallelStream().filter(Tx::flagged).findAny();
```

### Related

[find-first.md](./find-first.md) · [parallel-streams.md](./parallel-streams.md)

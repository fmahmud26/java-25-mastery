# summarizing\* collectors

`summarizingInt/Long/Double` → count/sum/min/max/average in one pass.

```java
LongSummaryStatistics s = txs.stream()
        .collect(Collectors.summarizingLong(Tx::cents));
```

Prefer for analytics dashboards; money still as **long cents**.

### Related

[counting.md](./counting.md) · [primitive-streams.md](./primitive-streams.md)

# Scalability with Virtual Threads

## Mental Model

```text
Scale axis VT improves: concurrent blocked operations
Scale axes VT does NOT improve: CPU cores, DB capacity, API rate limits
```

## What Scales

| Metric | Effect |
|--------|--------|
| Concurrent blocking requests | ↑↑ |
| Threads waiting on I/O | Cheap |
| Code simplicity vs reactive | ↑ |

## What Hits a Wall

| Limit | Symptom |
|-------|---------|
| Connection pools | Wait for Hikari |
| Downstream RPS limits | 429 / latency |
| CPU | Carrier saturation |
| Memory / stack chunks | GC / native pressure |
| GC / allocation | Object churn per request |

## Code — measure the real ceiling

```java
// Admission control at the scarce resource
hikari.setMaximumPoolSize(40);
hikari.setConnectionTimeout(1_000);

httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(2))
        .build();
```

## Production Scenario — 10K concurrent HTTP

VT server accepts 10K connections; only 40 DB queries run; 9960 wait on pool → p99 huge. Scalability of *threads* succeeded; scalability of *system* failed.

## Graph (conceptual)

```text
throughput
   ^
   |         ___________  dependency/pool ceiling
   |       /
   |     /   VT removed thread ceiling
   |   /
   +----------------──► concurrency
```

## Interview / PE

Define which ceiling you moved. How do you prove VT helped?

### Related

[memory.md](./memory.md) · [downstream-limitations.md](./downstream-limitations.md) · [experiments.md](./experiments.md)

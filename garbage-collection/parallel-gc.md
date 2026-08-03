# Parallel GC

Multi-threaded **throughput**-oriented collector. Parallel STW young/old collections.

```bash
java -XX:+UseParallelGC ...
```

## Mental Model

```text
Maximize work done per unit wall time for batch / CPU-heavy jobs
Accept longer STW pauses than low-latency collectors
```

## Production Implications

| Prefer | Batch analytics, offline jobs where pause is OK |
|--------|--------------------------------------------------|
| Avoid as default | User-facing tight p99 latency without measurement |

## Trade-offs

Often strong throughput on multi-core; weaker latency profile than G1/ZGC/Shenandoah for interactive services. Measure if tempted for a server SLA.

### Related

[serial-gc.md](./serial-gc.md) · [g1-gc.md](./g1-gc.md) · [trade-offs.md](./trade-offs.md)

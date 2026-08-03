# Garbage Collection — Real-World Usage

| Scenario | Choice | Why |
|----------|--------|-----|
| Typical Spring/HTTP service | G1 | Balanced, well-understood |
| Trading / p99-critical / multi-GB+ | ZGC (+ generational) | Pause predictability |
| Batch ETL overnight | Parallel or G1 | Throughput over latency |
| Sidecar / tiny heap | Serial | Less GC threading overhead |
| Kubernetes | Heap ≪ cgroup limit | Leave room for metaspace/native/threads |

## Production rules of thumb

- Alert on **Full GC frequency**, old occupancy after GC, allocation rate — not only heap %.
- Never “fix” prod with random `-XX:` folklore; change one knob, load-test.
- OOME → heap dump; rising old gen with flat traffic → **leak** until proven otherwise.
- Prefer reducing object churn (records, reuse buffers carefully) over heroic GC flags.

Related: [../../modern-java-engineering](../../modern-java-engineering/), [g1-gc.md](../../garbage-collection/g1-gc.md), [zgc.md](../../garbage-collection/zgc.md).

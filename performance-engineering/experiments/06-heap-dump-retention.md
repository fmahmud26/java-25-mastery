# Experiment 06 — Heap Dump Retention

## Question

Can you prove an unbounded `static` map retains memory via path-to-GC-root?

## Workload

```java
static final Map<String, byte[]> CACHE = new HashMap<>();
// each request: CACHE.put(UUID.randomUUID().toString(), new byte[10_000]);
```

Run until heap after GC climbs.

## Measure

```bash
jcmd <pid> GC.heap_info
jcmd <pid> GC.heap_dump /tmp/leak.hprof
```

## Hypothesize

Dominator: `CACHE` → `byte[]`.

## Analyze

MAT path to root → `static CACHE`.

## Optimize

Bound with Caffeine max size → re-run same request count → dump → retained size capped.

## Re-measure

After-GC occupancy plateaus; young GC still occurs from churn.

### Related

[../heap-dumps.md](../heap-dumps.md) · [../memory-profiling.md](../memory-profiling.md)

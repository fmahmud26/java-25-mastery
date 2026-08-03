# Memory — Cheat Sheet

**Sources:** [../memory-management/README.md](../memory-management/README.md) · [references](../memory-management/references.md) · [memory-leaks](../memory-management/memory-leaks.md) · [java-interview-questions/memory](../java-interview-questions/memory/) · [experiments/allocation-rate-pressure](../experiments/allocation-rate-pressure/)

## PE rule (chapter)

**A Java “leak” ≈ unintended strong reachability — prove with metrics → dump path-to-root → bounded fix.**

## Areas

| Area | Cheat link / depth |
|------|---------------------|
| Stack / heap / metaspace | [memory-management study path](../memory-management/README.md) |
| Object headers | Chapter + Java 25 compact headers (519) — product; check defaults/flags in [16-java-25-features](../16-java-25-features/features/jep-519-compact-object-headers.md) |
| Direct/native | [bank q03](../java-interview-questions/memory/q03-direct-buffers.md) |

## References (policy)

| Type | Typical use | Pitfall |
|------|-------------|---------|
| Strong | Normal refs | Accidental retention |
| Soft | Memory-sensitive caches | Unpredictable clear → stampede |
| Weak | Canonical maps | Entry can vanish |
| Phantom | Post-mortem cleanup | Need ReferenceQueue discipline |

Depth: [references.md](../memory-management/references.md) · [gc soft/weak bank](../java-interview-questions/gc/q05-reference-queues.md)

## Leak / pressure cheats

| Smell | Action |
|-------|--------|
| ThreadLocal not removed | `remove()` in finally — [q02](../java-interview-questions/memory/q02-threadlocal-leak.md) |
| Unbounded cache | Size + TTL — [q05](../java-interview-questions/memory/q05-cache-without-bounds.md) |
| Rising retained heap | Dump → GC roots — [q01](../java-interview-questions/memory/q01-heap-histogram-incident.md) |
| High alloc, flat live set | Cut allocation — [experiment](../experiments/allocation-rate-pressure/) · [GC sheet](./gc.md) |

## Investigation loop

Chapter: [investigation](../memory-management/investigation.md) · [incidents](../memory-management/incidents.md)

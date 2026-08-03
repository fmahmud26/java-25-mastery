# Platform Threads (in a VT world)

OS-backed threads — still the **carriers** and still the right tool for **CPU-bound** bounded parallelism.

## Mental Model

```text
1 platform thread ≈ 1 OS thread ≈ expensive stack + scheduling entity
Pool and reuse for CPU work; do not create millions
```

## Role After Loom

| Role | Use platform threads |
|------|----------------------|
| Carriers | JVM runs VTs on them |
| CPU pools | Image resize, crypto, scoring |
| Native/TLS-heavy libs | When pinning/affinity required |
| Limited parallelism | `newFixedThreadPool(cores)` |

## Code

```java
try (var cpu = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
    return cpu.submit(() -> heavyScore(order)).get();
}
```

## Production Scenario

High-concurrency REST uses VTs for request I/O, but offloads CPU PDF generation to a **bounded platform pool** so carriers aren’t stolen by compute.

## Failure Scenario

Running CPU-bound work on millions of VTs → carrier pool saturated → whole server latency collapses (VT doesn’t add cores).

## Trade-offs

Platform = scarce & predictable parallelism. VT = abundant waiting. Mixing without bulkheads → interference.

## Interview / PE

Why keep platform pools after adopting VT? Why is the VT carrier scheduler **not** `ForkJoinPool.commonPool()`?

### Related

[carrier-threads.md](./carrier-threads.md) · [virtual-threads.md](./virtual-threads.md) · [when-vt-do-not-help.md](./when-vt-do-not-help.md)

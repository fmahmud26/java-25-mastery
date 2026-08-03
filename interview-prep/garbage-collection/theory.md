# Garbage Collection — Theory

## Why GC exists

Manual `free` → use-after-free / leaks. GC reclaims unreachable objects from GC roots (stacks, statics, JNI refs, etc.).

## Generational hypothesis

Most objects die young → split heap:

| Generation | Role |
|------------|------|
| **Young** (Eden + Survivor) | Frequent, cheap collections |
| **Old** | Long-lived; collected less often |
| **Humongous** (G1) | Large objects spanning regions |

## Mark → sweep → compact (sketch)

```text
Mark live from roots → reclaim dead → (optional) compact to fight fragmentation
```

Collectors differ on **which parts are STW vs concurrent** and how they compact.

## STW

**Stop-The-World**: mutator threads pause at a safepoint. Even “concurrent” collectors have short STW phases (root scan, remark, etc.). Interview distinction: pause **frequency/duration** and whether pause scales with heap/live set.

## Collector map (HotSpot)

| Collector | Pitch |
|-----------|-------|
| Serial | Single-thread GC; tiny heaps / containers |
| Parallel | Throughput; longer STW OK |
| **G1** | Region-based; pause-goal oriented; common default |
| **ZGC** | Ultra-low pause aim; concurrent compact; huge heaps; **generational-only on Java 25** (no `-XX:+ZGenerational`) |
| Shenandoah | Low-pause concurrent (OpenJDK builds) |

Related chapter: [why-gc-exists.md](../../garbage-collection/why-gc-exists.md), [generational-gc.md](../../garbage-collection/generational-gc.md).

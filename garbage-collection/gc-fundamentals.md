# GC Fundamentals

What garbage collection is doing, independent of which collector you pick.

## Mental Model

```text
Live  = reachable from GC roots
Dead  = unreachable
GC    = find live, reclaim dead, sometimes relocate/compact
```

Mutator threads run the application. GC threads (and STW phases) reclaim memory.

## Technical Mechanism — classic phases

| Phase | Purpose |
|-------|---------|
| **Mark** | From roots, mark reachable objects ([mark.md](./mark.md)) |
| **Sweep** | Reclaim unmarked space ([sweep.md](./sweep.md)) |
| **Compact / Evacuate** | Move live objects to reduce fragmentation ([compact.md](./compact.md)) |
| **Copy** | Young gens often *copy* live objects to another space |

Modern collectors interleave these **concurrently** with mutators (barriers, colored pointers, etc.) and still use short [stop-the-world](./stop-the-world.md) pauses for roots and coordination.

## JVM Internals (roots)

Typical GC roots: thread stacks / locals, static fields, JNI references, and other VM roots. If an object is reachable, GC **must not** reclaim it — that is correct behavior, not a “GC bug.” Unintended reachability is a [memory leak](../memory-management/memory-leaks.md).

## Production Implications

| You observe | First question |
|-------------|----------------|
| High GC CPU | Allocation rate? Live set? |
| Long pauses | Which pause phase? Collector? Heap size? |
| Frequent GC | Eden too small? Alloc storm? |
| Rising after-GC heap | Leak / retention, not “GC broken” |

## Trade-offs (universal)

```text
Throughput  ↔  Latency  ↔  Footprint  ↔  CPU overhead
```

No collector wins all four for every workload. See [trade-offs.md](./trade-offs.md).

## Interview / PE

Define live vs dead. Why concurrent collectors still have STW? Leak vs GC problem?

### Related

[why-gc-exists.md](./why-gc-exists.md) · [generational-gc.md](./generational-gc.md) · [stop-the-world.md](./stop-the-world.md)

# Heap

Shared store for **Java objects and arrays**. Reclaimed by the **Garbage Collector**.

## Mental Model

```text
new Foo()  →  allocate in heap  →  live while reachable from GC roots
GC roots include: thread stacks, static fields, JNI refs, …
```

```text
Heap (simplified generational view)
├── Young: Eden + Survivor (S0/S1)
└── Old / Tenured
(+ Humongous / large-object paths — e.g. G1)
```

## Technical Mechanism

```java
byte[] data = new byte[1024];    // array on heap
String s = new String("hi");     // String object on heap
```

| Topic | Detail |
|-------|--------|
| Shared | All threads |
| GC | Unreachable objects reclaimed |
| OOM | `OutOfMemoryError: Java heap space` |
| Tunables | `-Xms`, `-Xmx`, GC choice (`G1`, `ZGC`, `Shenandoah`, …) |

## JVM Internals

- Allocation: TLAB (thread-local allocation buffers) for fast bump-pointer alloc in young gen.  
- **Large / humongous** objects may skip Eden and stress old gen / regions — see incidents.  
- Object header + fields; Java 25 **compact object headers** (JEP 519) via `-XX:+UseCompactObjectHeaders` (product feature, **not default** yet).  
- Escape analysis may eliminate some allocations (scalar replacement) — not a language guarantee.

## Production Implications

- Size `-Xmx` from live set + allocation rate + headroom — not “machine RAM.”  
- Allocation rate drives GC frequency; “memory leak” vs “too small heap” need histograms.  
- Huge arrays (`new byte[100_000_000]`) → humongous fragmentation / promotion spikes.

## Incident — large object allocation / memory pressure

See [incidents.md](./incidents.md): rising old gen, humongous count, allocation profiles via JFR / async-profiler.

## Interview / PE

Where do objects live? Can JIT put them on the stack? (Only via EA/scalar replacement — not programmer-visible.) Heap vs metaspace? Young vs old purpose?

### Related

[stack.md](./stack.md) · [metaspace.md](./metaspace.md) · [escape-analysis.md](./escape-analysis.md) · [runtime-data-areas.md](./runtime-data-areas.md)

**Canonical memory/reachability depth:** [../memory-management/heap.md](../memory-management/heap.md) · GC sizing: [../garbage-collection/heap-sizing.md](../garbage-collection/heap-sizing.md)

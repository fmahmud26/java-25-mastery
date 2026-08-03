# Heap

Shared runtime area for **objects and arrays**, reclaimed by the **Garbage Collector**.

## Mental Model

```text
new Foo() → allocate on heap → live while strongly reachable from GC roots
GC reclaims when unreachable (modulo Soft/Weak/Phantom rules)
```

```text
Young generation                 Old generation
┌──────────┬────┬────┐          ┌────────────────┐
│  Eden    │ S0 │ S1 │  ──►     │    Tenured     │
└──────────┴────┴────┘          └────────────────┘
(+ large / humongous paths — GC-specific, e.g. G1)
```

## Technical Mechanism

| Topic | Detail |
|-------|--------|
| Shared | All threads |
| Allocation | Usually TLAB → Eden ([object-allocation.md](./object-allocation.md)) |
| Reclamation | GC |
| OOM | `OutOfMemoryError: Java heap space` |
| Tunables | `-Xms`, `-Xmx`, GC algorithm |

```bash
java -Xms512m -Xmx2g \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=./dump.hprof \
  -jar app.jar
```

## JVM Internals

- Generational hypothesis: most objects die young → cheap young collections.  
- Survivors promote to old; old collections are more expensive (collector-dependent).  
- Large objects may bypass Eden (humongous) → fragmentation / pause risk.  
- Headers add per-object cost — Java 25 compact headers optional ([object-headers.md](./object-headers.md)).  
- Escape analysis may eliminate some allocations — not a guarantee.

## Production Implications

- Size `-Xmx` from **live set + allocation rate + headroom**, not “all of RAM.”  
- Container limit must cover heap **plus** metaspace, stacks, code cache, direct/native.  
- Rising heap *after GC* ⇒ retention problem ([memory-leaks.md](./memory-leaks.md)).

## Investigation Hook

See [investigation.md](./investigation.md) and [incidents.md](./incidents.md): heap continuously increasing.

## Interview / PE

Young vs old? Why isn’t `-Xmx` equal to container memory? How do you distinguish leak vs undersized heap?

### Related

[metaspace.md](./metaspace.md) · [object-allocation.md](./object-allocation.md) · [outofmemoryerror.md](./outofmemoryerror.md)

**JVM runtime area sketch:** [../jvm-internals/heap.md](../jvm-internals/heap.md) (keep this file for leak/retention depth).

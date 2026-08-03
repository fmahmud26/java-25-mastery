# Metaspace

Native memory for **class metadata** — HotSpot’s method area. Replaced PermGen (removed Java 8).

## Mental Model

```text
Heap        → objects you new
Metaspace   → descriptions of classes/methods (Klass, Method*, constant pool, …)
```

Not “where static objects live” as a simplistic myth — **static field values that are references point to heap objects**; metadata describing the class lives in metaspace.

## Technical Mechanism

```text
Metaspace (native)
├── Class metadata (Klass)
├── Method metadata
├── Constant pool (runtime)
├── Annotations / related structures
└── Compressed class space (when used)
```

| Trait | Detail |
|-------|--------|
| Location | Native, not `-Xmx` |
| Bound | `-XX:MaxMetaspaceSize=` (else can grow until native OOM) |
| OOM | `OutOfMemoryError: Metaspace` |
| Unloading | When defining **class loader** becomes unreachable |

```bash
java -XX:MaxMetaspaceSize=256m -Xlog:gc+metaspace=info …
```

## JVM Internals

- Many loaders × many classes ⇒ metaspace pressure (app servers, Groovy/Jython, generated proxies).  
- Compressed class pointers use a bounded **compressed class space** region when enabled.  
- Class unloading is not “delete .class file” — GC must collect the loader.

## Production Implications

Track metaspace used vs committed in metrics. After redeploys, if metaspace never drops → loader leak.

## Incident — class-loading / metaspace

See [incidents.md](./incidents.md): Metaspace OOM after N redeploys; heap fine.

## Interview / PE

PermGen vs Metaspace? Why MaxMetaspaceSize? What must die for classes to unload? Is String pool in metaspace? (Historically permgen; modern string pool is heap.)

### Related

[class-loading.md](./class-loading.md) · [heap.md](./heap.md) · [runtime-data-areas.md](./runtime-data-areas.md)

**Canonical metaspace / loader-leak depth:** [../memory-management/metaspace.md](../memory-management/metaspace.md)

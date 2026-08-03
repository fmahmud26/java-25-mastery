# Metaspace

Native memory holding **class metadata** (HotSpot method area). Separate from the Java heap.

## Mental Model

```text
Heap      = instances you `new`
Metaspace = Klass / method / constant-pool metadata for loaded classes
```

Static *references* point at heap objects; the *description* of the class lives in metaspace.

## Technical Mechanism

| Trait | Detail |
|-------|--------|
| Location | Native (not counted in `-Xmx`) |
| Bound | `-XX:MaxMetaspaceSize=` (else grows toward native/cgroup limits) |
| OOM | `OutOfMemoryError: Metaspace` |
| Unloading | When defining **class loader** becomes unreachable |

```bash
java -XX:MaxMetaspaceSize=256m -Xlog:gc+metaspace=info …
jcmd <pid> VM.metaspace
```

Compressed class space is a related region when compressed class pointers are used.

## JVM Internals

- Each loaded class costs metaspace; generated proxies / Groovy / frequent redeploy amplify.  
- Class unload ≠ deleting a file — GC must collect the **loader**.  
- Loader leaks look like metaspace never returning after hot redeploy.

## Production Implications

Track metaspace used/committed in metrics. Heap-healthy + Metaspace OOM is a **different incident** from heap leak.

## Incident Link

Unexpected OOM with message `Metaspace` → [incidents.md](./incidents.md), [outofmemoryerror.md](./outofmemoryerror.md).

## Interview / PE

PermGen vs Metaspace? What must die for classes to unload? Is the string pool in metaspace? (No — heap on modern HotSpot.)

### Related

[heap.md](./heap.md) · [memory-leaks.md](./memory-leaks.md) · [outofmemoryerror.md](./outofmemoryerror.md)

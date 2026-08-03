# Runtime Data Areas

Where the JVM keeps program state while a process runs.

## Mental Model

```text
Shared across threads          Per thread
─────────────────────          ──────────
Heap (objects)                 Java stack (frames)
Metaspace (class metadata)     PC register
                               Native method stack
(+ Code Cache for JIT nmethods — native, process-wide)
```

Spec names: PC, JVM stacks, heap, method area (HotSpot ≈ **Metaspace**), native method stacks. Constant pool runtime structures sit with class metadata.

## Technical Mechanism

| Area | Contents | Failure |
|------|----------|---------|
| **Heap** | Instances, arrays | `OutOfMemoryError: Java heap space` |
| **Java stack** | Frames: locals, operand stack, link | `StackOverflowError` |
| **PC** | Current bytecode index (Java methods) | — |
| **Metaspace** | Klass, methods, constant pool, annotations… | `OutOfMemoryError: Metaspace` |
| **Native method stack** | JNI / native frames | overflow / native crash |
| **Code cache** | Compiled methods | `CodeCache is full` → may stop compiling |

## JVM Internals

- Heap is GC-managed; layout depends on collector (G1 regions, ZGC colored pointers, etc.).  
- Stacks are not GC roots only — they *are* roots: live references in frames keep objects alive.  
- Metaspace is **native** memory; class unloading requires the defining loader to become unreachable.  
- Direct `ByteBuffer` / FFM arenas / thread stacks add RSS **outside** `-Xmx`.

## Production Implications

Size containers for **total** footprint:

```text
RSS ≈ heap + metaspace + code cache + stacks×threads + direct/native + JVM overhead
```

Many “mysterious” OOMs under Kubernetes are **cgroup OOM kills** while heap looks fine — native/metaspace/direct buffers.

## Incident Hook — memory pressure

See [incidents.md](./incidents.md): rising RSS with stable heap used → check metaspace, direct memory, thread count × `-Xss`.

## Interview / PE

List all runtime data areas. Which are shared? Which cause which `OutOfMemoryError`? Where do static fields live? (The `Class`/`static` storage is associated with class metadata / heap mirrors — say: static *references* point to heap objects; metadata in metaspace.)

### Related

[heap.md](./heap.md) · [stack.md](./stack.md) · [metaspace.md](./metaspace.md) · [pc-register.md](./pc-register.md) · [native-method-stack.md](./native-method-stack.md)

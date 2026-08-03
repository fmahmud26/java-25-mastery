# Memory Management — Java 25 Deep Guide

How objects are allocated, retained, and reclaimed — and how to investigate when heap, metaspace, or stacks go wrong.

**Standards:** [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md) · [MASTER_INSTRUCTION.md](../MASTER_INSTRUCTION.md)

## Mental map

```text
GC roots (stacks, statics, JNI, …)
    ↓ strong reachability
Heap objects (+ Soft/Weak/Phantom policies)
    ↓ unreachable
Reclaimed by GC

Separate: Metaspace (class metadata) · Thread stacks · Direct/native
```

## Study path

1. Areas: [stack](./stack.md) · [heap](./heap.md) · [metaspace](./metaspace.md) · [object-allocation](./object-allocation.md) · [object-headers](./object-headers.md)  
2. Reachability: [references](./references.md) → strong / weak / soft / phantom  
3. Lifetime: [object-lifecycle](./object-lifecycle.md) · [object-retention](./object-retention.md)  
4. Failures: [memory-leaks](./memory-leaks.md) · [outofmemoryerror](./outofmemoryerror.md) · [stackoverflowerror](./stackoverflowerror.md)  
5. Ops: [investigation](./investigation.md) · [incidents](./incidents.md) · [practical/](./practical/) · [principal-engineer](./principal-engineer.md) · [interview](./interview.md)

## One-line PE rule

**A Java “leak” is almost always unintended strong reachability — prove it with metrics, then a heap dump path-to-GC-root, then a bounded fix.**

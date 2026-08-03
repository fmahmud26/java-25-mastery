# Interview — Memory Management

Advanced Java / Principal prep. Pair with [incidents.md](./incidents.md) and [investigation.md](./investigation.md).

---

## L1 — Areas

**Stack vs heap vs metaspace?**  
Stack: frames/refs per thread. Heap: objects/arrays + GC. Metaspace: class metadata (native).

**SOE vs OOM?**  
Stack frames exhausted vs allocation failed in a named space.

---

## L2 — Allocation & lifecycle

**TLAB?** Thread-local bump-pointer allocation in young gen — fast path.

**When eligible for GC?** No strong reachability from roots (soft/weak/phantom policies apply).

**Nulling a field frees memory immediately?** No — removes one ref; GC asynchronous; other refs may remain.

---

## L3 — References

| Type | One-liner |
|------|-----------|
| Strong | Default; keeps alive |
| Soft | Clear under pressure; cache-ish |
| Weak | Don’t pin; weak maps/keys |
| Phantom | Post-mortem; `get()` null; prefer Cleaner |

**Java leak?** Unintended strong retention.

---

## L4 — Investigation story

Walk **heap continuously increasing**:

1. Symptoms: rising after-GC baseline  
2. Metrics: old gen, cache size  
3. Dump: `GC.heap_dump`  
4. Analysis: dominator + path to root  
5. RC: unbounded static cache  
6. Fix: max size + eviction  
7. Prevention: metrics + review rule  

Same structure for ThreadLocal, listener, large alloc, unexpected OOM.

---

## Rapid fire

| Q | A |
|---|---|
| OOM variants? | Heap, Metaspace, Direct, native thread, compressed class, GC overhead… |
| WeakHashMap pitfall? | Strong value referencing key pins key |
| Retained size? | Memory freed if this object removed as dominator |
| Why container OOM with free heap? | Native/metaspace/stacks/direct outside `-Xmx` |
| Soft vs explicit cache? | Soft nondeterministic; prefer sized library caches |
| Compact headers Java 25? | JEP 519; `-XX:+UseCompactObjectHeaders`; not default yet |
| finalize()? | Avoid; use Cleaner / try-with-resources |

---

## Follow-ups

1. How compare two heap dumps?  
2. Prove ThreadLocal leak in MAT?  
3. Metaspace leak after redeploy — what retains the loader?  
4. When is raising `-Xmx` correct?  

**PE line:** Metrics first, dump second, resize last — and only for the right memory domain.

### Related

[principal-engineer.md](./principal-engineer.md) · [references.md](./references.md) · [outofmemoryerror.md](./outofmemoryerror.md)

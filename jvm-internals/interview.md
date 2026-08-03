# Interview — JVM Internals (Advanced)

Whiteboard + production storytelling. Pair with [incidents.md](./incidents.md) and [diagnostic-tools.md](./diagnostic-tools.md).

Dimensional prep (if present): [../interview-prep/jvm](../interview-prep/jvm/).

---

## L1 — Draw and name

**Architecture:** Class loaders → Runtime data areas → Execution engine → Native interface.

**Runtime areas:** Heap, Java stack, PC, Metaspace, Native method stack (+ code cache in practice).

**Heap vs stack:** Objects/arrays vs frames (locals + operand stack). Shared GC vs per-thread push/pop. OOM vs SOE.

---

## L2 — Class loading & loaders

Lifecycle: Loading → Linking (verify, prepare, resolve) → Initialization → Using → Unloading.

Delegation: Application → Platform → Bootstrap (`null`). Type identity = name + defining loader.

`ClassNotFoundException` vs `NoClassDefFoundError`?

---

## L3 — Execution engine

Interpreter profiles → tiered C1/C2 → nmethod. Intrinsics. OSR.

**Escape analysis:** non-escaping ⇒ scalar replacement / lock elision — not a language guarantee.

**Deoptimization:** speculation failed → safe slow path.

**Safepoint:** threads at known-safe state for VM ops; TTSP matters; not only GC.

---

## L4 — Principal incidents

| Prompt | Strong answer shape |
|--------|---------------------|
| High CPU | Profiler + dumps; separate app/GC/compiler |
| Metaspace OOM | Loader leak; prove with metaspace + dump roots |
| Latency spike, GC small | Safepoint sync / deopt / lock / class init |
| Large alloc | Histogram + streaming fix |
| “JIT made us slow” | Warm-up vs deopt storm vs code cache — evidence |

---

## Rapid-fire bank

| Q | A |
|---|---|
| Where is bytecode stored at runtime? | Method metadata / metaspace-related structures; executed by interpreter or as compiled code in code cache |
| Static field location? | Metadata in metaspace; referenced objects on heap |
| Why parent delegation? | Protect core classes; consistent bootstrap types |
| Tiered compilation? | Interpret → C1 → C2 for startup + peak |
| Compact headers Java 25? | JEP 519 product feature; `-XX:+UseCompactObjectHeaders`; not default yet |
| `-Xmx` = container limit? | No — RSS includes more |
| EA guarantee? | No |
| Safepoint = GC? | Safepoints used by GC and other VM ops |

---

## Follow-ups interviewers love

1. How do you prove a metaspace leak?  
2. What does a flame graph showing `CompilerThread` mean?  
3. Explain a deopt storm after plugin load.  
4. How can one tight loop stall the whole JVM?  
5. Walk allocation of `new byte[size]` from TLAB to humongous.  

## PE one-liner

**Internals knowledge without diagnostics is trivia; diagnostics without a mental model is cargo cult — carry both.**

### Related

[jvm-architecture.md](./jvm-architecture.md) · [escape-analysis.md](./escape-analysis.md) · [safepoints.md](./safepoints.md) · [incidents.md](./incidents.md)

# JVM — Interview Questions (L1–L4)

Escalate only when the interviewer pushes. Architecture → memory → JIT → production diagnosis.

---

## Level 1 — Junior

### What is the JVM?

**Answer:** A process virtual machine that loads Java bytecode (and other JVM languages), manages memory (heap/GC), and executes via interpreter + JIT. HotSpot is the common implementation. Write once, run on any compatible JVM.

### Heap vs stack?

**Answer:** **Heap** holds objects/arrays shared across threads (GC-managed). **Stack** (per thread) holds frames with local variables and operand stacks for method calls. Primitive locals live in frames; object *references* live in frames, objects usually on the heap.

### What is bytecode?

**Answer:** Platform-neutral instructions in `.class` files produced by `javac`. The JVM interprets them and/or JIT-compiles hot methods to native code.

---

## Level 2 — Mid-level

### How does class loading work?

**Answer:** Loaders (bootstrap → platform → application) locate bytes, define the class, then **link** (verify, prepare, resolve) and **initialize** (`<clinit>`). Parent delegation asks the parent first. Same binary name under two loaders = two distinct runtime classes.

### What memory areas does HotSpot expose conceptually?

**Answer:** Shared **heap** and **metaspace**; per-thread **Java stack**, **PC**, **native stack**. Code cache holds JIT code. Direct buffers / native allocs sit outside the Java heap but in process RSS.

### Interpreter vs JIT?

**Answer:** Interpreter starts immediately and profiles. Hot methods compile (C1 then often C2). JIT applies inlining, escape analysis, speculative opts; failed assumptions → **deoptimization**.

---

## Level 3 — Senior

### What is escape analysis and why does it matter?

**Answer:** JIT analysis of whether an allocation is reachable outside a method/thread. Non-escaping objects may be scalar-replaced or not heap-allocated; locks on non-escaping objects may be elided. Improves allocation/GC pressure but is not a language guarantee — verify with allocation profiles.

### StackOverflowError vs OutOfMemoryError — how do you reason?

**Answer:** SOE = thread stack depth (deep recursion / huge frames). OOME subtypes matter: Java heap, Metaspace, Unable to create native thread, Direct buffer memory. Fix differs: recursion/stack size vs leak/`-Xmx` vs loader leak vs thread explosion vs `MaxDirectMemorySize`.

### When do you worry about metaspace?

**Answer:** Dynamic proxies, Groovy/bytecode gen, app-server redeploys, many short-lived classloaders. Cap with `MaxMetaspaceSize`, fix loader lifecycle, confirm with `jcmd` classloader stats / NMT — **not** by blindly raising `-Xmx`.

---

## Level 4 — Expert

### Production: RSS climbs for days while `-Xmx` heap looks stable. How do you diagnose?

**Answer (structured):**

1. **Confirm symptoms** — container RSS / OOMKill; Java heap occupancy flat in JMX/JFR; latency maybe fine.  
2. **Tools** — NMT summary/detail; JFR (native alloc if available), `jcmd GC.heap_info`, direct-buffer MXBean, thread count, code-cache usage, classloader stats.  
3. **Hypotheses**  
   - Direct `ByteBuffer` / netty arenas not released  
   - Classloader / metaspace leak  
   - Thread / stack accumulation  
   - JNI / FFM native leak  
   - Code cache / compiler growth (less common “days”)  
4. **Evidence** — NMT category deltas; metaspace used; `BufferPoolMXBean`; thread dump counts; heap dump only if heap actually grows.  
5. **Remedies (risk-ordered)**  
   - Fix buffer lifecycle / arena bounds  
   - Discard loaders; stop generating unbounded classes  
   - Cap threads; virtual threads where appropriate  
   - Bound direct memory; fix native owner  
6. **Validate** — soak test RSS plateau; alerts on metaspace / direct memory / thread count.

**Common Mistake at L4:** Raising `-Xmx` when the Java heap was never the problem.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | Name the three class loaders. |
| 2 | What happens during linking? |
| 3 | How can JIT deoptimization cause latency spikes? |
| 4 | After redeploy, Metaspace OOM — diagnosis path? |

# Virtual Threads — Interview Questions (L1–L4)

Escalate only when the interviewer pushes. Platform vs VT → carriers → pinning → when not to use → production diagnosis.

---

## Level 1 — Junior

### What is a virtual thread?

**Answer:** A lightweight JVM-managed thread (Project Loom). Many virtual threads share a small set of **carrier** platform threads. Best for high numbers of concurrent **blocking** tasks. Same `Thread` API as platform threads.

### Virtual vs platform thread — one difference that matters?

**Answer:** Platform threads map 1:1 to OS threads and are costly; virtual threads are cheap and mount/unmount on carriers so blocking I/O doesn’t consume an OS thread for the whole wait (when unmount works).

### How do you start a virtual thread in Java 25?

**Answer:** `Thread.ofVirtual().start(runnable)`, `Thread.startVirtualThread(…)`, or `Executors.newVirtualThreadPerTaskExecutor()` for many tasks. Do not build a pool of virtual threads.

---

## Level 2 — Mid-level

### What is a carrier thread?

**Answer:** A platform thread that currently executes a virtual thread. When the VT blocks in a way the JVM can handle, it **unmounts** and the carrier can run another VT. Throughput depends on free carriers, not on VT count alone.

### Should you use virtual threads for CPU-intensive work?

**Answer:** Generally **no** as the main parallelism mechanism. VTs don’t add CPU cores; CPU-bound tasks rarely unmount, so they just contend on carriers. Use a sized platform pool / FJP; optionally have VTs submit CPU chunks to that pool.

### What is pinning?

**Answer:** When a virtual thread cannot unmount while blocked, it stays glued to its carrier. **Historically (JDK 21):** blocking inside `synchronized` was a major cause. **Java 24+ / JEP 491 (incl. 25):** `synchronized` no longer pins that way. Residual risks: native/JNI/FFM, some local file I/O, class init. Long I/O under locks remains bad **design** even when not pinning.

---

## Level 3 — Senior

### When would you *not* adopt virtual threads?

**Answer:** Pure CPU pipelines; workloads needing careful native thread affinity; libraries that pin/block carriers heavily with no fix path; or when you already have a well-tuned reactive stack and migration cost outweighs benefit. Also reject “pool of N VTs” designs.

### Virtual threads vs reactive (e.g. WebFlux)?

**Answer:** Both target high concurrency I/O. VTs keep **imperative blocking** style (JDBC OK). Reactive uses event loops/operators and prefers non-blocking drivers. On Java 25, many services choose MVC/blocking + VTs for simplicity; keep reactive when the ecosystem or streaming model demands it.

### How does structured concurrency fit?

**Answer:** Preview API (`StructuredTaskScope`, JEP 505 in JDK 25): fork related subtasks (often on VTs), join as one unit, cancel siblings on failure. Improves lifetimes vs fire-and-forget `Future`s. Needs `--enable-preview`; mention API may change.

---

## Level 4 — Expert

### Production: after switching to virtual threads, throughput flatlines and latency rises under load. Carriers look busy. How do you diagnose?

**Answer (structured):**

1. **Symptoms** — VT count huge, throughput ≤ old platform pool, p99 up, maybe low app CPU but saturated carriers.  
2. **Tools** — JFR (virtual thread / pinned events), `jcmd` thread dump (VT + carrier), async-profiler, dependency metrics (DB pool wait).  
3. **Hypotheses** (Java 25 order)  
   - **CPU-heavy work on VTs** (most common “worse after Loom”)  
   - Downstream connection pool smaller than concurrency  
   - Unbounded fan-out → memory + GC  
   - Lock held across I/O (design)  
   - **Residual** pinning: JNI/FFM, local file I/O, class-init — *not* “synchronized pins” as primary (JEP 491)  
4. **Evidence** — flame graphs on VT CPU frames; DB pool wait ≈ latency; heap growth; JFR `jdk.VirtualThreadPinned` only if present.  
5. **Fixes**  
   - Offload CPU to platform pool; keep VT on I/O  
   - `Semaphore`/bulkhead to match DB/HTTP limits  
   - Cap submits; backpressure at entry  
   - Shorten lock scope; isolate native on small platform pool  
6. **Validate** — load test; carrier idle, p99, pool waits; pinning near zero if it was residual.

**Common Mistake at L4:** Leading with JDK 21 synchronized-pin lore on Java 25, or “VT are broken” without separating CPU-on-VT vs pool saturation vs residual pin.

### Production: intermittent stalls; dumps show few carriers stuck in native while millions of VTs wait. Approach?

**Answer (structured):**

1. **Confirm** — dump: carriers in native/BLOCKED; runnable VTs queued.  
2. **Hypotheses** — residual pinning (JNI/FFM/file); global lock-over-I/O; scarce native thread affinity.  
3. **Evidence** — JFR pinned samples; attribute to library frame.  
4. **Fixes** — redesign lock scope; isolate native calls on a **small platform pool**; circuit-break slow deps; upgrade libs.  
5. **Validate** — pinning metric → near zero; latency recovers under same load.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | Does `Thread.sleep` on a VT waste an OS thread? |
| 2 | Why not `Executors.newFixedThreadPool(10_000)` of VTs? |
| 3 | How do you combine VT I/O with a CPU pool safely? |
| 4 | Service OOMs after enabling VTs — diagnosis path? |

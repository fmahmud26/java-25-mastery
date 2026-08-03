# Garbage Collection — Interview Questions (L1–L4)

Escalate only when the interviewer pushes. Generational → collector choice → failure modes → prod diagnosis.

---

## Level 1 — Junior

### What is garbage collection?

**Answer:** Automatic reclamation of objects that are no longer reachable from GC roots. The JVM (HotSpot) runs collectors so you don’t `free` memory manually. Unreachable ≠ “I don’t need it anymore” if a cache still references it.

### Young vs old generation?

**Answer:** Young holds new allocations (Eden + survivors); collected often. Objects that survive long enough are **promoted** to old, collected less frequently. Matches the observation that most objects die young.

### What is Stop-The-World?

**Answer:** A pause where application threads stop at a safepoint so GC can do work that isn’t safe concurrently. All HotSpot collectors have some STW; length/frequency differ.

---

## Level 2 — Mid-level

### How does a minor (young) GC work at a high level?

**Answer:** From roots (and remembered-set / card data for old→young pointers), mark live objects in Eden/Survivor, evacuate survivors to the other survivor (or promote), reclaim Eden. Triggered typically by **allocation failure** when Eden is full.

### G1 vs ZGC?

**Answer:** **G1** splits the heap into regions, evacuates young regions, concurrently marks, then mixed-collects garbage-heavy old regions; tuned around a pause goal. **ZGC** uses colored pointers/load barriers to mark and relocate mostly concurrently with very short pauses, scaling to large heaps; generational ZGC improves allocation efficiency. Choose G1 for balanced defaults; ZGC when tail latency / huge heaps dominate.

### What is a Full GC?

**Answer:** A collection that (effectively) processes the whole heap — expensive. In G1 it’s often a fallback when concurrent/mixed collections can’t keep up (evacuation failure, etc.). Frequent Full GC is a production red flag.

---

## Level 3 — Senior

### What causes frequent Full GCs on G1?

**Answer:** Live set too large for heap; allocation/promotion outpacing mixed GC; humongous allocations fragmenting regions; to-space exhaustion; overly aggressive pause goals starving collection work; explicit `System.gc()`. Fix live set / heap sizing / allocation patterns before exotic flags.

### How do you reason about allocation failure?

**Answer:** Normal path: thread fails to bump-allocate in TLAB/Eden → young GC. If after GC there’s still no room (or promotion fails), escalate toward old/mixed/Full and eventually OOME. High failure *rate* means high allocation rate or too-small young gen — profile allocations, don’t only raise heap blindly.

### When would you *not* pick ZGC?

**Answer:** Very small heaps where barrier/concurrent overhead loses to Serial/G1; workloads that are purely throughput-bound and measure better on Parallel/G1; environments where ZGC isn’t available/supported. Always measure p99 and CPU.

---

## Level 4 — Expert

### Production: p99 latency spikes every few minutes; CPU OK; old gen slowly climbs then a long pause. Diagnose.

**Answer (structured):**

1. **Confirm symptoms** — latency charts align with GC pauses; GC log / JFR shows long STW or G1 Full / mixed; old occupancy sawtooth.  
2. **Tools** — `-Xlog:gc*`, JFR GC events, heap dump at high old occupancy, allocation flame graph, promotion metrics.  
3. **Hypotheses**  
   - Unbounded cache / listener leak (true leak)  
   - Cache with long TTL → large live set (working set)  
   - Humongous / big-byte[] churn  
   - Pause goal too low → backlog → Full GC  
   - Wrong collector for heap size  
4. **Evidence** — dominator tree (leak vs expected live); humongous counts; evacuation failures; allocation sites.  
5. **Remedies (risk-ordered)**  
   - Bound caches; fix static/listener leaks  
   - Right-size heap to live set + headroom  
   - Reduce allocation / large objects  
   - Relax pause goal or switch ZGC if latency-bound  
6. **Validate** — soak test; alert on Full GC and old-after-GC; compare p99.

**Common Mistake at L4:** Only flipping collector flags without proving leak vs insufficient heap vs allocation storm.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | What are GC roots? |
| 2 | What is promotion? |
| 3 | How do remembered sets help young GC? |
| 4 | Container OOM-killed with heap at 60% — what’s going on? |

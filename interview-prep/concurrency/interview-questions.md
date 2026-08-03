# Concurrency — Interview Questions (L1–L4)

Escalate only when the interviewer pushes. Threads → sync → HB → CHM/pools → production diagnosis.

---

## Level 1 — Junior

### What is a thread? Process vs thread?

**Answer:** A process has its own address space. A thread is a schedulable unit of execution that shares the process heap/code. Concurrency bugs come from shared mutable state without proper synchronization.

### What does `synchronized` do?

**Answer:** Acquires the object’s intrinsic lock (mutual exclusion) and establishes happens-before on unlock→lock so writes in the critical section become visible to the next thread that locks the same monitor. Methods and blocks are reentrant for the same thread.

### What is `volatile`?

**Answer:** Guarantees visibility and ordering for that field’s reads/writes. It does **not** make compound actions like `count++` atomic. Use for flags / safe publication of a single field; use atomics or locks for counters and multi-field invariants.

---

## Level 2 — Mid-level

### What is happens-before?

**Answer:** A JMM ordering edge: if action A happens-before B, A’s effects are visible to B and ordered before it. Common edges: program order, monitor unlock→lock, volatile write→read, `Thread.start`/`join`, and documented concurrent-collection sync points. Without an edge, reordering/stale reads are allowed.

### How do you create and run work concurrently in Java 25?

**Answer:** Prefer virtual threads for blocking work: `Thread.ofVirtual().start(…)` or `Executors.newVirtualThreadPerTaskExecutor()`. Use a sized platform pool / FJP for CPU-bound tasks. Implement `Runnable`/`Callable`; never call `run()` instead of `start()` when you need a new thread.

### ConcurrentHashMap vs Hashtable vs synchronized HashMap?

**Answer:** `HashMap` is not safe for concurrent mutation. `Hashtable` / `Collections.synchronizedMap` use coarse locking and scale poorly. `ConcurrentHashMap` uses fine-grained concurrency (CAS/bin locks), forbids null keys/values, offers atomic `compute`/`merge`, and weakly consistent iterators.

---

## Level 3 — Senior

### volatile vs AtomicInteger vs synchronized — when each?

**Answer:**  
- **`volatile`** — visibility/ordering for one field (e.g. shutdown flag).  
- **`Atomic*` / CAS** — atomic updates to one variable without a full critical section.  
- **`synchronized` / locks** — multi-field invariants, check-then-act spanning several locations, wait/notify conditions.  
Using `volatile` for `count++` is a classic race.

### How do deadlocks happen and how do you prevent them?

**Answer:** Circular wait while holding locks (Coffman: mutex, hold-and-wait, no preemption, circular wait). Prevent with consistent global lock ordering, avoiding nested locks, `tryLock` timeouts, or redesigning to a single lock / lock-free structure. Detect with thread dumps (`FOUND JAVA-LEVEL DEADLOCK`).

### How would you size an executor / choose VT vs platform pool?

**Answer:** Blocking I/O fan-out → virtual thread per task (don’t pool VTs). CPU-bound → ~`availableProcessors()` platform threads (or FJP). Mixed: VT orchestrates I/O, submits CPU chunks to a platform pool. Watch queue depth, rejection policy, and never hold locks across long I/O.

---

## Level 4 — Expert

### Production: p99 latency spikes; many threads BLOCKED; occasional complete stall. How do you diagnose?

**Answer (structured):**

1. **Symptoms** — latency cliffs, timeouts, thread count high, CPU maybe low during stall.  
2. **Tools** — `jcmd <pid> Thread.print` / `jstack`; JFR + async-profiler (lock/cpu); app metrics (pool queue, active, CHM sizes).  
3. **Hypotheses**  
   - Classic deadlock (circular monitors)  
   - Lock convoy / single global lock under load  
   - Pool exhaustion: all workers blocked on I/O or downstream  
   - Hot lock or hot CHM key  
   - Accidental coarse `synchronized` around remote calls  
4. **Evidence** — dump shows deadlock or same monitor; flame graph in `park`/`synchronized`; queue depth → max; one key dominates updates.  
5. **Fixes (risk-ordered)**  
   - Break deadlock (lock order / remove nesting)  
   - Shrink critical sections; move I/O outside locks  
   - Shard maps / use `LongAdder`; replace global lock with CHM/striping  
   - Separate I/O (VT) from CPU pools; tune timeouts/circuit breakers  
6. **Validate** — reproduce under load test; compare p99 + lock profiles; add alerts on pool queue / blocked time.

**Common Mistake at L4:** “Add more threads” or “just use ConcurrentHashMap” without naming the contended resource or proving it with dumps/profiles.

### Production: wrong counters / lost updates under load, no crash. Approach?

**Answer (structured):**

1. **Symptoms** — under-count, duplicate IDs, intermittent map corruption symptoms.  
2. **Tools** — code audit of shared fields; JCStress-style reasoning; JFR for races is harder — prefer finding missing sync.  
3. **Hypotheses** — non-atomic `count++` on shared int; check-then-act on `HashMap`; `volatile` misused for compounds; double-checked locking without safe publication.  
4. **Fixes** — `Atomic*`/`LongAdder`, CHM `compute`/`merge`, proper locks, immutable snapshots.  
5. **Validate** — stress test with many threads; assert invariants; add metrics that detect drift.

---

## Extra ladder prompts (practice)

| L | Prompt |
|---|--------|
| 1 | Difference between `Runnable` and `Callable`? |
| 2 | How does `ReentrantLock` differ from `synchronized`? |
| 3 | Explain weakly consistent CHM iterators. |
| 4 | Service GC and latency worsen after “optimizing” with a huge shared HashMap under sync — diagnose. |

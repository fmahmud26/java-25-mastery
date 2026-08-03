# Interview — Concurrency (Staff / Principal)

Answer with **invariant → tool → failure mode → dump/metric**. Depth in topic files. Java 25.

---

## Foundations

| Q | Sketch |
|---|--------|
| Process vs thread? | Isolation vs shared heap |
| Platform thread? | OS-backed; pool carefully |
| Lifecycle states? | BLOCKED vs WAITING matters |
| JMM? | Visibility + ordering contract |
| Happens-before? | Unlock/lock, volatile, start/join |

---

## Tools

| Need | Tool |
|------|------|
| Critical section | synchronized / ReentrantLock |
| Flag / publish | volatile / AtomicReference |
| Counter | Atomic* / LongAdder |
| Concurrent map | ConcurrentHashMap |
| Job handoff | BlockingQueue + Executor |
| Limit in-flight | Semaphore |
| Startup gate | CountDownLatch |

synchronized vs ReentrantLock · volatile vs Atomic · Latch vs Barrier · CHM guarantees

---

## Hazards

Race (check-then-act) · Deadlock (dump cycle) · Livelock (RUNNABLE no progress) · Starvation · Contention (hot lock)

---

## Scenario drills

1. **Payments:** idempotent capture under concurrent retries — design.  
2. **Inventory:** prevent oversell in one JVM vs multi-node.  
3. **Orders:** workers + bounded queue + rejection policy.  
4. **Caches:** immutable snapshot vs RW lock vs CHM.  
5. **High-traffic API:** pool sizing, Future timeouts, nested pool deadlock.  
6. **Job processing:** shutdown, interrupt, poison pill.

---

## Dump exercise (verbalize)

Given AB-BA stacks on Order/Inventory monitors — name the bug, fix (ordering / remove nesting), prevention.

---

## Principal discussions

1. Bulkheads: separate executors for PSP vs DB vs CPU.  
2. When in-memory concurrency is the wrong layer (need DB/broker).  
3. VT impact: more concurrency ≠ fewer races.  
4. Review bar: unbounded queues/pools, lock-over-I/O, bare `new Thread`.  
5. SLOs: lock wait metrics, queue depth, rejected executions.  
6. Safe publication checklist for a new shared cache.

---

## Quick fire

| Q | A |
|---|---|
| count++ safe? | No |
| HashMap concurrent? | No |
| Future.get no timeout? | Avoid on request path |
| Fair lock default? | No (unfair) |
| COW for hot writes? | No |

### Related

[README.md](./README.md) · [thread-dumps-and-debugging.md](./thread-dumps-and-debugging.md) · [java-memory-model.md](./java-memory-model.md) · [deadlock.md](./deadlock.md) · [executor-service.md](./executor-service.md)

# Concurrency — Cheat Sheet

**Sources:** [../concurrency/README.md](../concurrency/README.md) · [java-memory-model](../concurrency/java-memory-model.md) · [happens-before](../concurrency/happens-before.md) · [concurrenthashmap](../concurrency/concurrenthashmap.md) · [java-interview-questions/concurrency](../java-interview-questions/concurrency/) · [experiments/lock-contention-modes](../experiments/lock-contention-modes/) · [experiments/atomic-vs-synchronized-counter](../experiments/atomic-vs-synchronized-counter/)

## JMM (must)

- **Happens-before** → visibility + ordering (not just mutual exclusion).  
- Tools establishing HB: monitor unlock→lock, volatile write→read, thread start/join, etc. — [happens-before.md](../concurrency/happens-before.md)  
- Library-level modes: [VarHandle](../concurrency/varhandles.md) (plain / acquire / release / volatile / CAS)  
- Data race = conflicting accesses without HB.

## Tool chooser

| Need | Tool | Source |
|------|------|--------|
| Mutual exclusion | `synchronized` / `ReentrantLock` | [synchronized](../concurrency/synchronized.md) · [reentrantlock](../concurrency/reentrantlock.md) |
| Flag / safe publish | `volatile` / `AtomicReference` | [volatile](../concurrency/volatile.md) |
| Ordered publish (libs) | `VarHandle` acquire/release | [varhandles](../concurrency/varhandles.md) |
| Counter | `Atomic*` / `LongAdder` | [atomic-variables](../concurrency/atomic-variables.md) |
| Concurrent map | `ConcurrentHashMap` atomic ops | [concurrenthashmap](../concurrency/concurrenthashmap.md) |
| Work queue | `BlockingQueue` | [blockingqueue](../concurrency/blockingqueue.md) |
| Limit concurrency | `Semaphore` / pool bounds | [semaphore](../concurrency/semaphore.md) · [executor-service](../concurrency/executor-service.md) |

## Hazards

| Hazard | One-liner | Depth |
|--------|-----------|-------|
| Race | Lost update / check-then-act | [race-condition](../concurrency/race-condition.md) |
| Deadlock | Lock order / tryLock | [deadlock](../concurrency/deadlock.md) |
| Pool exhaustion | Idle CPU, all threads blocked | [interview bank](../java-interview-questions/concurrency/q02-pool-exhaustion.md) |
| Retry storm | Amplification w/o jitter/budget | [q06](../java-interview-questions/concurrency/q06-retry-storm.md) |

## Interview defaults

1. Name the invariant.  
2. Pick the smallest sync tool.  
3. Never hold locks across remote I/O.  
4. Timeouts on `.get()` / HTTP.  
5. Result = stress test or pool-wait metric.

VT contrast: [virtual-threads cheat sheet](./virtual-threads.md) — VT do **not** remove races ([concurrency README](../concurrency/README.md)).

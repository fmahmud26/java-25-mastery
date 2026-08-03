# Concurrency — Theory

## Core contracts

| Concept | Meaning |
|---------|---------|
| Race | Shared mutable state + concurrent access without sync → wrong result |
| Visibility | One thread’s write must become visible to another |
| Atomicity | Compound actions (check-then-act, `count++`) need more than `volatile` |
| Happens-before | Ordering edge that restores visibility + order between actions |
| Deadlock | Circular wait on locks — stuck forever |
| Livelock / starvation | Busy without progress / one party never gets the resource |

## Mental model

- **Thread** = unit of concurrent execution sharing the heap.
- **Monitor / lock** = mutual exclusion + happens-before on unlock→lock.
- **`volatile`** = visibility + ordering for *that* field — not atomic compound ops.
- **Executor / pool** = manage worker lifecycle; don’t `new Thread` ad hoc for servers.
- On **Java 25**, prefer **virtual threads** for blocking I/O concurrency; platform pools for CPU-bound work.

## Happens-before (interview shortlist)

| Edge | Example |
|------|---------|
| Program order | Same thread, earlier statement → later |
| Monitor | unlock `m` → subsequent lock `m` |
| Volatile | write `v` → subsequent read `v` |
| Thread start / join | `start()` → run; end of run → `join()` returns |
| Concurrent collections | Documented sync points (e.g. CHM put → later get) |

## Tool selection

| Need | Reach for |
|------|-----------|
| Critical section / multi-field invariant | `synchronized` / `ReentrantLock` |
| Flag / safe publication of one field | `volatile` |
| Atomic counter / CAS | `Atomic*` / `LongAdder` |
| Concurrent map | `ConcurrentHashMap` |
| Bounded parallelism (CPU) | fixed platform pool / FJP |
| Massive blocking tasks (I/O) | virtual thread per task |

Related chapter: [synchronized.md](../../concurrency/synchronized.md), [volatile.md](../../concurrency/volatile.md), [ordering.md](../../concurrency/ordering.md), [deadlock.md](../../concurrency/deadlock.md).

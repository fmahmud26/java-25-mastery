# Thread Lifecycle

## Mental Model

```text
NEW → RUNNABLE → (running) → BLOCKED / WAITING / TIMED_WAITING → TERMINATED
```

| State | Meaning |
|-------|---------|
| NEW | Created, not started |
| RUNNABLE | Runnable / running / waiting for CPU |
| BLOCKED | Waiting to enter `synchronized` |
| WAITING | `wait()`, `join()`, `LockSupport.park` |
| TIMED_WAITING | sleep, timed wait/join/park |
| TERMINATED | `run` finished |

## Internal Mechanics

JVM maps states to OS scheduling + monitor/park machinery. `BLOCKED` ≠ `WAITING` — dumps tell you which.

## Code

```java
Thread t = Thread.ofPlatform().start(() -> {
    synchronized (lock) {
        try {
            lock.wait(); // WAITING
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
});
t.getState();
t.join();
```

## Production Scenario — job processing

Workers WAITING on `BlockingQueue.take`; stuck BLOCKED on a shared lock → inventory update hotspot.

## Failure Scenario

Calling `run()` instead of `start()` → same thread, no parallelism. Ignoring interrupt → shutdown hangs.

## Debugging Strategy

Thread dump: see [thread-dumps-and-debugging.md](./thread-dumps-and-debugging.md). Filter by pool name.

## Performance

Transitions involving kernel futex/monitor are costly under contention.

## Trade-offs

Park/wait frees CPU; spin wastes CPU but may win briefly at tiny critical sections (usually don’t hand-roll).

## Interview Questions

- BLOCKED vs WAITING?  
- What does RUNNABLE really mean?

## Principal-Level Discussion

Lifecycle literacy turns dumps into root cause: deadlock (cycle of BLOCKED), pool starvation (all WAITING on same lock), I/O wait vs CPU.

### Related

[creating-threads.md](./creating-threads.md) · [deadlock.md](./deadlock.md) · [thread-dumps-and-debugging.md](./thread-dumps-and-debugging.md)

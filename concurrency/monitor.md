# Monitors

Each Java object can be a **monitor**: mutual exclusion + `wait`/`notify` condition queue.

## Mental Model

```text
monitor = lock + wait-set
wait() releases lock and parks
notify/notifyAll wakes waiters (must re-acquire lock)
```

## Internal Mechanics

`wait/notify` must hold the monitor. Spurious wakeups → always `while (!condition) wait()`. notify wakes one; notifyAll all.

## Code

```java
synchronized (lock) {
    while (!workAvailable) {
        lock.wait();
    }
    Job job = queue.poll();
}
synchronized (lock) {
    queue.offer(job);
    lock.notifyAll();
}
```

Prefer `BlockingQueue` over hand-rolled wait/notify for job queues.

## Production Scenario — job processing

Classic producer/consumer; production code uses `ArrayBlockingQueue` instead of raw monitors.

## Failure Scenario

`if` instead of `while` on wait → spurious wakeup bugs. notify instead of notifyAll with multiple conditions → stuck workers.

## Debugging Strategy

WAITING on `Object.wait`; check who should notify.

## Performance

notifyAll can thundering-herd; use java.util.concurrent when possible.

## Trade-offs

Raw monitors educational; concurrent utilities safer.

## Interview Questions

- Why wait in a loop?  
- notify vs notifyAll?

## Principal-Level Discussion

Ban new wait/notify in app code unless implementing a library primitive — use j.u.c.

### Related

[synchronized.md](./synchronized.md) · [blockingqueue.md](./blockingqueue.md) · [intrinsic-locks.md](./intrinsic-locks.md)

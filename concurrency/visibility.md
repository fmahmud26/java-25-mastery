# Visibility

Whether one thread’s write is **seen** by another. Separate from atomicity and locking for multi-step invariants.

## Mental Model

```text
Thread A: shared = 1
Thread B: print(shared)  → may print 0 forever without HB
```

## Internal Mechanics

Caches/registers/store buffers. Volatile/sync/atomics flush visibility via HB. Plain reads/writes have no cross-thread guarantee.

## Code

```java
volatile boolean shutdown;

void stop() { shutdown = true; }
void worker() {
    while (!shutdown) {
        doWork();
    }
}
```

## Production Scenario — job processing

Worker loop must see shutdown flag → `volatile` or interrupt.

## Failure Scenario

Non-volatile boolean `running`; main sets false; worker never stops → deploy hang.

## Debugging Strategy

Thread dump shows RUNNABLE in loop; flag “already false” in debugger on another thread — visibility bug.

## Performance

Volatile reads are cheap-ish; still not free in hot loops — prefer better coordination (blocking queue poison pill).

## Trade-offs

Volatile vs interruption vs `CountDownLatch` for shutdown.

## Interview Questions

- Visibility vs atomicity?  
- Is `volatile` enough for `count++`?

## Principal-Level Discussion

Many “random infinite loops” in prod are visibility. Make shutdown and config publish paths explicit.

### Related

[volatile.md](./volatile.md) · [atomicity.md](./atomicity.md) · [happens-before.md](./happens-before.md)

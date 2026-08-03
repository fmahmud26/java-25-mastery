# Concurrency — Implementation

Idiomatic Java 25 usage (not internals).

```java
// Mutual exclusion + visibility
private final Object lock = new Object();
private int balance;

void deposit(int n) {
    synchronized (lock) {
        balance += n;
    }
}

// volatile flag / publication
private volatile boolean running = true;
void stop() { running = false; }

// Atomics for single-variable updates
private final AtomicInteger hits = new AtomicInteger();
hits.incrementAndGet();

// Executors — platform pool for CPU-bound
try (var cpu = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
    cpu.submit(() -> compress(buf));
}

// Concurrent map — compound ops, no nulls
ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
counts.merge("a", 1, Integer::sum);
counts.compute("a", (k, v) -> v == null ? 1 : v + 1);
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Guard invariant | `synchronized` / `ReentrantLock` |
| Interruptible / try-lock | `ReentrantLock` |
| Visibility only (flag) | `volatile` |
| Counter under contention | `LongAdder` / `AtomicLong` |
| Shared map | `ConcurrentHashMap` |
| Wait for N events | `CountDownLatch` |
| Limit concurrency | `Semaphore` |
| Blocking I/O fan-out | `Executors.newVirtualThreadPerTaskExecutor()` |

```java
// Deadlock-safe lock order (always A then B)
synchronized (lockA) {
    synchronized (lockB) { /* … */ }
}
```

Related: [creating-threads.md](../../concurrency/creating-threads.md), [concurrenthashmap.md](../../concurrency/concurrenthashmap.md), [reentrantlock.md](../../concurrency/reentrantlock.md).

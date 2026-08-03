# Concurrency — Coding

Patterns that show up constantly:

| Problem | Pattern |
|---------|---------|
| Safe counter / ID | `AtomicInteger` / `LongAdder` |
| Producer–consumer | `BlockingQueue` |
| Print in order / barrier | `CountDownLatch` / `CyclicBarrier` |
| Rate limit N workers | `Semaphore` |
| Cache map under concurrency | `ConcurrentHashMap` + `compute` |
| Avoid deadlock | Global lock order / `tryLock` |
| Transfer ownership | Happens-before via join / queue / volatile publish |

```java
// Bounded producer–consumer
BlockingQueue<Job> q = new ArrayBlockingQueue<>(256);

void producer(Job j) throws InterruptedException {
    q.put(j);                    // blocks if full
}

void consumer() throws InterruptedException {
    Job j = q.take();            // blocks if empty
    j.run();
}
```

```java
// ConcurrentHashMap — atomic upsert
map.compute(key, (k, v) -> v == null ? List.of(item) : append(v, item));
```

```java
// Latch: wait for N workers
CountDownLatch done = new CountDownLatch(n);
for (int i = 0; i < n; i++) {
    Thread.startVirtualThread(() -> {
        try { work(); } finally { done.countDown(); }
    });
}
done.await();
```

**Talk track:** race → identify shared state → pick minimal sync (atomic / CHM / lock) → prove HB edge → consider deadlock / fairness.

Practice: chapter drills in [../../concurrency](../../concurrency/), coding problems under [../../coding-problems](../../coding-problems/).

# Thread Pools with Virtual Threads

## Mental Model

```text
Platform threads: pool & reuse (scarce)
Virtual threads:  create per task — do NOT pool VTs like platform threads
```

Pooling VTs reintroduces artificial concurrency caps and queueing you adopted VT to avoid (for the *thread* resource).

## Code

```java
// Correct — VT per task
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    exec.submit(task);
}

// Usually wrong — fixed pool of VTs
Executors.newFixedThreadPool(200, Thread.ofVirtual().factory());
// This caps concurrency at 200 tasks again (like a platform pool)
```

## When a Bounded Pool Still Makes Sense

| Need | Approach |
|------|----------|
| Limit concurrent CPU tasks | Platform fixed pool |
| Limit concurrent calls to a dependency | Semaphore / bulkhead **or** small pool of workers (often platform) |
| Limit concurrent VT tasks intentionally | Semaphore preferred over fixed VT pool |

```java
Semaphore dbAdmission = new Semaphore(50);
try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    exec.submit(() -> {
        dbAdmission.acquire();
        try { return repo.query(); }
        finally { dbAdmission.release(); }
    });
}
```

Better: size **Hikari max** and use acquisition timeouts — admission at the real scarce resource.

## Production Scenario — connection pool exhaustion

10K VTs + Hikari 30 → 9970 waiting for connections. Fix: pool size, query time, timeouts, shed load — not a bigger VT pool.

## Failure Scenario

`newFixedThreadPool(N, virtualFactory)` then wondering why VT “didn’t scale.”

## Interview / PE

Why not pool virtual threads? How do you bound work without pooling VTs?

### Related

[executors-new-virtual-thread-per-task-executor.md](./executors-new-virtual-thread-per-task-executor.md) · [database-connection-pools.md](./database-connection-pools.md) · [downstream-limitations.md](./downstream-limitations.md)

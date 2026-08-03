# BlockingQueue

`Queue` that **blocks** on take when empty and/or on put when full — core producer/consumer handoff in `java.util.concurrent`.

## 1. Mental Model

```text
producers ──put/offer──► [bounded buffer] ──take/poll──► consumers
                 ↑ block if full              ↑ block if empty
```

## 2. Internals / Variants

| Impl | Structure | Notes |
|------|-----------|-------|
| `ArrayBlockingQueue` | Bounded circular array | Fixed capacity; fair optional |
| `LinkedBlockingQueue` | Linked nodes | Optional capacity (default huge) |
| `PriorityBlockingQueue` | Heap | Unbounded; priority |
| `DelayQueue` | Delayed elements | Scheduling |
| `SynchronousQueue` | No storage | Direct handoff |
| `LinkedTransferQueue` | Transfer | Advanced handoff |

| Topic | Behavior |
|-------|----------|
| Concurrency | Thread-safe |
| Ordering | FIFO for Array/Linked (except priority/delay) |
| Iteration | Weakly consistent typically |
| Rejection | Executor uses queue + `RejectedExecutionHandler` |

## 3. Code — job queue

```java
BlockingQueue<Job> jobs = new ArrayBlockingQueue<>(10_000);

// producer
if (!jobs.offer(job, 100, TimeUnit.MILLISECONDS)) {
    metrics.markRejected();
    throw new CapacityExceededException();
}

// consumer workers
Job job = jobs.take(); // blocks
process(job);
```

## 4. Scenarios

| Scenario | Choice |
|----------|--------|
| Job queues | ArrayBlockingQueue bounded |
| Leaderboard updates fan-in | Bound + drop/sample policy |
| Rate limiting pipelines | Queue of events + workers |
| Unbounded LinkedBlockingQueue | Dangerous default — prefer bounds |

## 5. When BlockingQueue instead of ArrayDeque?

Multi-threaded producers/consumers → BlockingQueue. Single thread → ArrayDeque.

## 6. Failure Scenario

| | |
|--|--|
| Symptom | OOM; thread starvation; deadlock |
| Cause | Unbounded queue; consumers dead; take without timeout in wrong place; circular wait |
| Fix | Bound capacity; rejection metrics; timeouts; poison-pill shutdown protocol |
| Prevent | Load test backpressure; alert on queue depth |

## 7. Interview (Senior → Principal)

- `put` vs `offer` vs `offer(timeout)`?  
- ArrayBlockingQueue vs LinkedBlockingQueue?  
- Fairness?  
- **Staff:** sizing a queue for 5K RPS, 50ms work?  
- **Principal:** in-process BlockingQueue vs Kafka — ownership, durability, replay?

### Related

[queue.md](./queue.md) · [arraydeque.md](./arraydeque.md) · [priorityqueue.md](./priorityqueue.md) · [decision-matrix.md](./decision-matrix.md)

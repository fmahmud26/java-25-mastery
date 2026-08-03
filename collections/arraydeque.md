# ArrayDeque

Resizable **circular array** implementing `Deque` — best default for stack/queue in single-threaded code.

## 1. Mental Model

```text
head and tail indices on a ring buffer
offerLast / pollFirst → classic queue
push / pop → stack
```

## 2. Internals

| Topic | Behavior |
|-------|----------|
| Structure | `Object[]` ring; head/tail |
| Growth | Double capacity when full; redistribute |
| Ends ops | Amortized O(1) |
| Null elements | **Not allowed** |
| Random access | No List index API |
| Iteration | Fail-fast |
| Concurrency | Not thread-safe |
| Memory | Better locality than LinkedList |

## 3. Scenarios

- **Job queue** inside one thread / actor.  
- **Undo stack.**  
- **Sliding window** of request timestamps for rate limiting (single thread).

```java
Deque<Runnable> localJobs = new ArrayDeque<>();
localJobs.offerLast(this::process);
Runnable r = localJobs.pollFirst();
```

## 4. When ArrayDeque instead of LinkedList / Stack / ArrayList?

| vs | Prefer ArrayDeque when |
|----|------------------------|
| LinkedList | Always for deque/queue/stack |
| Stack | Always (Stack is legacy sync) |
| ArrayList | Ends-only workloads; no index need |
| BlockingQueue | Only single-threaded handoff |

## 5. Failure Scenario

Sharing ArrayDeque across threads → races. Null offer → NPE. Unbounded growth → OOM.

## 6. Interview

- Why ArrayDeque over LinkedList?  
- Circular buffer mechanics?  
- **Staff:** growth and wrapping behavior?

### Related

[deque.md](./deque.md) · [queue.md](./queue.md) · [linkedlist.md](./linkedlist.md) · [blockingqueue.md](./blockingqueue.md)

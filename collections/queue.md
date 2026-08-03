# Queue

Handoff of elements — typically **FIFO** (exceptions: priority queues).

## 1. Mental Model

```text
offer → [tail ...... head] → poll
```

## 2. Contract

| Method | Exception style | Special value |
|--------|-----------------|---------------|
| Insert | `add` throws | `offer` → false |
| Remove | `remove` throws | `poll` → null |
| Examine | `element` throws | `peek` → null |

Prefer `offer`/`poll`/`peek` for queues that can be full/empty.

## 3. Implementations

| Impl | Role |
|------|------|
| [ArrayDeque](./arraydeque.md) | Fast non-concurrent deque/queue |
| [PriorityQueue](./priorityqueue.md) | Priority heap |
| [LinkedList](./linkedlist.md) | Also Queue/Deque (usually slower) |
| [BlockingQueue](./blockingqueue.md) | Concurrent producer/consumer |

## 4. Scenarios

- **Job queues:** BlockingQueue between API and workers.  
- **Single-thread pipeline:** ArrayDeque.  
- **Leaderboard drain:** PriorityQueue top-N.

## 5. Decision

Single thread → ArrayDeque. Multi-thread handoff → BlockingQueue. Priority → PriorityQueue (or external ranked store at scale).

## 6. Failure Scenario

Unbounded queue + slow consumers → OOM. Fix: bounded queue + rejection/backpressure.

## 7. Interview

- `offer` vs `add`?  
- Why not use Stack class?  
- **Principal:** design a job queue for 10K producers — bounds, metrics, poison pills?

### Related

[deque.md](./deque.md) · [arraydeque.md](./arraydeque.md) · [blockingqueue.md](./blockingqueue.md) · [priorityqueue.md](./priorityqueue.md)

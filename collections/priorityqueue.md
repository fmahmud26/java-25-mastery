# PriorityQueue

Binary **heap** `Queue` — head is least element (natural order or Comparator). Not bounded; not thread-safe.

## 1. Mental Model

```text
heap array: parent ≤ children (min-heap by default)
peek/poll → smallest
not a sorted full iteration order guarantee like TreeSet
```

## 2. Internals

| Topic | Behavior |
|-------|----------|
| Structure | Balanced binary heap in `Object[]` |
| Ordering | Comparator / Comparable |
| offer/poll | O(log n) |
| peek | O(1) |
| Iteration | **Not** sorted order |
| Concurrency | Not thread-safe (`PriorityBlockingQueue` for concurrent) |
| Memory | Compact array vs tree nodes |

## 3. Scenarios

- **Job priority** within a single worker.  
- **Leaderboard top-N:** keep size-N max-heap of lowest scores, etc.  
- At multi-node scale → external priority systems.

```java
PriorityQueue<Job> jobs = new PriorityQueue<>(Comparator.comparingInt(Job::priority));
jobs.offer(new Job(1, "bill"));
Job next = jobs.poll();
```

## 4. When PriorityQueue instead of TreeMap?

| Need | Choice |
|------|--------|
| Repeated take-min | **PriorityQueue** |
| Full sorted view / ranges | TreeMap |
| Concurrent priority handoff | PriorityBlockingQueue |

## 5. Failure Scenario

Assuming iterator is sorted → subtle bugs. Using PriorityQueue across threads without sync → corruption.

## 6. Interview

- Heap vs TreeMap?  
- Why isn’t iteration sorted?  
- **Principal:** priority job queue at scale — in-memory heap vs broker (Kafka/SQS)?

### Related

[queue.md](./queue.md) · [treemap.md](./treemap.md) · [blockingqueue.md](./blockingqueue.md)

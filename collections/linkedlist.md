# LinkedList

Doubly-linked nodes implementing `List` + `Deque`. Rarely the right default.

## 1. Mental Model

```text
null ← [A] ↔ [B] ↔ [C] → null
each node: prev, item, next
```

## 2. Internals

| Topic | Behavior |
|-------|----------|
| Structure | Node objects (pointer-heavy) |
| get(i) | Walk min(i, n-i) — O(n) |
| add/remove at ends | O(1) |
| add/remove mid | O(n) find + O(1) unlink |
| Memory | High per-element overhead; poor cache locality |
| Concurrency | Not thread-safe |
| Iteration | Fail-fast; ListIterator supports mid mutation |

## 3. Complexity

| Op | Cost |
|----|------|
| ends add/remove | O(1) |
| get(i) | O(n) |
| contains | O(n) |

## 4. Scenarios

- Rare: pipeline where you hold an iterator and splice often.  
- **Usually wrong** for job queues → [ArrayDeque](./arraydeque.md) / [BlockingQueue](./blockingqueue.md).

## 5. When LinkedList instead of ArrayList?

Almost never for List workloads. Prefer ArrayList (access) or ArrayDeque (ends). Interview myth: “insert mid is O(1)” — **finding** the mid is O(n).

## 6. Failure Scenario

Using LinkedList as default list → CPU + GC from node allocation; slower iteration. Fix: ArrayList; measure.

## 7. Interview

- Why is mid insert not free?  
- LinkedList vs ArrayDeque for queue?  
- **Principal:** when was the last time LinkedList was correct in your systems?

### Related

[arraylist.md](./arraylist.md) · [arraydeque.md](./arraydeque.md) · [deque.md](./deque.md)

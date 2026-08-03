# Deque

**Double-ended queue** — insert/remove at both head and tail. Also supports stack operations.

## 1. Mental Model

```text
first ◄──► deque ◄──► last
stack: push/pop at one end
queue: offer last / poll first
```

## 2. Contract

Extends Queue. Methods: `addFirst`/`addLast`, `offerFirst`/`offerLast`, `pollFirst`/`pollLast`, `peekFirst`/`peekLast`, `push`/`pop`.

## 3. Implementations

| Impl | Notes |
|------|-------|
| [ArrayDeque](./arraydeque.md) | Default — circular array |
| [LinkedList](./linkedlist.md) | Linked nodes; usually worse locality |
| Concurrent | `ConcurrentLinkedDeque`, blocking variants |

## 4. Scenarios

- **Undo stacks** in editors/services: ArrayDeque as stack.  
- **Work stealing prep / local queues:** ArrayDeque.  
- **Sliding window rate limit:** deque of timestamps.

## 5. Decision: ArrayDeque vs LinkedList

Prefer **ArrayDeque** for deque/stack/queue unless you need List mid-index ops (you usually don’t).

## 6. Failure Scenario

Using `Stack` (synchronized, legacy) → prefer ArrayDeque.

## 7. Interview

- Deque vs Queue?  
- Why ArrayDeque over Stack?  
- **Staff:** circular buffer growth behavior?

### Related

[queue.md](./queue.md) · [arraydeque.md](./arraydeque.md) · [linkedlist.md](./linkedlist.md)

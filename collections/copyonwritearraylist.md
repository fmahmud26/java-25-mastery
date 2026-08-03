# CopyOnWriteArrayList

Thread-safe `List` that copies the underlying array on **each mutation** — iterators see a snapshot.

## 1. Mental Model

```text
reads → cheap, no lock (volatile array)
writes → clone array + publish
```

## 2. Internals

| Topic | Behavior |
|-------|----------|
| Structure | Volatile `Object[]` |
| Write | Synchronized copy-on-write |
| Iteration | Snapshot — never CME from concurrent writes |
| Memory | Write allocates full new array; old arrays until iterators/GC release |
| Concurrency | Safe; optimized for **read-heavy, write-rare** |

## 3. Complexity

get O(1); add O(n) copy; iteration O(n) on snapshot.

## 4. Scenarios

- **Listener / observer lists** (many notifies, rare register).  
- Rarely mutating config lists read on every request.  
- **Wrong** for high-churn job queues or order lines under write load.

```java
CopyOnWriteArrayList<PaymentListener> listeners = new CopyOnWriteArrayList<>();
listeners.add(ledger::onPaid);
for (var listener : listeners) {
    listener.onPaid(event); // snapshot
}
```

## 5. When COW instead of CHM / synchronized List?

| Need | Choice |
|------|--------|
| Read-mostly list iteration | **COW** |
| Concurrent map | CHM |
| Frequent writes | Avoid COW — use other concurrent structures |

## 6. Failure Scenario

Register/unregister listeners at high rate → GC thrash / CPU on copies. Fix: different structure or batch updates.

## 7. Interview

- Why iterators don’t fail-fast?  
- Cost of add?  
- **Principal:** COW for product catalog — good idea? (Usually no — immutable snapshot publish better.)

### Related

[arraylist.md](./arraylist.md) · [concurrenthashmap.md](./concurrenthashmap.md) · [decision-matrix.md](./decision-matrix.md)

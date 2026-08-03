# ArrayList

Resizable **array**-backed `List`. Default list on Java 25.

## 1. Mental Model

```text
elementData[0..capacity)
size = logical length
get(i) → direct index
add mid → shift tail right
```

## 2. Internals

| Topic | Behavior |
|-------|----------|
| Data structure | `Object[] elementData` |
| Growth | ~1.5× when full (OpenJDK); allocate new array + copy |
| Random access | `RandomAccess` — O(1) get/set |
| Iteration | Indexed or iterator; fail-fast |
| Memory | Contiguous refs — good locality; may waste spare capacity |
| Concurrency | **Not** thread-safe |

## 3. Complexity

| Op | Cost |
|----|------|
| `get`/`set` | O(1) |
| `add` (end) | Amortized O(1) |
| `add`/`remove` (index) | O(n) shifts |
| `contains` | O(n) |

## 4. Code

```java
List<OrderLine> lines = new ArrayList<>(cart.size());
lines.addAll(cart.lines());
OrderLine first = lines.get(0);
```

Use `ArrayList.newArrayList(n)` / constructor capacity when size known (`ensureCapacity`).

## 5. Scenarios

- **Order lines / invoice items** — preserve order, random access.  
- **Product catalog page results** — materialize then sort.  
- Prefer Map for customer lookup — not list scan.

## 6. When ArrayList instead of LinkedList / ArrayDeque

| Choose ArrayList when | Choose other when |
|-----------------------|-------------------|
| Index access, default list | Ends-only → ArrayDeque |
| Mostly append | COW for concurrent readers |
| Tight loops over data | Mid-insert via iterator (rare) → measure LinkedList |

## 7. Failure Scenario

| | |
|--|--|
| Symptom | Latency spikes / GC |
| Cause | Repeated growth from tiny default; huge lists on heap |
| Fix | Size hint; stream/pagination; bound sizes |
| Prevent | Metrics on list sizes in hot paths |

## 8. Interview (Senior → Principal)

- Growth strategy and amortized add?  
- Why faster than LinkedList for iteration?  
- **Staff:** `subList` semantics and CME?  
- **Principal:** returning live ArrayList from a service vs defensive copy?

### Related

[linkedlist.md](./linkedlist.md) · [list.md](./list.md) · [decision-matrix.md](./decision-matrix.md)

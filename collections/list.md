# List

Ordered collection with **positional access** and duplicates allowed.

## 1. Mental Model

```text
index:  0    1    2    3
value: A1   A2   A3   A4     ← duplicates OK
```

## 2. Contract Highlights

| Op | Meaning |
|----|---------|
| `get`/`set`/`add(i,e)`/`remove(i)` | Index-based |
| `indexOf` / `lastIndexOf` | Search |
| `subList` | View (careful structural mods) |
| `List.of` / `ArrayList` | Immutable literal vs mutable |

## 3. Implementations

| Impl | Strength |
|------|----------|
| [ArrayList](./arraylist.md) | Default — array, random access |
| [LinkedList](./linkedlist.md) | Deque + rare mid inserts |
| [CopyOnWriteArrayList](./copyonwritearraylist.md) | Snapshot iteration, rare writes |
| Vector | Legacy synchronized |

## 4. Complexity Snapshot

Depends on impl — never assume O(1) mid-insert for all Lists.

## 5. Scenarios

- **Order lines:** ArrayList preserves line order.  
- **Product images:** ArrayList of URLs.  
- **Listeners:** COW list when iterated often, mutated rarely.

## 6. Decision: ArrayList vs LinkedList vs ArrayDeque

See [decision-matrix.md](./decision-matrix.md). **Default ArrayList.** Use ArrayDeque for pure ends; LinkedList almost never for List-only workloads.

## 7. Failure Scenario

`subList` + structural modify of parent → `ConcurrentModificationException`. Treat subList as a view with rules.

## 8. Interview

- Why ArrayList over LinkedList by default?  
- What is `RandomAccess`?  
- **Staff:** memory layout and cache locality impact?  
- **Principal:** when expose `List` vs immutable snapshot in a public API?

### Related

[arraylist.md](./arraylist.md) · [linkedlist.md](./linkedlist.md) · [collection.md](./collection.md)

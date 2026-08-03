# Collection Hierarchy

Root contracts for grouping elements — **Map is separate**.

## 1. Mental Model

```text
Iterable
└── Collection          // group of elements
    ├── List            // ordered, index, duplicates
    ├── Set             // unique
    └── Queue / Deque   // handoff / ends

Map                     // key → value (not a Collection)
```

## 2. Why the Split Exists

Lists need indexes and duplicates; sets need uniqueness; queues need handoff semantics; maps need key lookup. One mega-type would force meaningless methods (`get(index)` on a Set).

## 3. Core Contracts

| Type | Allows dupes | Order | Key access |
|------|--------------|-------|------------|
| List | Yes | Positional | Index |
| Set | No | Hash / insertion / sorted | Element as key |
| Queue | Impl-defined | Encounter / priority | Head |
| Map | Keys unique | Impl-defined | Key |

## 4. Internals You Must Remember

- Most non-concurrent collections are **fail-fast** iterators ([fail-fast-iterators.md](./fail-fast-iterators.md)).  
- Optional operations: immutable collections throw UOE on mutators.  
- `hashCode`/`equals` on content for many implementations — careful with nested mutability.

## 5. Realistic Scenarios

- **Catalog:** `Collection<Product>` only for bulk; lookup via `Map`.  
- **Job batch:** `Queue<Job>` for workers.  
- **Session ids:** `Set<String>` for uniqueness checks (bounded!).

## 6. Decision Notes

Program to `List`/`Set`/`Map`/`Deque` interfaces; pick implementations from [decision-matrix.md](./decision-matrix.md).

## 7. Failure Scenario

Symptom: `UnsupportedOperationException` on `add`. Cause: `List.of` / unmodifiable view. Fix: mutable copy when mutation required.

## 8. Interview (Senior → Principal)

- Why isn’t `Map` a `Collection`?  
- What does “optional operation” mean?  
- How do you choose interface vs concrete type in APIs?  
- **Principal:** When do you expose `Collection` vs a domain type (`OrderLines`)?

### Related

[list.md](./list.md) · [set.md](./set.md) · [map.md](./map.md) · [queue.md](./queue.md) · [deque.md](./deque.md) · [decision-matrix.md](./decision-matrix.md)

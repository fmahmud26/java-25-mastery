# TreeSet

`NavigableSet` backed by **TreeMap** — unique sorted elements.

## 1. Mental Model

```text
TreeSet ≈ TreeMap<E, PRESENT> with ordering
```

## 2. Internals

Same tree mechanics as TreeMap. O(log n) add/contains/remove. No hashing. Iteration sorted. Not concurrent.

## 3. Scenarios

- Sorted unique SKUs for catalog admin tools.  
- Sliding window of ordered timestamps (often ArrayDeque better for simple windows).

## 4. When TreeSet instead of HashSet?

Need sorted order or range (`subSet`) → TreeSet. Else HashSet (faster average).

## 5. Failure Scenario

Mutable elements that change comparison key after insert → tree corruption / lost elements.

## 6. Interview

- TreeSet vs HashSet?  
- How is TreeSet implemented?  
- **Staff:** ConcurrentSkipListSet vs synchronizing TreeSet?

### Related

[treemap.md](./treemap.md) · [hashset.md](./hashset.md) · [comparable.md](./comparable.md)

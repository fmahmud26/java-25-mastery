# LinkedHashSet

`Set` with **insertion-order** iteration — backed by LinkedHashMap.

## 1. Mental Model

```text
unique elements + stable encounter order
```

## 2. Internals

HashMap-like membership + linked list for order. Average O(1) ops. Not concurrent.

## 3. Scenarios

- Product **tags** displayed in the order added.  
- Deduping while preserving first-seen order in an import.

## 4. Decision

| Need | Choice |
|------|--------|
| Unique fast | HashSet |
| Unique + insertion order | **LinkedHashSet** |
| Sorted | TreeSet |

## 5. Failure Scenario

Same mutable-element hazard as HashSet.

## 6. Interview

- LinkedHashSet vs HashSet vs TreeSet?  
- Implementation?

### Related

[hashset.md](./hashset.md) · [linkedhashmap.md](./linkedhashmap.md)

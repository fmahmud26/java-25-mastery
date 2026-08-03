# Streams — Coding

Patterns that show up constantly:

| Problem | Stream approach |
|---------|-----------------|
| Filter + transform list | `filter` → `map` → `toList` |
| Frequency map | `groupingBy(fn, counting())` |
| Flatten nested lists | `flatMap(List::stream)` |
| Top-N after score | `sorted` → `limit` |
| Partition pass/fail | `partitioningBy` |

```java
// Word frequency
Map<String, Long> freq(List<String> words) {
    return words.stream()
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
}

// Flatten
List<Integer> flat(List<List<Integer>> nested) {
    return nested.stream().flatMap(Collection::stream).toList();
}

// Safe parallel only if associative & pure
int sum = IntStream.rangeClosed(1, 1_000_000).parallel().sum();
```

**Talk track:** brute `for` is fine; streams win when the pipeline is declarative and intermediate laziness/short-circuit help. Mention purity (no side effects) in lambdas.

Practice: chapter terminals — [collect.md](../../stream-api/collect.md), [flatmap.md](../../stream-api/flatmap.md), [reduce.md](../../stream-api/reduce.md).

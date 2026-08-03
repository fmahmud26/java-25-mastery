# Streams — Implementation

Idiomatic Java 25 usage.

```java
var names = List.of("ada", "alan", "grace");

// Pipeline: filter → map → collect
List<String> upper = names.stream()
        .filter(s -> s.length() > 3)
        .map(String::toUpperCase)
        .toList();                      // unmodifiable (Java 16+)

Map<Integer, List<String>> byLen = names.stream()
        .collect(Collectors.groupingBy(String::length));

String joined = names.stream()
        .collect(Collectors.joining(", "));

// Short-circuit
boolean hasGrace = names.stream().anyMatch("grace"::equals);

// flatMap
List<String> chars = names.stream()
        .flatMap(s -> s.chars().mapToObj(c -> String.valueOf((char) c)))
        .toList();
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Filter/map/collect | sequential `stream()` |
| Group / partition | `Collectors.groupingBy` / `partitioningBy` |
| Mutable reduction | custom `Collector` or `collect(supplier, acc, comb)` |
| Primitive heavy math | `IntStream` / `LongStream` |
| Parallel CPU-bound, large, associative | `parallelStream()` **after** measuring |
| Early exit | `findFirst` / `anyMatch` / `limit` |

```java
// Prefer toList() over collect(toList()) when unmodifiable is fine
// Prefer Stream.toList() (J16+) over Collectors.toUnmodifiableList()
```

Related: [collect.md](../../stream-api/collect.md), [grouping-by.md](../../stream-api/grouping-by.md), [filter.md](../../stream-api/filter.md).

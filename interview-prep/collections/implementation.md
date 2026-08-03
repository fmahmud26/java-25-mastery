# Collections — Implementation

Idiomatic Java 25 usage (not internals).

```java
// Prefer interfaces on the left
Map<String, Integer> scores = HashMap.newHashMap(128);
scores.put("a", 1);
scores.merge("a", 1, Integer::sum);

List<String> names = new ArrayList<>();
names.add("Ada");

// Unmodifiable / immutable factories
var frozen = List.of("x", "y");
var copy = Map.copyOf(scores);

// Sequenced collections (encounter order APIs)
var linked = new LinkedHashMap<String, Integer>();
linked.putFirst("first", 0);
```

## Selection cheat sheet

| Need | Reach for |
|------|-----------|
| Fast key lookup | `HashMap` |
| Insertion / access order | `LinkedHashMap` |
| Sorted keys | `TreeMap` |
| Concurrent map | `ConcurrentHashMap` |
| Growable array list | `ArrayList` |
| Queue / stack-like | `ArrayDeque` |

Related: [hashmap.md](../../collections/hashmap.md), [arraylist.md](../../collections/arraylist.md).

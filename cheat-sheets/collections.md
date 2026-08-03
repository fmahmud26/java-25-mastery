# Collections — Cheat Sheet

**Sources:** [../collections/README.md](../collections/README.md) · complexity/decision material in that chapter · [java-interview-questions/collections](../java-interview-questions/collections/) · [experiments/hashmap-resize-cost](../experiments/hashmap-resize-cost/) · [experiments/concurrent-map-scalability](../experiments/concurrent-map-scalability/)

## Pick structure

| Need | Prefer | Avoid (usually) |
|------|--------|-----------------|
| Fast key→value | `HashMap` / `ConcurrentHashMap` | `Hashtable` |
| Insertion order | `LinkedHashMap` | |
| Sorted / range | `TreeMap` / `TreeSet` | |
| Random access list | `ArrayList` | `LinkedList` for general use |
| Queue ends | `ArrayDeque` | `LinkedList` as queue |
| Concurrent map | `CHM` atomic ops | sync `HashMap` wrapper for hot paths |

Hierarchy diagram: [collections README](../collections/README.md)

## Complexity (interview recall)

| Type | Get/Contains | Add (amortized) | Notes |
|------|--------------|-----------------|-------|
| ArrayList | O(1) index | O(1) amort. | Resize copies — pre-size |
| LinkedList | O(n) | O(1) ends | Poor locality |
| HashMap | O(1) avg | O(1) avg | Collisions / resize bursts |
| TreeMap | O(log n) | O(log n) | Comparator contract |
| CHM | O(1) avg | O(1) avg | Use `computeIfAbsent`, not check-then-act |

Depth: chapter internals docs under [collections/](../collections/)

## Hot failure modes (from bank + chapter)

| Symptom | Likely | Fix direction |
|---------|--------|----------------|
| Lost map entries | Mutable key / broken hashCode | Immutable keys |
| CPU in HashMap | Resize or bad hash | Pre-size; fix hashCode |
| Duplicate side effects | CHM check-then-act | `computeIfAbsent` / `putIfAbsent` |
| CME while iterating | Structural mod | `removeIf` / iterator remove |
| OOM buffering | Unbounded list/queue | Bound + backpressure |

## Interview one-liners

- “Amortized O(1) ≠ no pauses — resize still shows in profiles.”  
- “CHM is thread-safe per call; compound actions need atomic methods.”  

Speak full scenarios from [java-interview-questions/collections](../java-interview-questions/collections/).

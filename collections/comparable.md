# Comparable

Natural ordering via `compareTo` — used by `TreeMap`/`TreeSet`/`PriorityQueue` without explicit Comparator.

## Rules

Sign of compareTo must be consistent with equals for SortedSet/Map contracts (`(a.compareTo(b)==0) == a.equals(b)` recommended).

## Failure

Inconsistent ordering → TreeMap/Set undefined behavior / duplicates weirdness.

## Interview

- Comparable vs Comparator?  
- Why consistency with equals?

### Related

[comparator.md](./comparator.md) · [treemap.md](./treemap.md)

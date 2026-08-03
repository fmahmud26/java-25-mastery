# Comparator

External ordering — `Comparator.comparing(...)`, thenComparing, reverseOrder — Java 8+ style preferred.

## Production

Leaderboards: `Comparator.comparingInt(Score::value).reversed()`. Keep comparators pure and side-effect free.

## Interview

- When Comparator over Comparable?  
- How do you sort with nulls last?

### Related

[comparable.md](./comparable.md) · [priorityqueue.md](./priorityqueue.md) · [treemap.md](./treemap.md)

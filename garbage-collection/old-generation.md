# Old Generation

(**Tenured** / old regions.) Holds objects that survived young collections long enough — or were allocated as large/humongous per collector rules.

## Mental Model

```text
Short-lived request junk     → dies in young
Caches, sessions, singletons → live in old
```

## Technical Mechanism

- Collected less often than young; more **live data** ⇒ more work per collection.  
- G1: old is many regions; reclaimed incrementally via **mixed** collections after concurrent mark.  
- Concurrent collectors reclaim old concurrently with short STW checkpoints.  
- Full GC often scans/compacts old when incremental strategies fail ([full-gc.md](./full-gc.md)).

## Production Implications

| Signal | Meaning |
|--------|---------|
| Old occupancy after GC climbs | Growth of live set / leak / undersized heap |
| High promotion rate | Young sizing / allocation pattern / survivor overflow |
| Humongous regions (G1) | Large arrays; fragmentation risk |

## Interview / PE

Why is old collection more expensive? G1 mixed GC vs “major GC” vocabulary?

### Related

[young-generation.md](./young-generation.md) · [major-gc.md](./major-gc.md) · [g1-gc.md](./g1-gc.md)

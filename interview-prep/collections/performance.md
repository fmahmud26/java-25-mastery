# Collections — Performance

## Complexity (HashMap average)

| Op | Average | Worst (pathological) |
|----|---------|----------------------|
| get/put | O(1) | O(n) before treeify; O(log n) tree bin |
| resize | amortized across puts | O(n) pause on that put |

## Degradation causes

1. **Bad `hashCode`** — everything in few bins  
2. **Mutable keys** changed after insert — lost entries  
3. **Oversized maps** — memory, GC pressure, cache misses  
4. **Undersized + many resizes** — during bulk load  
5. **Wrong structure** — `contains` on `List` instead of `HashSet`  
6. **False sharing / contention** — many threads on one `HashMap` (not safe) or hot CHM keys  

## Measurement

- Allocation: JFR Object Allocation  
- CPU in `HashMap.get/put` / `TreeNode` — async-profiler / JFR  
- Size & resize rate — custom metrics on map size  

Related: [../../performance-engineering](../../performance-engineering/), [load-factor.md](../../collections/load-factor.md).

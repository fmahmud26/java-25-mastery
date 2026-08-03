# Streams — Performance

## Complexity intuition

| Op | Cost note |
|----|-----------|
| `filter`/`map` | O(n) fused per-element |
| `sorted` | O(n log n) + buffering |
| `distinct` | State (hash) — memory |
| `limit(k)` early | Can stop early (ordered sources carefully with parallel) |
| `collect(toList)` | O(n) + allocations |

## Degradation causes

1. **Auto-boxing** in `Stream<Integer>` vs `IntStream`  
2. **Stateful ops** early (`sorted` before `filter`)  
3. **`parallel()` on small N** or blocking IO  
4. **Side-effectful peeks** / shared mutability  
5. **Boxing collectors** where primitive summary stats suffice  
6. Multiple passes when one pipeline would do  

## When streams lose to a loop

| Prefer loop | Prefer stream |
|-------------|---------------|
| Hot micro-path, tight arrays | Business transforms, grouping |
| Complex early `break` with locals | Clear filter/map/collect |
| Need indexed access heavily | Declarative pipelines |

## Measurement

- JMH for microbenches (warmup matters — JIT)  
- JFR allocation for boxing / intermediate lists  
- Don’t trust “parallel is faster” without numbers  

Related: [stream-performance.md](../../stream-api/stream-performance.md), [../../performance-engineering](../../performance-engineering/).

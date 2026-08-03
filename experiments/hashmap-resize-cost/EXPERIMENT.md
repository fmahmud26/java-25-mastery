# Experiment: HashMap resize cost

## Hypothesis

Inserting `n` entries into a `HashMap` that repeatedly crosses capacity thresholds costs more wall time than inserting into a map **pre-sized** with `new HashMap<>(n)` (fewer rehash/resizes).

## Setup

- JDK 25, single-threaded inserts of `Integer → Integer`
- Compare default `HashMap` vs `HashMap<>(expected)`
- Warmup 3 runs; measure median of 5 timed runs (printed by program)
- This is a **teaching microbench**, not JMH — treat magnitudes as local only

## Code

```bash
./run.sh
./run.sh 2_000_000
```

Source: `src/HashMapResizeCost.java`

## Expected behavior

Pre-sized map should show **lower** median wall time and fewer (or zero) internal resizes for the same `n`. Directional only until you run.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: n=1000000 warmup=3 measured=5; default HashMap median=36.26ms (min=32.94 max=43.92); pre-sized HashMap(n) median=30.40ms (min=10.12 max=102.56)
- Production implication: Pre-sizing cut median insert time from 36.26ms to 30.40ms for 1M puts—size HashMap when final capacity is known.

```text
n=1000000 warmup=3 measured=5 (not JMH)
default HashMap: min=32.94ms median=36.26ms max=43.92ms
pre-sized HashMap(n): min=10.12ms median=30.40ms max=102.56ms
Compare medians only on this machine. Do not publish as universal benchmark.
```

## Explanation

`HashMap` grows when size exceeds `capacity * loadFactor` (default 0.75). Growth allocates a larger table and rehashes entries. Pre-sizing sets initial capacity so inserts stay in one table (approximately).

## Production implication

If you know cardinality (batch load, caches), size maps/collections. Resizes allocate and burn CPU on hot paths (request handling, deserialization).

## Interview takeaway

“HashMap isn’t free at scale — resize/rehash is O(n) bursts. Pre-size when `n` is known; measure before claiming a win.”

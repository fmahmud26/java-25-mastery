# Experiment: HashMap collision treeify

## Hypothesis

When many keys land in the same bin (pathological `hashCode`), `HashMap` degrades toward tree bins (after thresholds) rather than remaining a pure linked list — lookup cost grows slower than O(n) list walk for large collisions. A normal hash distribution stays fast.

## Setup

- JDK 25
- Keys with **constant** `hashCode()` vs normal `Integer` keys
- Measure `get` over existing keys after insert
- Teaching microbench only

## Code

```bash
./run.sh
./run.sh 50000
```

Source: `src/HashMapCollisionTreeify.java`

## Expected behavior

Pathological keys: inserts/gets **much slower** than well-distributed keys. Absolute times are machine-local.

## Observed behavior

- Environment: openjdk version "25.0.3" 2026-04-21; Linux 7.0.0-28-generic x86_64
- Date: 2026-08-03
- Key metrics: n=20000; pathological hash get×n median=1086.45ms (min=1075.88 max=1099.79); normal Integer get×n median=1.29ms (min=0.36 max=2.10)
- Production implication: Pathological hashing was ~842× slower than Integer keys (1086.45ms vs 1.29ms)—implement proper hashCode/equals for map keys under load.

```text
n=20000 (teaching microbench, not JMH)
pathological hash get×n: median=1086.45ms (min=1075.88 max=1099.79)
normal Integer get×n: median=1.29ms (min=0.36 max=2.10)
```

## Explanation

Java 8+ treeifies bins when collisions exceed a threshold (and untreeifies when shrinking). Pathological hashing still hurts; it is not free. Don’t rely on treeification as an excuse for bad `hashCode`.

## Production implication

Bad `hashCode`/`equals` → CPU spikes and latency under load. Include keys in hashCode; watch for DoS via collision floods on untrusted map keys (use perfect hashing / limits at boundaries).

## Interview takeaway

“I can explain HashMap bins, treeify threshold, and why hash quality matters more than ‘HashMap is O(1)’ slogans.”

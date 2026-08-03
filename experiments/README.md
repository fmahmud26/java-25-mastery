# Experiments — Executable Java 25 Labs

Each lab is a **small program you run**, then fill **Observed behavior** from *your* output.  

**Never** treat README prose or guessed timings as benchmarks. Numbers only count after a local run (state JDK, OS, flags, warmup).

## Template (every experiment)

```text
Hypothesis → Setup → Code → Expected behavior
→ Observed behavior → Explanation → Production implication → Interview takeaway
```

## How to run

```bash
cd experiments/<name>
chmod +x run.sh
./run.sh
```

Requires **JDK 25**. Optional flags are printed by each main.

## Index

| Experiment | Topic |
|------------|--------|
| [hashmap-resize-cost](./hashmap-resize-cost/) | HashMap |
| [hashmap-collision-treeify](./hashmap-collision-treeify/) | HashMap |
| [arraylist-amortized-growth](./arraylist-amortized-growth/) | Collections |
| [concurrent-map-scalability](./concurrent-map-scalability/) | Collections |
| [allocation-rate-pressure](./allocation-rate-pressure/) | Memory / GC |
| [escape-or-not-demo](./escape-or-not-demo/) | Memory / JIT |
| [jit-warmup-cold-vs-hot](./jit-warmup-cold-vs-hot/) | JVM / JIT |
| [platform-thread-footprint](./platform-thread-footprint/) | Threads |
| [virtual-vs-platform-blocking](./virtual-vs-platform-blocking/) | Virtual Threads |
| [lock-contention-modes](./lock-contention-modes/) | Locks |
| [atomic-vs-synchronized-counter](./atomic-vs-synchronized-counter/) | Atomics |
| [completablefuture-fanout](./completablefuture-fanout/) | CompletableFuture |
| [stream-lazy-shortcircuit](./stream-lazy-shortcircuit/) | Streams |
| [parallel-stream-when-slower](./parallel-stream-when-slower/) | Parallel Streams |
| [io-buffered-vs-unbuffered](./io-buffered-vs-unbuffered/) | I/O |
| [nanotime-measurement-pitfalls](./nanotime-measurement-pitfalls/) | Performance |

## Measurement rules

1. Warm up before “measured” iterations when studying JIT.  
2. Report **min / median / spreads**, not a single heroic run.  
3. Say what you measured (wall time, ops/s, bytes) and what you did **not** (e.g. JMH, JFR).  
4. Qualitative Expected ≠ Observed numbers — Expected may be directional only.

**Filled local evidence (JDK 25):** [EVIDENCE.md](./EVIDENCE.md) · PE closed loop: [../principal-engineer/portfolio/closed-loop-allocation-gc.md](../principal-engineer/portfolio/closed-loop-allocation-gc.md)

Old stub markdown files at the folder root were replaced by these runnable labs.

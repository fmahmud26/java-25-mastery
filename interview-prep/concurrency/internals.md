# Concurrency — Internals

Focus: **monitors, volatile/HB, CHM, executors** (whiteboard depth).

```text
Thread A: write fields → unlock(m)  ──HB──►  Thread B: lock(m) → read fields
Thread A: write data; volatile write ready  ──HB──►  Thread B: volatile read ready; read data
```

## Must-explain pieces

| Piece | Point |
|-------|-------|
| Intrinsic lock | Per-object monitor; `synchronized` enter/exit; reentrant |
| Biased/thin/fat (HotSpot) | Lock inflation under contention — know it exists, don’t recite every state |
| `volatile` | Store-load barriers / forbid certain reorderings; **not** mutex |
| Happens-before | Transitive; without an edge, “stale” reads are legal |
| CAS | Compare-and-swap; retry loops underlie atomics & CHM |
| CHM | Bin/CAS + fine-grained locking — **not** one giant map lock; no nulls |
| Thread pool | Queue + workers; rejection policy; sizing ≠ “more threads always faster” |

## ConcurrentHashMap sketch

```text
key → hash → bin
  → CAS insert empty bin
  → lock bin / tree for contended updates
  → weakly consistent iterators (no CME)
```

Compound actions: use `compute` / `merge` / `putIfAbsent` — don’t DIY check-then-put races.

## Deadlock internals (diagnosis shape)

Circular wait on monitors → thread dump shows `BLOCKED` + “FOUND JAVA-LEVEL DEADLOCK”.

Related: [monitor.md](../../concurrency/monitor.md), [cas.md](../../concurrency/cas.md), [concurrenthashmap.md](../../concurrency/concurrenthashmap.md), [visibility.md](../../concurrency/visibility.md).

# Synchronization

Coordination so shared mutable state stays correct: exclusion, visibility, ordering.

## Mental Model

```text
Tools: synchronized / locks / volatile / atomics / concurrent collections
Goal: preserve invariants under concurrent access
```

## Internal Mechanics

Sync creates happens-before edges and often mutual exclusion. Pick the weakest tool that protects the invariant.

## Code

```java
// Exclusion + visibility
synchronized (lock) { /* critical */ }

// Visibility of one flag
volatile boolean ready;

// Atomic counter
AtomicLong hits = new AtomicLong();
```

## Production Scenario — inventory

Single JVM reservation map → CHM compute; multi-node → DB transaction / Redis — sync alone isn’t enough.

## Failure Scenario

Protecting reads with sync but writing unsynchronized (or vice versa).

## Debugging Strategy

Dump BLOCKED on monitors; review both read and write paths.

## Performance

Coarse locks → contention. Split (stripe) locks; prefer concurrent collections.

## Trade-offs

Correctness vs throughput; complexity of lock-free vs clarity of synchronized.

## Interview Questions

- What does synchronization guarantee?  
- Can you sync only writes?

## Principal-Level Discussion

Synchronization strategy is part of domain design (per-aggregate locks, sharding). Avoid global locks in payment hot paths.

### Related

[synchronized.md](./synchronized.md) · [java-memory-model.md](./java-memory-model.md) · [contention.md](./contention.md)

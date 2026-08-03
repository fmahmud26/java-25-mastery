# Ordering

Program order within a thread vs what other threads may observe; CPU/compiler reorderings constrained by HB.

## Mental Model

```text
In-thread: as-if-sequential for single thread semantics
Across threads: reordering allowed unless HB forbids it
```

## Internal Mechanics

Data-dependency within a thread preserved for that thread’s view. Synchronization order for volatiles/monitors. `volatile` prevents certain reorderings around the volatile access.

## Code

```java
// Writer
config = new Config(...); // publish object
ready = true;             // must be volatile (or sync) so config init can't float after

// Reader
if (ready) use(config);
```

## Production Scenario — high-traffic APIs

Safe publication of immutable response templates / feature flags.

## Failure Scenario

Seeing default field values in “constructed” object due to unsafe publication.

## Debugging Strategy

Stress + JCStress-style thinking; code review for publication.

## Performance

Memory barriers cost; don’t volatile every field.

## Trade-offs

Immutability + safe publication vs mutable + locks.

## Interview Questions

- What reorderings can break DCL?  
- Does synchronized prevent all reorderings globally?

## Principal-Level Discussion

Prefer immutable value publish (`volatile` ref / CHM) over complex ordering arguments in app code.

### Related

[happens-before.md](./happens-before.md) · [java-memory-model.md](./java-memory-model.md) · [volatile.md](./volatile.md)

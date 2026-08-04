# Ordering

Program order within a thread vs what other threads may observe; CPU/compiler reorderings constrained by happens-before.

## Mental Model

```text
In-thread: as-if-sequential for single thread semantics
Across threads: reordering allowed unless HB forbids it
```

## Internal Mechanics

- Compilers reorder for registers/CSE; CPUs reorder for store buffers / speculation.  
- Data dependencies within a thread preserved for **that** thread’s view.  
- Synchronization order for volatiles/monitors; acquire/release semantics (including via VarHandle) constrain reorderings around those ops.  
- Plain reads/writes may appear out of order to other threads.

## Code

```java
// Writer
config = new Config(...); // init object
ready = true;             // must be volatile (or sync) so config init can't float after ready

// Reader
if (ready) use(config);
```

With plain `ready`, a reader can see `ready == true` and still observe default fields in `config`.

## Production Scenario — high-traffic APIs

Safe publication of immutable response templates / feature flags via `volatile` ref or concurrent map.

## Production Scenario — final fields

Immutable `record` / final-field objects rely on initialization safety **plus** safe publication of the reference.

## Failure Scenario

Seeing default field values in a “constructed” object due to unsafe publication (classic DCL without volatile).

## Debugging Strategy

Stress + JCStress-style thinking; code review for publication edges. Ordinary tests pass on x86 more often than on weaker models — still fix the model, don’t rely on hardware kindness.

## Performance

Memory barriers cost; don’t volatile every field. Prefer immutability + one publication edge.

## Trade-offs

Immutability + safe publication vs mutable + locks. Acquire/release (VarHandle) vs full volatile when building concurrent structures.

## Interview Questions

- What reorderings can break DCL?  
- Does synchronized prevent all reorderings globally? (No — it creates HB with threads using the **same** monitor.)  
- Program order vs synchronization order?  

## Principal-Level Discussion

Prefer immutable value publish (`volatile` ref / CHM) over complex ordering arguments in app code. Leave litmus-level ordering to library authors with tests.

### Related

[happens-before.md](./happens-before.md) · [java-memory-model.md](./java-memory-model.md) · [volatile.md](./volatile.md) · [varhandles.md](./varhandles.md)

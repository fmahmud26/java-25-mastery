# Heap Sizing

`-Xms` / `-Xmx` are capacity controls — they do not fix leaks or magically cut pauses.

## Mental Model

```text
Live set (after GC) + headroom for allocation + collector needs  ≤  -Xmx
Container limit ≥ heap + metaspace + stacks + code cache + native + headroom
```

## Technical Mechanism

| Flag | Role |
|------|------|
| `-Xms` | Initial heap |
| `-Xmx` | Maximum heap |
| Equal Xms=Xmx | Avoids resize hiccups; common in production |
| Pause goals (G1) | Ergonomics may resize region roles within max |

## Sizing Practice

1. Measure **live set after GC** under peak realistic load.  
2. Add headroom for allocation spikes and concurrent GC (exact % is workload-specific — derive from tests).  
3. Watch promotion and old occupancy — climbing after-GC old gen ⇒ leak or undersized.  
4. Align cgroup memory with **total** RSS, not `-Xmx` alone.

## Anti-Patterns

| Anti-pattern | Why |
|--------------|-----|
| Tiny heap “to force GC” | Extra CPU, longer duty cycle, risk Full GC |
| Huge heap to hide leak | Expensive RAM; longer time to OOM; worse failure |
| Xmx == container limit | Native OOMKill with “heap free” |
| Copying flags from another service | Different live set / alloc rate |

## Interview / PE

How do you pick Xmx? Why Xms=Xmx? Relationship to container memory?

### Related

[allocation-rate.md](./allocation-rate.md) · [pause-time.md](./pause-time.md) · [incidents.md](./incidents.md)

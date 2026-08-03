# Full GC

A collection that (typically) processes the **entire heap**, often with a long stop-the-world pause. Treat frequent Full GCs as an **incident signal**.

## Mental Model

```text
Incremental / concurrent strategies failed to keep up
    → Full GC attempts to reclaim / compact broadly
    → Application stops longer than young/mixed pauses
```

## Common Causes (examples)

| Cause | Notes |
|-------|-------|
| Promotion / to-space failure | Can’t promote or evacuate |
| Concurrent mode / evacuation failure | Collector couldn’t finish in time |
| Heap fragmentation / humongous | Especially large objects (G1) |
| Explicit `System.gc()` | Often undesirable in servers |
| Extreme live set vs `-Xmx` | Need size or fix leak |
| Metaspace-related pressure | Separate domain; may induce GC activity |

Exact cause strings are collector-specific — always read the log reason.

## Production Implications

| Goal | Avoid relying on Full GC for steady state |
|------|-------------------------------------------|
| Signal | Investigate allocation rate, live set, sizing, humongous, leaks |
| Cost | Highest pause risk on G1/Parallel/Serial |

Low-pause collectors still have failure modes under extreme pressure — logs/JFR tell you.

## Interview / PE

Full vs young vs mixed? Why is Full GC a “smell”? When is `System.gc()` acceptable? (Rare: admin tooling, some caches — not request path.)

### Related

[stop-the-world.md](./stop-the-world.md) · [incidents.md](./incidents.md) · [g1-gc.md](./g1-gc.md)

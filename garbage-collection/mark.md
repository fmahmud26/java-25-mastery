# Mark

**Mark** phase: from **GC roots**, traverse references and mark reachable objects as live.

## Mental Model

```text
Roots (stacks, statics, JNI, …)
   ↓ follow references
marked = live
unmarked = candidates for reclaim
```

## Technical Mechanism

| Style | Notes |
|-------|-------|
| STW mark | Simple; mutators stopped |
| Concurrent mark | Mutators run; needs barriers / SATB / similar |
| Incremental | Mark in steps toward pause goals |

False negatives (missing live objects) are fatal; collectors err toward correctness with barriers and snapshots.

## Production Implications

Concurrent mark costs CPU and can race with mutator mutations — that is expected. Long concurrent cycles under heap pressure may lead to fallback Full GC.

### Related

[sweep.md](./sweep.md) · [compact.md](./compact.md) · [stop-the-world.md](./stop-the-world.md)

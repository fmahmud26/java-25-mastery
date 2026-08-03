# Stop-the-World (STW)

Phases where **application mutator threads are paused** so the JVM can safely perform GC (or related VM) work.

## Mental Model

```text
Mutators running
    → safepoint / STW handshake
    → GC does root scan / evacuate / compact step
    → mutators resume
```

Even “concurrent” collectors use **short** STW pauses. Concurrent ≠ zero pause.

## Technical Mechanism

- Threads reach a [safepoint](../jvm-internals/safepoints.md); time-to-safepoint can add latency before GC work starts.  
- Young evacuate, remark, cleanup, and full compact are classic STW examples (set varies by collector).  
- Concurrent mark/relocate runs *beside* mutators with read/write barriers — CPU cost, not always STW.

## Production Implications

| Measure | Why |
|---------|-----|
| Pause time (GC) | Mutator stopped for GC |
| Time-to-safepoint | Hidden latency before pause work |
| Concurrent GC CPU | Not STW but steals cores |

A “3-second latency spike” might be STW GC, safepoint lag, or *not GC at all* (lock, I/O, CPU). Prove with logs/JFR ([incidents.md](./incidents.md)).

## Trade-offs

| Collector style | STW character (qualitative) |
|-----------------|----------------------------|
| Serial / Parallel | Longer STW collections for throughput simplicity |
| G1 | Incremental STW young/mixed; aims for pause *goals* |
| ZGC / Shenandoah | Designed for very short STW; more concurrent work |

Do not quote marketing pause numbers as guarantees — measure your build and workload.

## Interview / PE

Define STW. Do concurrent collectors eliminate STW? Safepoint vs GC pause?

### Related

[pause-time.md](./pause-time.md) · [gc-fundamentals.md](./gc-fundamentals.md) · [diagnostics.md](./diagnostics.md)

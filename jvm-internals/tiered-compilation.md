# Tiered Compilation

Default HotSpot policy: **blend interpreter + C1 + C2** so startup stays reasonable while peak speed still arrives.

## Mental Model

```text
Tier 0: Interpreter
Tier 1–3: C1 with varying profiling
Tier 4: C2 (full optimization)
```

(Exact tier numbers are HotSpot policy details — interview answer: interpret → quick C1 → profiled C1 → C2.)

## Technical Mechanism

```text
cold  → interpret
warm  → C1 (cheap native)
hotter → C1 with more profiling
hottest → C2
```

OSR (on-stack replacement): a long-running loop can replace the continuing execution with compiled code without waiting for the method to return.

## JVM Internals

- Tiered is **on by default** on modern HotSpot server builds.  
- `-XX:-TieredCompilation` → typically interpret then C2 (slower warm-up to peak).  
- `-XX:TieredStopAtLevel=1` → stay at simple C1 (used in some containers to reduce compiler CPU / code cache).  
- Compilation thresholds are adaptive.

## Production Implications

| Goal | Lever |
|------|-------|
| Faster peak, OK with compile CPU | Default tiered |
| Less compiler overhead in tiny containers | Stop at C1 / smaller code cache — measure |
| Debug “is JIT the bug?” | `-Xint` or disable tiered carefully |
| Stable benchmarks | Warm-up iterations; or AOT/Leyden where applicable |

## Incident — JIT behavior

Service “fast after 10 minutes”: tiered warm-up. Service “CPU high at start”: compiler threads. Don’t scale pods to 0 and expect instant C2 performance on cold start without warm-up or AOT strategy.

## Interview / PE

What problem does tiered solve? C1-only trade-off? What is OSR?

### Related

[jit-compiler.md](./jit-compiler.md) · [interpreter.md](./interpreter.md) · [deoptimization.md](./deoptimization.md)

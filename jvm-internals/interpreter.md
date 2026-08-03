# Interpreter

HotSpot’s **template interpreter** executes bytecode quickly enough to start, while collecting profiles for the JIT.

## Mental Model

```text
Cold code → interpret (flexible, profile)
Hot code  → JIT compile (fast)
```

## Technical Mechanism

```text
fetch bytecode → template stub → execute → advance PC → repeat
                 (+ method invocation counters, type profiles, branch data)
```

| Pros | Cons |
|------|------|
| Fast startup | Lower peak throughput |
| Easy deopt target | Hot loops hurt if never compiled |
| Rich profiling | Not for CPU-bound steady state |

## JVM Internals

- “Template” = generated asm stubs per bytecode, not a pure C switch loop only.  
- Profiles feed C1/C2: receiver types, branch frequencies, etc.  
- Methods may remain interpreted forever if cold — that’s fine.  
- `-Xint` forces interpreted-only (debug / isolate JIT issues).

## Production Implications

- Warm-up: early traffic is slower until compilation; don’t benchmark cold JVMs.  
- Sudden new code paths (rare branches) may interpret until hot — latency blips.  
- Compare with [tiered-compilation.md](./tiered-compilation.md).

## Incident — JIT behavior / latency

After deploy, p99 high for minutes then settles → classic warm-up. If never settles, compilation disabled / code cache full / forced `-Xint`.

## Interview / PE

Why interpret at all? What does the interpreter collect? When use `-Xint`?

### Related

[jit-compiler.md](./jit-compiler.md) · [tiered-compilation.md](./tiered-compilation.md) · [bytecode.md](./bytecode.md)

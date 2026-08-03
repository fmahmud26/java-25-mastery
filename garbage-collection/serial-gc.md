# Serial GC

Single-threaded collector. Young: copying; old: mark-compact. Stop-the-world.

```bash
java -XX:+UseSerialGC ...
```

## When It Fits

| Fit | Poor fit |
|-----|----------|
| Small heaps, single CPU, simple tools | Multi-core low-latency services |
| Minimal footprint priority | Large multi-threaded servers |

On Java 25, HotSpot may still select Serial by default in **constrained** environments if you specify no collector — pin explicitly when you care.

## Trade-offs

Simple and small; does not scale GC work across cores. Pauses can be long relative to concurrent collectors as live data grows.

### Related

[parallel-gc.md](./parallel-gc.md) · [g1-gc.md](./g1-gc.md) · [trade-offs.md](./trade-offs.md)

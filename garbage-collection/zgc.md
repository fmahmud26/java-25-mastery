# ZGC

Low-latency concurrent compacting collector. On **Java 25**, enable with `-XX:+UseZGC` — ZGC operates in **generational** mode (single-generation ZGC was removed in earlier JDKs).

```bash
java -XX:+UseZGC -Xms… -Xmx… …
```

## Mental Model

```text
Mutators keep running while ZGC marks and relocates
Short STW pauses for coordination — designed not to grow with heap size the way classic full compactors do
Load barriers / colored pointer machinery (implementation detail) keep refs consistent
```

## Technical Mechanism (interview-level)

- Concurrent mark and relocate with load barriers.  
- Generational design exploits young death rate for efficiency vs older single-gen ZGC.  
- Scales to very large heaps; also used on modest heaps when latency matters — **validate**.

## Production Implications

| Consider ZGC when | Tail latency / pause sensitivity is a primary SLO |
|-------------------|------------------------------------------------------|
| Measure | Pause times, throughput, CPU, footprint vs G1 on *your* load |
| Sizing | Give enough heap headroom; concurrent collectors need room to finish work |
| Do not claim | A universal pause number for all apps |

## Trade-offs

Expect different CPU and memory behavior than G1 — direction and magnitude are workload-specific. Use JFR/GC logs in a bakeoff; avoid blog %-overhead as policy.

## Interview / PE

How to enable on Java 25? Generational status? STW eliminated? (No — minimized.) When keep G1?

### Related

[shenandoah.md](./shenandoah.md) · [g1-gc.md](./g1-gc.md) · [trade-offs.md](./trade-offs.md) · [pause-time.md](./pause-time.md)

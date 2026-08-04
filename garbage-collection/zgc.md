# ZGC

Low-latency concurrent compacting collector. On **Java 25**, enable with `-XX:+UseZGC` — ZGC operates in **generational** mode (single-generation ZGC was removed in earlier JDKs).

```bash
java -XX:+UseZGC -Xms… -Xmx… …
```

## Mental Model

```text
Mutators keep running while ZGC marks and relocates
Short STW pauses for coordination — designed not to grow with heap size the way classic full compactors do
Load barriers / colored pointer machinery keep refs consistent while objects move
```

## Internals (interview depth)

| Idea | Meaning |
|------|---------|
| Colored pointers | Metadata in pointer bits (with remapping) so GC can track object state |
| Load barrier | On reference load, mutator may remap/fix stale view after relocation |
| Concurrent relocate | Live objects move while app runs; pauses stay short for roots/handshakes |
| Generational ZGC | Young collections exploit infant mortality for efficiency vs older single-gen ZGC |

You need the *picture*, not a bit-layout memorization. Barriers cost **CPU**; that is the classic trade vs G1’s longer occasional pauses.

## Production Implications

| Consider ZGC when | Tail latency / pause sensitivity is a primary SLO |
|-------------------|------------------------------------------------------|
| Measure | Application p99, throughput, CPU, footprint vs G1 on *your* load |
| Sizing | Enough heap headroom so concurrent work finishes; starving ZGC causes allocation stalls |
| Do not claim | A universal pause number for all apps |

## Production Scenario — ZGC wins

Heap multi-GB; G1 mixed/Full pauses breach p99 SLO; after switch to ZGC, GC pauses flatten; CPU +% acceptable; business p99 inside SLO.

## Production Scenario — ZGC loses

CPU-bound service already at 85% mutator CPU; ZGC barriers + concurrent threads steal the rest; **throughput** falls though pauses look pretty. Revert or add capacity; fix alloc first.

## When Not to Use

- Tiny heaps / batch jobs where Parallel/G1 throughput wins  
- No evidence that GC pauses are on the critical path  
- Team cannot operate a second collector’s failure modes  

## Failure Modes to Name

Allocation stall (GC not keeping up); OOM still possible if live set > heap; native/footprint differences vs G1 — measure RSS.

## Interview / PE

Generational on Java 25? (Yes — non-gen removed earlier.) Are pauses zero? (No — short STW remains.) How do load barriers relate to moving objects? When prefer G1?

### Related

[g1-gc.md](./g1-gc.md) · [shenandoah.md](./shenandoah.md) · [trade-offs.md](./trade-offs.md) · [pause-time.md](./pause-time.md) · [../performance-engineering/tail-latency.md](../performance-engineering/tail-latency.md)

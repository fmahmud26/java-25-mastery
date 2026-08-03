# Interview — Garbage Collection (Production)

Draw, define, then **measure**. Pair with [incidents.md](./incidents.md) and [trade-offs.md](./trade-offs.md).

---

## L1 — Fundamentals

**Why GC?** Reclaim unreachable objects; safety/productivity; costs CPU/pauses.

**Generational hypothesis?** Most objects die young → cheap young collections.

**STW?** Mutators paused; concurrent collectors still have short STW.

---

## L2 — Vocabulary

| Term | Meaning |
|------|---------|
| Minor / young | Young generation collection |
| Mixed (G1) | Young + selected old regions |
| Full | Whole-heap / fallback — smell if frequent |
| Allocation rate | Drives young GC frequency |
| Live set | Heap retained after GC |

---

## L3 — Collectors (Java 25)

**G1:** Regions; young; concurrent mark; mixed; pause *goal* via `MaxGCPauseMillis` (not a guarantee). Default on typical servers.

**ZGC:** `-XX:+UseZGC`; generational on Java 25; low-pause design — validate on workload.

**Shenandoah:** `-XX:+UseShenandoahGC`; generational mode product (JEP 521) via `ShenandoahGCMode=generational`; default mode still non-generational.

**Parallel vs Serial:** Throughput vs simple single-threaded.

---

## L4 — Incident narrative

“3s latency spike” answer shape:

1. Correlate timestamp with GC/safepoint logs and JFR  
2. Identify collection type / TTSP / non-GC cause  
3. If Full GC — find failure reason + live set/alloc  
4. Fix root cause; collector change only with bakeoff data  

---

## Rapid fire

| Q | A |
|---|---|
| Leak vs GC problem? | Rising after-GC live set vs collector can’t meet goals with stable expected live set |
| Humongous? | Large objects spanning G1 regions |
| Why not System.gc()? | Can force expensive collections; avoid on request path |
| Xms=Xmx? | Avoid resize; common prod practice |
| JFR vs GC log? | Timeline/detail vs always-on collection diary |

---

## Follow-ups

1. How bake off G1 vs ZGC responsibly?  
2. What does evacuation failure imply?  
3. How does allocation rate show up in logs?  
4. When is Parallel still the right call?  

**PE line:** Flags without allocation and live-set evidence are superstition.

### Related

[g1-gc.md](./g1-gc.md) · [diagnostics.md](./diagnostics.md) · [README.md](./README.md)

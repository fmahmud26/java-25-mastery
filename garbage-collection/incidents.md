# Production Incidents (GC)

Each case: symptoms → evidence → likely causes → actions. **No guaranteed pause numbers** — correlate on your system.

---

## 1) 3-Second Latency Spikes

**Symptoms:** p99/p999 spikes ~seconds; median may look fine.

**Evidence:**
- GC logs: pause ≈ spike duration? Young vs mixed vs **Full**?
- Safepoint logs: long time-to-safepoint?
- JFR: GC pause vs lock contention vs I/O in the same second

**Likely causes:** Full GC / long mixed; safepoint lag; *or not GC* (stop-the-world elsewhere).

**Actions:** Fix trigger (alloc, humongous, leak, `System.gc`); sizing; collector bakeoff only after proof; eliminate uncounted tight loops if TTSP.

---

## 2) High Allocation Rate

**Symptoms:** Very frequent young GCs; GC CPU elevated; alloc profiles hot.

**Evidence:** Young GC cadence; JFR allocation; async-profiler alloc flame graph.

**Likely causes:** Per-request buffers, boxing, deserializing whole payloads, logging/json churn.

**Actions:** Reduce churn at hot sites; reuse buffers carefully; size request limits. Collector change alone rarely fixes true alloc storms.

---

## 3) Frequent GC

**Symptoms:** Back-to-back young collections; possible app throughput drop.

**Evidence:** Logs show interval; Eden size vs alloc rate; pause *duration* still small?

**Split:**
| Frequent + short pauses + stable live set | Often OK / allocate less if CPU matters |
| Frequent + rising old / Full GC | Pressure — size or leak or promotion |

**Actions:** Confirm which bucket; then alloc fix or heap/live-set work.

---

## 4) Heap Pressure

**Symptoms:** Occupancy high; concurrent cycles struggle; allocation slow / failures; OOM risk.

**Evidence:** `GC.heap_info`; after-GC occupancy trend; promotion; humongous count (G1).

**Likely causes:** Live set too large for `-Xmx`; leak; cache; oversized heap consumers.

**Actions:** Dump if live set unexpected; otherwise right-size or reduce retained data. See memory-management investigation pattern.

---

## 5) Long Pauses

**Symptoms:** Multi-hundred-ms or multi-second STW; user-visible stalls.

**Evidence:** GC log pause lines; JFR GC phase; which collection type.

**Likely causes:** Full GC; large young survivor set; Parallel/Serial on big live heap; evacuation failure path.

**Actions:** Remove Full GC causes; review heap sizing; consider latency-oriented collector **after** measuring G1 baseline; never tune 20 flags blind.

---

## Investigation Card

```text
1. Timeline match: latency ↔ GC log ↔ JFR
2. Collection type + pause + heap before/after
3. Live set after GC flat or rising?
4. Allocation rate / promotion / humongous?
5. One change; re-measure
```

### Related

[diagnostics.md](./diagnostics.md) · [trade-offs.md](./trade-offs.md) · [full-gc.md](./full-gc.md) · [heap-sizing.md](./heap-sizing.md)

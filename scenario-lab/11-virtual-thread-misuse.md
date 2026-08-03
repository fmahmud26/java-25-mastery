# Virtual Thread Misuse — “we switched to VT” and got worse

## Incident

A batch fan-out service migrated platform threads to virtual threads expecting higher throughput. After release: CPU pegged, latency worse, throughput flat or down. Code performs heavy JSON parsing on VTs and holds a monitor across a large in-memory index rebuild. Engineers say “VT should be faster.” Interviewers will punish that slogan.

## Symptoms

- Throughput unchanged or lower vs platform pool of 200
- High CPU (parsing / hashing), not blocking I/O waits
- Carriers busy; run queues long
- Optional: JFR residual pinning (native / file / class-init) — **not** required to explain JDK 25 regressions from CPU oversubscription
- DB pool not the bottleneck this time

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** (JEP 491: `synchronized` no longer pins the old way) |
| Workload | Mix: 30% blocking I/O, 70% CPU JSON + long critical section around index (illustrative) |
| Before | `FixedThreadPool(200)` |
| After | `Executors.newVirtualThreadPerTaskExecutor()` per request fan-out |

## Metrics

```
throughput.rps               4.2k → 3.9k
cpu                          55% → 98%
carriers_busy                ≈ available cores
time.in.critical_section     elevated
db.pool.wait                 low
```

Illustrative: CPU-bound oversubscription ⇒ VT cannot invent cores.

## Logs

```
2026-08-03T08:00:01.002Z INFO  Fanout - migrated to virtualThreadPerTaskExecutor
2026-08-03T08:05:12.440Z INFO  metrics - cpu=98% carriers_busy=cores
2026-08-03T08:06:00.000Z INFO  metrics - vt_submitted unbounded fan-out
```

## Initial Hypotheses

1. Work is CPU-bound — VT adds concurrency, not cores
2. Unbounded fan-out causes cache/CPU thrash
3. Lock held across heavy work worsens latency (design) even when not pinning
4. Residual pinning (JNI/file) — check only if JFR shows `VirtualThreadPinned`
5. Measurement error (warmup, GC, different load)

## Questions

- When are virtual threads the right tool? When not? ([virtual-threads/](../virtual-threads/), [experiments/virtual-vs-platform-blocking/](../experiments/virtual-vs-platform-blocking/))
- What changed with **JEP 491** for `synchronized` pinning on Java 24+/25?
- What would you profile first: CPU flame graph or pinning events?
- What if traffic ×100 with the same CPU work — what breaks first?
- Why is the VT scheduler **not** `ForkJoinPool.commonPool()`?

## Investigation

1. **Clarify workload**  
   Blocking I/O fraction vs CPU. If CPU-dominant, VT won’t help throughput.

2. **CPU profile first**  
   async-profiler / JFR method profile: JSON parse, crypto, regex ([performance-engineering/tools/java-flight-recorder.md](../performance-engineering/tools/java-flight-recorder.md)).

3. **Pinning only if indicated**  
   JFR `jdk.VirtualThreadPinned` — expect residual causes (native/file/class-init), not “synchronized pins” lore on JDK 25.

4. **Concurrency cap**  
   Count concurrent tasks — unbounded VT storming CPU caches.

5. **A/B**  
   Same hardware: platform pool sized to cores for CPU section; VT only around blocking calls.

6. **Compare to principal card**  
   [principal-engineer/scenarios/virtual-threads-no-gain.md](../principal-engineer/scenarios/virtual-threads-no-gain.md).

7. **GC**  
   Extra allocations from excessive parallelism — secondary.

## Root Cause

Migration applied VT to a **CPU-heavy** fan-out (and held locks across heavy work). Virtual threads excel at scaling **blocking I/O** concurrency; they do not speed CPU-bound work and can worsen it via oversubscription. Expectation “VT = faster” was false. On Java 25, do **not** diagnose primarily as synchronized pinning (JEP 491).

## Resolution

- Split: VT for I/O; bounded platform pool for CPU stages.
- Keep critical sections short; never hold locks across I/O.
- Bound fan-out with a semaphore ≈ core count for CPU stages.
- Re-benchmark with JMH/load tests — no slogans.

## Prevention

- VT adoption checklist: I/O-bound? scarce pools sized? CPU stages bounded? residual pinning watched via JFR?
- Education: [experiments/virtual-vs-platform-blocking/](../experiments/virtual-vs-platform-blocking/)
- Architecture review for unbounded `newVirtualThreadPerTaskExecutor` usage

## Principal Engineer Discussion

- How do you roll out VT in a monolith safely?
- Version-aware pinning literacy (21 vs 24+/25).
- Structured concurrency (preview) as the API boundary for fan-out cancellation.
- Counter-slogan discipline: articulate **when VT loses** as clearly as when it wins.

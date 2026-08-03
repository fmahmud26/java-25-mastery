# Production Incidents (JVM Internals)

Realistic failure stories. Practice the **symptom → hypothesis → tool → fix** loop.

---

## 1) High CPU

**Symptoms:** CPU 90–100%; latency up; maybe few GC pauses.

**Hypotheses:**

| Cause | Clue |
|-------|------|
| App hot loop / contention | Same stacks in dumps; profiler in app frames |
| GC thrash | High GC CPU / allocation rate |
| JIT compilation storm | `C2 CompilerThread` hot; recent code load |
| Spin / livelock | Runnable threads, no progress |

**Diagnostics:** async-profiler CPU flame graph; `jcmd Thread.print` ×3; GC logs; compilation log.

**Fixes:** fix algorithmic hot path; reduce allocation; stop deopt loop; size heap; avoid compile storms from generated code.

---

## 2) Class-Loading Issue

**Symptoms:** `ClassNotFoundException` / `NoClassDefFoundError` / `LinkageError` / `ClassCastException: Foo cannot be cast to Foo`; or Metaspace climb.

**Hypotheses:** wrong classpath/module; TCCL; duplicate loaders; loader leak after redeploy.

**Diagnostics:** `-Xlog:class+load=info`; print `ClassLoader` for both types; `jcmd VM.metaspace`; heap dump → loader GC roots.

**Fixes:** align loaders; clear caches on undeploy; set TCCL; fix `jlink` modules.

---

## 3) Memory Pressure

**Symptoms:** RSS high / OOMKill; heap used high or *not*; GC frequent; container restart.

**Split brain:**

```text
Java heap OOM          → -Xmx / leak / live set
Metaspace OOM          → classloader leak / too many classes
Native / Direct OOM    → NMT, DirectByteBuffer, threads×stacks
cgroup kill, heap OK   → total footprint > limit
```

**Diagnostics:** `GC.heap_info`, heap dump, NMT, metaspace, thread count.

**Fixes:** right-size limits; fix leaks; bound caches; reduce platform threads; direct buffer caps.

---

## 4) JIT Behavior

**Symptoms:** Slow for N minutes after deploy then fine; or never reaches expected RPS; or CPU on compiler threads; code cache warnings.

**Hypotheses:** warm-up; `-Xint`; tiered stopped; code cache full; deopts; megamorphic sites.

**Diagnostics:** PrintCompilation / jit logs; CodeHeap analytics; JFR compilation events; compare cold vs warm latency.

**Fixes:** warm-up traffic; AOT/CDS/Leyden where appropriate; enlarge code cache; simplify call sites; don’t ship with `-Xint`.

---

## 5) Large Object Allocation

**Symptoms:** Sudden old-gen / humongous growth; long GC; allocation failures; p99 spikes on big requests.

**Hypotheses:** huge `byte[]`/`ByteBuffer` per request; loading entire files; unbounded JSON trees; G1 humongous fragmentation.

**Diagnostics:** JFR allocation profiling; heap histogram top classes; GC humongous logs (G1).

**Fixes:** stream processing; size caps; chunked I/O; reuse buffers carefully; reject oversized payloads.

---

## 6) Latency Spike

**Symptoms:** p99/p999 spike; median OK.

**Hypotheses:** GC pause; **safepoint TTSP**; deopt; class init on request path; lock contention; noisy neighbor CPU.

**Diagnostics:**

```bash
-Xlog:gc*,safepoint=info
JFR (GC + Safepoint + Java Monitor + Compilation)
```

**Fixes:** collector/tuning if GC; eliminate uncounted tight loops / bad native for TTSP; move class init off hot path; fix locks; isolate CPU.

---

## Investigation Card (print this)

```text
1. Timeline: deploy? traffic? flag?
2. CPU vs GC vs Compiler vs IO
3. Heap vs Metaspace vs Native
4. Safepoint duration vs GC pause
5. One reproducing artifact: JFR / dump / flame graph
```

### Related

[diagnostic-tools.md](./diagnostic-tools.md) · [safepoints.md](./safepoints.md) · [deoptimization.md](./deoptimization.md) · [heap.md](./heap.md)

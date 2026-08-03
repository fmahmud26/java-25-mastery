# Thread Starvation — async work never drains

## Incident

`report-builder` accepts “export CSV” jobs. Users report exports stuck in `QUEUED` for 30+ minutes while the UI shows workers “healthy.” HTTP health checks pass. A few exports complete slowly. Deploy yesterday switched a shared `ExecutorService` to a smaller pool “to reduce context switching,” and added a fan-out that calls back into the same executor.

## Symptoms

- Job queue depth climbs; consumer lag-like metric for exports rises
- Active worker threads sit at pool max; many tasks wait in queue
- Some threads blocked on `Future.get()` / `CompletableFuture.join()`
- CPU modest (20–40%); not a CPU saturation story
- Health endpoint returns 200 (does not check queue depth)

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| Pool | `Executors.newFixedThreadPool(8)` shared for “all async” (illustrative) |
| Job rate | ~40 exports/min peak (illustrative) |
| Pattern | Parent task submits N child tasks to **same** pool, then blocks waiting |
| HTTP | Platform threads (Tomcat); business work on shared pool |

## Metrics

```
executor.active          8/8
executor.queue           0 → 2 000+
export.job.age.p50       2s → 18min
cpu.usage                ~0.3
db.pool.active           low
thread.state.blocked      elevated (WAITING on futures)
```

Illustrative: pool saturated + queue growth + waits on internal futures ⇒ classic self-deadlock/starvation pattern risk.

## Logs

```
2026-08-03T11:02:11.004Z INFO  ExportJob - submitted jobId=E-4401 children=8
2026-08-03T11:02:11.020Z INFO  ExportJob - waiting for children jobId=E-4401
2026-08-03T11:05:44.118Z WARN  ExportJob - child still pending jobId=E-4401 child=7 elapsed=213s
2026-08-03T11:10:02.550Z ERROR ExportSupervisor - queueDepth=1872 oldestAgeSec=1402
2026-08-03T11:10:03.001Z INFO  Health - status=UP disk=ok db=ok
```

## Initial Hypotheses

1. Pool too small for offered work (simple capacity)
2. **Same-thread-pool deadlock**: parent blocks waiting for children that need the same pool
3. Downstream DB/HTTP blocking holds all workers
4. Lock contention / unfair locking inside export
5. Virtual-thread migration incomplete (mixed schedulers) — possible but check evidence

## Questions

- Why can health be green while the product is down for exports?
- What do three spaced thread dumps tell you that one does not?
- What assumption does `FixedThreadPool(8)` encode about task dependency structure?
- What if export rate ×100 — does a bigger pool alone save you?
- When would virtual threads help this design, and when would they hide a still-broken dependency graph? ([virtual-threads/](../virtual-threads/), [concurrency/](../concurrency/))

## Investigation

1. **Thread dumps ×3**  
   `jcmd <pid> Thread.print` every 5–10s ([performance-engineering/tools/jstack.md](../performance-engineering/tools/jstack.md)). Look for workers blocked in `Future.get` / `join` while queue is non-empty.

2. **Map pool vs tasks**  
   Identify thread names (`pool-1-thread-*`). Count how many hold parent frames vs child work. If all 8 are parents waiting, children cannot run → **starvation**.

3. **Executor metrics**  
   Active count, queue size, completed tasks. Correlate with job ages.

4. **Rule out external wait**  
   If stacks show JDBC/`socketRead`, it’s pool exhaustion from blocking I/O — different fix (see scenario 06). Here stacks point inward.

5. **JFR Thread Park / Java Monitor**  
   Confirm park duration on joins; no CPU hot method.

6. **Code path**  
   Read export orchestration: `supplyAsync(..., sameExecutor)` then `allOf(...).join()` on a worker from that executor.

7. **Compare to ForkJoin / VT**  
   Note FJP has compensation in some cases; unbounded VT would run children but can still overload DB — redesign still required. Related: [principal-engineer/scenarios/thread-explosion.md](../principal-engineer/scenarios/thread-explosion.md).

## Root Cause

Export parents run **on** the shared 8-thread pool and block waiting for child tasks scheduled on the **same** pool. Under load, all workers become waiting parents; children sit in the queue with no free thread → **thread-pool starvation** (a form of self-deadlock). Shrinking the pool yesterday reduced headroom and made the latent bug continuous.

## Resolution

- **Immediate:** drain by restart; pause new exports; or temporarily increase pool **and** stop deploying parent-wait pattern (still racy).
- **Proper:** do not block pool threads on same-pool futures — use a separate completion pool, non-blocking composition, or structured concurrency with clear scheduler separation ([concurrency/](../concurrency/)).
- Pipeline: parent accepted on HTTP / VT; CPU chunks on a bounded pool; no sync wait on that pool’s own queue.

## Prevention

- Queue-depth and oldest-job-age SLOs on health/readiness
- Arch unit test / SpotBugs-style rule: forbid `get`/`join` on tasks from the current executor
- Load test nested async under pool-size == parallel parents
- Document executor topology in a runbook diagram

## Principal Engineer Discussion

- “One shared executor for the app” — convenience vs coupling. What’s your default in a large service?
- Structured concurrency (JDK) as a readability and cancellation fix — does it solve pool sizing?
- Readiness vs liveness: should a backed-up export queue fail readiness?
- Trade-off: larger pools mask dependency bugs until a worse outage — how do you keep pools small **and** safe?

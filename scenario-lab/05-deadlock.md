# Deadlock — inventory reserve vs pricing lock order

## Incident

During a flash sale, a fraction of `checkout` requests hang until Tomcat’s stuck-thread detector fires (≈600 s). Other requests succeed. CPU is low. DB looks fine. Two hot code paths touch inventory and pricing services in-process with synchronized sections / ReentrantLocks introduced for “consistency.”

## Symptoms

- Intermittent hard hangs (not slow — stuck)
- Thread dump mentions `Java-level deadlock` (sometimes) or cycles in `jcmd Thread.print`
- Error rate spikes only when both code paths overlap under load
- Restart clears hangs temporarily
- No GC storm; heap stable

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| QPS | Flash sale ~3k RPS peak (illustrative) |
| Sync | Mix of `synchronized` on domain objects + `ReentrantLock` |
| Topology | Single JVM process; inventory + pricing modules co-located |

## Metrics

```
http.inflight            climbs on hung workers
thread.timed_waiting     up
cpu                      low during hang windows
db.pool.active           normal
lock.contention (JFR)    spikes when available
```

Illustrative: low CPU + stuck inflight ⇒ wait/block, investigate locks/IO.

## Logs

```
2026-08-03T18:44:01.002Z WARN  org.apache.catalina.valves.StuckThreadDetectionValve - Thread "http-nio-8080-exec-11" has been a stuck thread for 612 seconds
2026-08-03T18:44:01.015Z WARN  StuckThread - stack includes InventoryService.reserve and PricingService.quote
2026-08-03T18:45:12.440Z ERROR watchdog - possible deadlock detected by ThreadMXBean.findDeadlockedThreads count=4
```

## Initial Hypotheses

1. Classic lock-order deadlock (A then B vs B then A)
2. Nested locks with try/lock timeout missing → apparent deadlock
3. Database row-lock deadlock surfaced as Java waits (check stacks)
4. Carrier pinning oddity with virtual threads (only if VT + synchronized)
5. Waiting on external resource mislabeled deadlock

## Questions

- What’s the difference between “deadlock” and “everyone blocked on one slow lock”?
- Why take **two** dumps / use `findDeadlockedThreads` instead of reading one stack?
- What assumption does “we only lock for a few lines” make under flash-sale concurrency?
- What if traffic ×100 — more deadlocks or same cycle more often?
- How do lock-ordering strategies interact with module boundaries? ([concurrency/](../concurrency/))

## Investigation

1. **Thread dump + deadlock detector**  
   `jcmd <pid> Thread.print`  
   Or programmatically / via JMX: `ThreadMXBean.findDeadlockedThreads`. Look for the JVM’s deadlock section.

2. **Draw the wait-for graph**  
   Thread-1: holds `PricingLock`, waits `InventoryLock`  
   Thread-2: holds `InventoryLock`, waits `PricingLock`  
   Confirm cycle.

3. **Code audit of lock order**  
   Path `/reserveThenPrice` vs `/priceThenReserve` (or cart update vs quote). Document acquisition order.

4. **JFR Java Monitor / Capsule**  
   Contended monitors, park events — supports the story for postmortem ([performance-engineering/tools/java-flight-recorder.md](../performance-engineering/tools/java-flight-recorder.md)).

5. **Exclude DB deadlock**  
   Stacks in `socketRead` / JDBC vs `waiting to lock <0x...>`. DB deadlocks usually abort one transaction — different logs.

6. **VT angle**  
   If virtual threads: on Java 25, `synchronized` no longer pins the old way (JEP 491) — the **cycle** is still a lock-order bug; lock-over-I/O remains bad design ([virtual-threads/thread-pinning.md](../virtual-threads/thread-pinning.md)).

7. **Reproduce**  
   Two-thread unit test acquiring locks in opposite orders with a barrier — should hang; fix should pass.

## Root Cause

Inventory reservation locks **sku → price-list**, while the pricing quote path locks **price-list → sku** under concurrent flash-sale updates. Opposite lock orders create a deadlock cycle under overlap. Stuck-thread valve surfaces the hang; `findDeadlockedThreads` confirms.

## Resolution

- **Immediate:** rolling restart to break cycles; shed load; feature-flag one path to single-lock or lock-free read.
- **Fix:** global lock order (e.g., always `skuId` then `priceListId`); or one striped lock; or remove cross-module synchronized in favor of DB transactions / optimistic versioning.
- Prefer `tryLock(timeout)` with abort/retry **only** as a safety net — not a substitute for ordering.

## Prevention

- Concurrency tests for cross-module lock acquisition
- Deadlock detection alert via `ThreadMXBean` periodically in staging
- Code owners: “no synchronized on shared domain objects across modules” without an order registry
- Prefer immutable pricing snapshots over live dual locks

## Principal Engineer Discussion

- Fine-grained locks vs one coarser lock vs DB as source of truth — latency, complexity, correctness.
- Should in-process mutexes exist at all for inventory in a multi-instance deployment? (They don’t protect across pods.)
- Timeouts and compensation: user-visible failures vs silent hangs — which is worse for checkout?
- How do you review PRs for lock order systematically?

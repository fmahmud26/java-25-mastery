# Race Condition — coupon residual goes negative

## Incident

Promo service allows “apply coupon” and “release coupon” from checkout and cancel flows. Under load tests and occasionally in prod, `remainingUses` goes **negative**, or two checkouts both receive the last single-use coupon. Code uses a check-then-act on a `ConcurrentHashMap` without atomic update. JDK 25 service; developers assumed CHM made operations “thread-safe enough.”

## Symptoms

- Intermittent oversell of single-use / limited coupons
- Metrics: `coupon.applied` > `coupon.budget` for some campaign ids
- Hard to reproduce with one thread; easy with parallel JMeter
- No exceptions — logical corruption
- Audit log shows two applies with nearly identical timestamps

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** |
| State | In-memory `ConcurrentHashMap<CouponId, Integer> remaining` + async DB flush |
| QPS | Checkout apply ~500 RPS peak (illustrative) |
| Intended rule | Never apply if `remaining == 0` |

## Metrics

```
coupon.remaining.min          -3 (should be ≥ 0)
coupon.oversell.count         17 in 10m window
check.vs.act.gap (custom)     applies where remaining read was 1
```

Illustrative: negative remaining is a strong correctness smell for lost updates.

## Logs

```
2026-08-03T12:00:01.100Z INFO  CouponService - check remaining=1 coupon=C-100
2026-08-03T12:00:01.101Z INFO  CouponService - check remaining=1 coupon=C-100
2026-08-03T12:00:01.102Z INFO  CouponService - apply ok coupon=C-100 order=O-1 remainingNow=0
2026-08-03T12:00:01.102Z INFO  CouponService - apply ok coupon=C-100 order=O-2 remainingNow=-1
```

## Initial Hypotheses

1. Check-then-act race on `get` + `put` despite `ConcurrentHashMap`
2. Two app instances without shared coordination (in-memory only)
3. DB read committed race on flush path
4. Integer overflow (unlikely at these magnitudes)
5. Clock skew in logs only — actual bug elsewhere

## Questions

- What does “thread-safe collection” guarantee — and **not** guarantee? ([collections/](../collections/), [concurrency/concurrenthashmap.md](../concurrency/concurrenthashmap.md))
- What would you try first: code inspection, jcstress, or prod metrics?
- What assumption fails when you scale from 1 pod to 10?
- What if traffic ×100 — more oversells linearly?
- AtomicInteger / `compute` / DB constraint — which layer is source of truth?

## Investigation

1. **Read the critical section**  
   Pseudocode smell: `if (map.get(id) > 0) map.put(id, map.get(id) - 1)`.

2. **Reproduce**  
   Parallel applies in a unit test / JMH race harness; assert never negative.

3. **Multi-instance**  
   If map is per-JVM, two pods each allow the “last” use — distributed race.

4. **DB layer**  
   Check whether `UPDATE … SET remaining = remaining - 1 WHERE remaining > 0` exists; row count 0 means reject.

5. **jcstress / stress**  
   Confirm Java Memory Model issue is ordinary race, not exotic visibility-only.

6. **Telemetry**  
   Log compare-and-set failures once fixed.

7. **Heap/stack**  
   Not needed for this class of bug — don’t waste time on JFR CPU first.

## Root Cause

`ConcurrentHashMap` makes individual operations atomic, **not** composite check-then-act sequences. Two threads read `remaining=1`, both apply, both write — oversell / negative. Separately, in-memory state per pod lacks a cluster-wide atomic decrement, so horizontal scale multiplies the race.

## Resolution

- Use `map.compute(id, (k, v) -> …)` / `computeIfPresent` with abort logic, or `AtomicInteger` updates.
- Source of truth: transactional SQL / Redis `DECR` with checks / Lua; enforce `CHECK (remaining >= 0)` or conditional update.
- Reject apply when conditional update matches 0 rows.

## Prevention

- Code review: forbid get+put conditionals on shared counters
- Property tests under parallel load for coupons
- Centralize inventory-like counters in a store with atomic ops
- Educate: CHM ≠ business-level atomicity ([concurrency/](../concurrency/))

## Principal Engineer Discussion

- Where should limited coupons live for a global flash sale?
- Strong consistency vs availability for promo abuse — what’s acceptable fraud rate?
- Compare synchronized, striped locks, atomics, and DB constraints for this use case.
- How do you explain this race on a whiteboard in 5 minutes to an interviewer?

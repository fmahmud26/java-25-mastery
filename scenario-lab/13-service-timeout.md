# Service Timeout — cascading failures from a small dependency

## Incident

User login and browsing fail with 504/timeouts across **multiple** services, not just `auth-service`. The originating issue is a slow LDAP/IdP dependency behind auth. Gateway timeouts are 3 s; auth’s outbound timeout to IdP is 10 s. Retries are aggressive. Thread pools on callers fill with waits. A “tiny” dependency takes down the mesh’s perceived availability.

## Symptoms

- Wide blast radius: BFF, checkout, and mobile API all time out
- Auth error/latency first; then caller pools saturate
- Retry amplification visible in outbound RPS to auth/IdP
- Circuit breakers absent or half-open flapping
- CPU low everywhere; waiting dominates

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** services |
| Gateway timeout | 3 s |
| Auth → IdP timeout | 10 s (illustrative mismatch) |
| Retries | 3× with minimal backoff on Idempotent GETs **and** some POSTs |
| Pool | Platform threads 200 on each caller |

## Metrics

```
idp.latency.p99              8s
auth.http.p99                9s+
bff.timeouts                 surge
auth.inbound.rps             ×3 from retries
caller.pool.active           maxed
circuit_breaker.state         CLOSED (not configured) 
```

Illustrative: timeout mismatch + retries + shared pools ⇒ cascade.

## Logs

```
2026-08-03T15:22:01.002Z WARN  AuthClient - IdP slow elapsed=9022ms
2026-08-03T15:22:01.010Z WARN  Bff - Auth call timeout after 3000ms (attempt 1/3)
2026-08-03T15:22:04.020Z WARN  Bff - Auth call timeout after 3000ms (attempt 2/3)
2026-08-03T15:22:07.030Z ERROR Bff - Auth failed after retries; returning 504
2026-08-03T15:22:07.040Z WARN  Checkout - profile enrichment timeout; aborting cart
```

## Initial Hypotheses

1. Timeout antipattern: caller timeout < callee’s outbound wait ⇒ wasted work + pool hold
2. Retry storm amplifying load on sick dependency
3. Missing bulkhead — auth slowness occupies threads needed for unrelated work
4. IdP outage/slowdown as root trigger
5. DNS/network partition (check; usually narrower)

## Questions

- Why do unrelated endpoints fail when auth is slow?
- What would you fix first in the war room: IdP, retries, or pools?
- What assumption does “retry three times” make about dependency health?
- What if traffic ×100 during IdP slowness?
- How do deadlines propagate in Java HTTP clients? ([system-design/](../system-design/), scenario 07)

## Investigation

1. **Blast radius map**  
   Which services time out; dependency graph from traces.

2. **Timeout matrix**  
   Table of every hop’s deadline — find inversions (3 s vs 10 s).

3. **Retry metrics**  
   Outbound attempts / success; amplification factor.

4. **Thread dumps on BFF/checkout**  
   Stacks stuck in auth client — pool starvation for other routes ([performance-engineering/thread-analysis.md](../performance-engineering/thread-analysis.md)).

5. **IdP health**  
   Confirm root trigger; separate SEV owners.

6. **Circuit breaker / bulkhead state**  
   Absent ≠ closed.

7. **Reproduce**  
   Toxiproxy delay on IdP in staging; watch cascade with/without retries.

## Root Cause

IdP latency rose. Auth waited up to 10 s. Callers timed out at 3 s and **retried**, holding their own threads while auth still worked on abandoned requests. Without bulkheads, auth-waiting threads exhausted pools used by unrelated handlers → **cascading timeouts** far beyond auth. The trigger was IdP; the amplifier was timeout/retry/pool design.

## Resolution

- **Immediate:** disable retries to auth; fail fast; open circuit; shed noncritical traffic; scale/fix IdP; restart only if pools wedged.
- **Fix:** hierarchical deadlines (caller budget ≥ sum of children); retry only with jitter/backoff on safe ops; bulkhead auth; cache sessions to reduce IdP; stale login fallback where policy allows.
- Align gateway and service timeouts deliberately.

## Prevention

- Timeout policy as code-reviewed config
- Chaos days: dependency delay tests
- Alerts on retry amplification ratios
- Separate thread pools / VT + semaphores per dependency

## Principal Engineer Discussion

- Resilience4j / custom bulkheads — operational complexity vs safety.
- Should login failure take down browsing of cached catalogs?
- Deadline propagation standards across a Java org.
- SEV taxonomy: trigger vs amplifier ownership in postmortems.

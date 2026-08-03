# Production Outage — multi-factor collapse of checkout

## Incident

SEV-1: checkout unavailable in `region-eu` for 26 minutes. Symptom soup: gateway 5xx, “DB pool exhausted,” elevated GC, Kafka lag on payment events, and a deploy of `orders-api` mid-incident. Executives want a single root cause. Principal engineers know outages are usually **chains**. This lab trains you to untangle trigger, amplifiers, and bad recovery.

## Symptoms

- Checkout success rate 99.9% → ~12%
- `orders-api` Hikari timeouts; pod CPU high; GC pauses up
- Payment consumer lag spikes; duplicate risk window
- Autoscaler adds pods → more connections to Postgres → DB CPU cliffs
- Rollback attempted; partial improvement only until traffic shed

## Environment

| Item | Value (illustrative) |
|------|----------------------|
| JDK | **25** on `orders-api`, VT enabled on web tier |
| DB | Postgres `max_connections=300`; 4 services share (illustrative) |
| Deploy | `orders-api` 5.14.2 — “saves price snapshot JSON per line item” |
| Cache | Price cache TTL bug aligned to deploy warm |
| Kafka | payments topic; consumers scaled automatically |

## Metrics (compressed timeline)

```
T+0m   deploy complete; alloc rate ↑ ; young GC time ↑
T+3m   cache hit ratio cliff (stampede-like) ; DB QPS ↑
T+6m   hikari timeouts ; http success ↓
T+8m   HPA scales 8→40 pods ; db connections → max ; postgres CPU 100%
T+12m  payment lag ↑ ; rebalances
T+18m  manual traffic shed 80% ; DB recovers
T+26m  success rate restored with feature flag off + scale capped
```

Illustrative composite — practice narrative control under ambiguity.

## Logs

```
2026-08-03T19:01:02Z INFO  deploy - orders-api 5.14.2 rolled out eu
2026-08-03T19:04:10Z WARN  PricingCache - mass miss cohort=prime
2026-08-03T19:07:44Z ERROR HikariPool - Connection is not available ...
2026-08-03T19:09:01Z WARN  HPA - scaling orders-api to 40
2026-08-03T19:09:20Z ERROR postgres - remaining connection slots reserved for SUPERUSER
2026-08-03T19:12:11Z WARN  PaymentConsumer - max.poll.interval exceeded; leave group
2026-08-03T19:20:00Z INFO  incident - traffic shed 80%; feature flag priceSnapshot=off
```

## Initial Hypotheses

1. Bad deploy (allocation-heavy price snapshot) as **trigger**
2. Cache stampede amplifier
3. VT + small pools amplifier under miss storm
4. HPA / connection math turning a brownout into hard outage
5. Kafka payment path failure as separate simultaneous cause (maybe secondary)

Do not collapse to one slogan early — rank trigger vs amplifiers.

## Questions

- What do you do in the first five minutes: rollback, scale up, or shed load?
- What assumption does HPA make that failed here?
- How do you communicate “multiple causes” without sounding evasive?
- What if Black Friday traffic ×100 with the same bug?
- Which scenarios in this lab compose into this outage? (03, 06, 11, 12, 10, 08)

## Investigation

Work like an incident commander who also knows JVMs.

1. **Stabilize first**  
   Shed traffic / disable new feature flag / cap replicas — stop the bleeding before perfect RCA.

2. **Timeline**  
   Deploy, metrics cliffs, scaler actions — order matters.

3. **orders-api JVM**  
   JFR/GC logs: allocation from snapshot JSON (scenario 03 pattern).  
   Tools: [performance-engineering/tools/](../performance-engineering/tools/).

4. **Cache**  
   Hit ratio cliff → stampede into DB (scenario 12).

5. **Pool + VT**  
   Concurrent requests × pods > DB connections (scenarios 06, 11).

6. **Autoscaler**  
   More pods × pool size ⇒ `max_connections` exhaustion — meta-amplifier.

7. **Kafka**  
   Lag/rebalance as **consequence** of DB/checkout failure delaying commits — validate not independent trigger (scenario 10); watch duplicates (scenario 08).

8. **Postmortem evidence pack**  
   GC snippets, Hikari graphs, Postgres connection storms, HPA events, flag state.

Related: [principal-engineer/](../principal-engineer/), [DEEP_LEARNING_STANDARD.md](../DEEP_LEARNING_STANDARD.md).

## Root Cause

**Trigger:** 5.14.2 wrote large ephemeral price-snapshot graphs per line item (alloc/GC pressure) and interacted with a cache cohort expiry to miss-storm the DB.  
**Amplifiers:** virtual threads admitted huge concurrency into a small JDBC pool; HPA scaled pod count, multiplying pools until Postgres refused connections; checkout threads blocked → upstream timeouts; payment consumers fell behind and rebalanced.  
**Not a single bug** — a deploy defect plus cache TTL alignment plus pool/VT/HPA coupling. Rollback alone was insufficient until traffic shed and replica caps restored connection headroom.

## Resolution

- **War room:** feature flag off; cap HPA; shed load; protect Postgres; pause noncritical consumers; communicate ETA.
- **Code:** remove heavy snapshot from request path; jitter TTLs; bound concurrent DB users under VT; set global connection budgets (`pods × pool ≤ budget`).
- **Verify:** gradual traffic restore with error budgets; watch duplicates/refunds on payments.

## Prevention

- Connection budget alerts across the fleet
- HPA policies that consider downstream saturation (not only CPU)
- Deploy-time load test with production cache expiry behavior
- Feature flags on allocation-heavy changes
- Game days composing stampede + pool exhaustion
- Runbooks that say **shed before scale** when DB connections are the scarce resource

## Principal Engineer Discussion

- How do you write the public RCA without blaming one engineer?
- Organizational fix: who owns cross-service connection budgets?
- “Scale up fixes outages” — when is that false in JVM/data systems?
- Design a checkout architecture that fails partial (cart readable, pay deferred) rather than hard-down.
- Map each amplifier to a concrete control: semaphore, TTL jitter, HPA max, circuit breaker, flag.
- Interview prompt: *Walk me through this timeline as incident commander, then as JVM engineer.*

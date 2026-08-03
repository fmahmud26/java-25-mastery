# Availability

**Definition:** fraction of time the system successfully serves requests (usually measured at the **edge** the user hits).

## Math interviewers expect

| Target | Downtime / year |
|--------|-----------------|
| 99% | ~3.65 days |
| 99.9% | ~8.8 hours |
| 99.99% | ~52 minutes |
| 99.999% | ~5 minutes |

Availability of serial dependencies multiplies: `A = A1 × A2 × A3`. Three 99.9% deps → ~99.7% if any failure fails the request.

**Principal move:** remove critical path deps (cache, degrade, async) or add redundancy.

## Redundancy patterns

| Pattern | Why | Cost |
|---------|-----|------|
| Multi-AZ | Survive AZ loss | Cross-AZ latency/$$ |
| Active-passive | Simple failover | RTO during promote |
| Active-active | Lower RTO | Conflict/consistency hard |
| N+1 / N+2 capacity | Survive instance loss under peak | Idle spend |

## Planned vs unplanned

Deploys, schema migrations, certificate rotations count against users unless blue/green or dual-write carefully. PE designs **change** as carefully as runtime.

## Degraded availability (often better than binary down)

- Read-only mode if writer primary dies  
- Serve stale cache if origin unhealthy  
- Disable non-critical features (recommendations) under load  

State the **degradation contract** in the interview.

## Measuring

SLI: success rate of critical endpoints (exclude client 4xx if appropriate). SLO with error budget. Alert on burn rate, not single blips.

Related: [reliability.md](./reliability.md), [disaster-recovery.md](./disaster-recovery.md), [load-balancing.md](./load-balancing.md).

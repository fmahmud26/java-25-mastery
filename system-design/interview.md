# System Design Interview — Principal Rubric

## Opening (60–90s)

“I’ll clarify functional scope, non-goals, traffic shape, latency SLOs, consistency needs, and regions. Then capacity estimates, a layered architecture, deep-dive the hardest invariant—usually data or money—walk failure modes with retries and backpressure, cover observability and DR, and sketch how the system evolves at 10×.”

## Scoring

| Signal | Strong | Weak |
|--------|--------|------|
| Clarification | QPS, size, SLO, consistency, region, non-goals | Jumps to Kafka |
| Capacity | Order-of-magnitude storage/QPS/bandwidth | No numbers |
| Architecture | Tiered; sync vs async justified | Flat buzzword pile |
| Data | Access patterns → schema/partition key | One giant SQL table forever |
| Consistency | Per-read/write contract | “Strong everywhere” |
| Scale | Bottleneck named + lever | “Add servers” |
| Failure | Timeout, duplicate, partition, dependency down | Happy path only |
| Ops | SLIs, traces, alerts, RPO/RTO | “We’ll monitor” |
| Security | Authn/z, secrets, PII, abuse | Ignored |
| Evolution | Phased; what breaks first at 10× | Final boss architecture day one |

## 50-minute timeline

| Min | Move |
|-----|------|
| 0–5 | Clarify + write SLOs |
| 5–12 | Capacity estimation aloud |
| 12–22 | Architecture + components + primary data flow |
| 22–35 | Deep dive (shard / cache / money / exactly-once) |
| 35–42 | Failure modes + retries/CB/queues |
| 42–48 | Observability, security, DR |
| 48–50 | Evolution / follow-ups |

## Capacity checklist (always)

```text
DAU / QPS avg / QPS peak (×5–20)
Read:write ratio
Payload size → bandwidth
Record size × growth → storage + indexes (~2–3×)
Cache hit target → miss QPS to DB
Queue depth under dependency outage (sustain minutes × QPS)
```

## Phrase bank (Principal)

- “Stateless edge; durable state in DB/queue/object store.”  
- “Partition by X because query pattern is Y; hot key mitigation is Z.”  
- “Cache-aside with TTL T; invalidation via event, accept brief staleness.”  
- “Provider timeout → pending + reconcile; never blind re-charge.”  
- “At-least-once delivery; idempotent consumers keyed by business id.”  
- “Outbox/CDC to avoid dual-write between DB and Kafka.”  
- “p99 budget: edge 5ms, service 20ms, dependency 50ms — miss path separate SLO.”  
- “Fail-closed on auth rate limits; fail-open on best-effort telemetry.”  
- “RPO minutes via async replica; RTO via runbook + multi-AZ.”  

## Anti-patterns

- Designing multi-region active-active before single-region correctness  
- Exactly-once as a slogan without a mechanism  
- Caching without stampede or invalidation story  
- Retry storms without jitter/CB/budgets  
- Ignoring poison messages and DLQ  

Related: [README.md](./README.md), [java-focused.md](./java-focused.md).

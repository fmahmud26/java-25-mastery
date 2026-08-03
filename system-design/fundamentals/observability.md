# Observability

Know what the system is doing **without SSH archaeology**. Three pillars + continuous profiling culture.

## Pillars

| Pillar | Answers | Examples |
|--------|---------|----------|
| Metrics | Is it healthy / trending? | QPS, p99, error rate, lag, saturation |
| Logs | What happened on this request? | Structured JSON with `requestId` |
| Traces | Where did time go across services? | OpenTelemetry spans |

## Golden signals (and USE/RED)

- **RED:** Rate, Errors, Duration per service  
- **USE:** Utilization, Saturation, Errors per resource  
- Business SLIs: checkout success, payment capture rate  

## Alerting

Alert on **SLO burn** and customer impact, not every CPU blip. Page humans for actionable symptoms; ticket for capacity trends.

## Correlation

Propagate `traceparent` / `x-request-id` across HTTP and Kafka headers. Without this, microservices are un-debuggable.

## Capacity & dependency dashboards

Queue lag, replica lag, cache hit ratio, CB state, thread/DB pool wait — these predict outages.

## Interview expectation

Name 5–7 metrics for your design and one trace path for the critical user journey.

Related: [latency.md](./latency.md), [reliability.md](./reliability.md), [disaster-recovery.md](./disaster-recovery.md).

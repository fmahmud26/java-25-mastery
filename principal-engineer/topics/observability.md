# Observability (PE lens)

Observability is how you **budget attention**: what is measured, who owns dashboards, what pages humans.

## PE obligations

- Critical journeys have RED + business SLI  
- Trace propagation mandatory across HTTP/Kafka  
- Dependency SLIs visible to callers  
- Cost of telemetry controlled (sampling, index tiers)  
- JVM signals owned for latency-sensitive services (GC, alloc, pools) — see [jvm-observability](../../performance-engineering/jvm-observability.md)  

## Design reviews ask

1. How do we know this is broken in 5 minutes?  
2. How do we know *which* dependency?  
3. What alert fires — and what does the human do?  
4. If p99 spikes with flat error rate — which JVM view do we open first?  

## Failure mode

Metrics without owners → alert fatigue → silent reliability decline.

## Principal stance

Treat continuous profiling and GC/heap SLIs as production features with budgets, not optional “platform nice-to-haves.”

Related: [../system-design/fundamentals/observability.md](../../system-design/fundamentals/observability.md), [incident-management.md](./incident-management.md), [../../performance-engineering/jvm-observability.md](../../performance-engineering/jvm-observability.md).

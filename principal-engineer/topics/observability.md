# Observability (PE lens)

Observability is how you **budget attention**: what is measured, who owns dashboards, what pages humans.

## PE obligations

- Critical journeys have RED + business SLI  
- Trace propagation mandatory across HTTP/Kafka  
- Dependency SLIs visible to callers  
- Cost of telemetry controlled (sampling, index tiers)  

## Design reviews ask

1. How do we know this is broken in 5 minutes?  
2. How do we know *which* dependency?  
3. What alert fires — and what does the human do?  

## Failure mode

Metrics without owners → alert fatigue → silent reliability decline.

Related: [../system-design/fundamentals/observability.md](../../system-design/fundamentals/observability.md), [incident-management.md](./incident-management.md).

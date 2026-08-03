# Observability

## Purpose

Know what the system is doing in production: **metrics, logs, traces** (and profiles when needed).

## Before

“It feels slow” — no per-dependency latency, no trace id, only INFO spam.

## After

```text
RED metrics per endpoint: Rate, Errors, Duration
Per dependency: payment_client_latency_p99, timeouts, retries
Traces: orderId → inventory span → payment span
Logs: correlated with trace_id
```

## Engineering hooks

- OpenTelemetry / Micrometer instrumentation at edges  
- Exemplars linking metrics → traces  
- SLOs define what to measure  

## Trade-offs

Cardinality (userId as label) can melt Prometheus. High sampling in prod costs CPU.

## PE Decision

Observability is part of the **definition of done** for new services — not a follow-up ticket.

### Related

[logging.md](./logging.md) · [error-handling.md](./error-handling.md) · [principal-decisions.md](./principal-decisions.md)

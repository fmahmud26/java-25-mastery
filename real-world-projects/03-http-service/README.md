# 03 — Resilient HTTP Service

**Tier:** Edge service · **Demonstrates:** networking, virtual threads, resilience, observability

## Problem

Expose a high-concurrency HTTP API that remains stable when handlers are slow and clients retry — with timeouts, metrics, and structured logs so you can explain p99 behavior in an interview.

## Requirements

**Functional**
- Endpoints: `/health`, `/echo`, `/work?ms=`, `/metrics`  
- Load client: concurrent requests with timeouts + retries  

**Non-functional**
- Server uses VT executor  
- Client: connect + request timeouts; bounded retries + backoff  
- Atomic metrics: requests, errors, retries  
- No unbounded work acceptance without visibility  

## Architecture

```text
LoadClient (HttpClient + retry policy)
    ↓
DemoHttpServer (HttpServer + VT executor)
    → handlers
    → Metrics (atomics)
    → ServiceLog (stderr JSON-ish)
```

## Technology choices

| Choice | Why | Rejected |
|--------|-----|----------|
| `com.sun.net.httpserver` | Zero-dep teaching server | Full Spring — hide the mechanics |
| `java.net.http.HttpClient` | Modern timeouts | Legacy `HttpURLConnection` |
| VT executor | Cheap blocking handlers | Fixed 200 platform threads |
| Manual metrics | Teach RED-ish counters | Micrometer until project 06+ |

## Design decisions

- **Timeouts on client** — server alone cannot save you.  
- **Retries only on idle/connect/5xx policy** — not on all errors.  
- **`/work`** simulates dependency latency for demos.  
- Metrics endpoint for live discussion during interview.

## Implementation plan

1. Metrics + logging  
2. Server + VT executor binding  
3. Handlers  
4. Client with retry/backoff  
5. Driver CLI flags: port, clients, requests  

## Failure scenarios

| Failure | Behavior |
|---------|----------|
| Handler sleep > client timeout | Client fails/retries per policy; server still VT-healthy |
| Connection refused | Retry then fail; metric error++ |
| Burst load | Observe latency via `/work`; discuss queueing |

## Testing strategy

- Unit: retry policy attempts on injected failures  
- Integration: start server on ephemeral port; hit `/health`  
- Chaos: `/work?ms=5000` vs client timeout 100ms → assert fail fast  
- Concurrency: 500 clients smoke without thread explosion  

## Performance considerations

- VT ≠ unlimited DB connections — next projects bind pools  
- Logging per request can dominate — sample under load  
- Compare platform executor (flag) in lab 05  

## Scaling strategy

- Add LB + multiple processes; externalize metrics (OTLP) in later projects  
- Rate limit at edge (project 06)  

## Interview discussion

“JDK HttpServer on virtual threads, HttpClient with deadlines and jittered retries, and atomic RED-style metrics. I’d replace the server with Jetty/Netty in production but the resilience principles stay.”

**Follow-ups:** Where do retries amplify outages? How do you propagate deadlines?

## Run

```bash
chmod +x run.sh && ./run.sh
./run.sh --port 18080 --clients 100 --requests 20
```

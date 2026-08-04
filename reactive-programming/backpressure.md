# Backpressure in Reactive Pipelines

**Backpressure** = the consumer controls how fast the producer may emit, so buffers do not grow without bound.

## Mental Model

```text
Slow consumer                    Fast producer
     │ request(8)                     │
     │◄───────────────────────────────┤  may emit ≤ 8
     │ (processing…)                  │ waits / slows / drops per policy
     │ request(8)                     │
```

Reactive Streams encode this as `Subscription.request(n)`. Distributed systems encode it as queue lag, TCP windows, HTTP 429 — same idea, different layer. See [distributed backpressure](../system-design/distributed-systems/backpressure.md).

## Strategies

| Strategy | Behavior | Use when |
|----------|----------|----------|
| Bound + wait | Producer blocks / slows | Cannot lose data; latency may grow |
| Drop / sample | Discard under load | Telemetry, best-effort events |
| Error / cancel | Fail fast upstream | Protect SLOs; force clients to shed |
| Buffer (bounded) | Absorb bursts | Burst < bound; still need overflow policy |
| Buffer (unbounded) | Hide load | **Almost never** in production |

Reactor-style names you will hear: `onBackpressureBuffer`, `onBackpressureDrop`, `onBackpressureLatest`, `onBackpressureError` — policies, not magic.

## Contrast with Virtual Threads

| Model | Natural backpressure |
|-------|----------------------|
| VT request/response | Thread/pool/queue/DB pool limits — **you** must bound fan-out |
| Reactive stream | Demand credit is first-class in the pipeline |
| Kafka / MQ | Lag + producer limits (system-level) |

VT does **not** invent reactive demand. Unbounded `startVirtualThread` per message with a 20-connection pool is still a stampede.

## Production Scenario — SSE fan-out

10k browser clients subscribe to price ticks. Without demand limits, one slow client forces a huge in-memory buffer per connection → heap pressure → GC tails.

**Fix:** per-subscription request window; drop or conflate ticks for lagging clients; metric on dropped ticks; isolate slow clients.

## Failure Scenario — “we buffered to be safe”

Team sets unbounded `onBackpressureBuffer` “temporarily.” Marketing blast + slow mailer → multi-GB heap → OOM. Root cause is missing backpressure policy, not “GC bug.”

## Debugging

- Metric: queue depth / outstanding demand / drop count  
- Symptom: rising heap with flat CPU → buffered work  
- Symptom: constant 100% CPU drain loop → pathological request/emit churn  

## When Not to Obsess

CRUD APIs returning one JSON body do not need Reactive Streams backpressure. Bound the **thread/pool/connection** instead (often with VT).

## Principal Decision

Ask: *Where does excess work go — wait, drop, or reject — and who owns that policy?* If unanswered, you do not have backpressure; you have a future incident.

### Related

[reactive-streams.md](./reactive-streams.md) · [production-pitfalls.md](./production-pitfalls.md) · [vs-virtual-threads.md](./vs-virtual-threads.md)

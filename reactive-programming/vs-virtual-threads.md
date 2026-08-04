# Reactive vs Virtual Threads (Canonical Decision)

This is the **canonical** architecture comparison. Short VT-side notes live in [virtual-threads-vs-reactive-programming.md](../virtual-threads/virtual-threads-vs-reactive-programming.md) and should defer here for depth.

## Why Both Exist

| Pressure | Historical answer | Java 21–25 answer |
|----------|-------------------|-------------------|
| Many blocked requests, few OS threads | Reactive / async I/O | **Virtual threads** for blocking style |
| Streaming + demand control | Reactive Streams | **Still reactive** (or queues + explicit policy) |
| Complex async composition | CF / reactive | CF, structured concurrency (preview), or reactive |

## Decision Matrix

| Concern | Prefer VT | Prefer Reactive |
|---------|-----------|-----------------|
| CRUD + JDBC + HttpClient | Yes | Only if already WebFlux-native |
| Readable code / hiring | Yes | No (steeper) |
| Stack traces / debuggability | Yes | Harder |
| SSE / infinite streams | Possible with care | Natural fit |
| Built-in pipeline backpressure | DIY (pools/queues) | First-class |
| Operator graph (fan-in, window, sample) | Awkward | Natural |
| Team already expert in WebFlux | Maybe keep | Yes — migrate only with ROI |
| CPU-bound work | Platform pool (not VT flood) | `publishOn` CPU pool / parallel |

## Side-by-Side Runtime

```text
VT path:     request → VT blocks on JDBC → unmount → carrier free
Reactive:    request → event-loop does non-blocking I/O → callbacks/operators
             blocking JDBC → must hop to worker pool (or use VT workers)
```

Blocking on a Netty event-loop thread remains a production footgun even in 2026.

## Production Scenario — payments orchestrator

Orchestrator calls PSP + ledger + fraud. Each hop is request/response with timeouts and idempotency.

- **VT:** readable sequential code, structured fan-out (or CF), pool limits on DB/PSP.  
- **Reactive:** workable, but often adds little if there is no streaming backpressure need.

Choose VT unless the org standard is already WebFlux end-to-end.

## Production Scenario — market data fan-out

Ticks to thousands of subscribers with conflation and drop-old policies → reactive (or specialized broker) wins. VT-per-tick without demand control recreates buffer blowups.

## Failure: False Dichotomy

1. Rewrite stable WebFlux to VT “because Loom” → lose operators/backpressure, burn quarters.  
2. Keep reactive wrappers **only** to save platform threads → VT already fixed that for blocking I/O.  

## Migration Guidance

| Situation | Move |
|-----------|------|
| New blocking service | VT-first |
| Stable WebFlux + skilled team | Keep; VT for blocking adapters |
| Streaming edge + CRUD core | Reactive at edge; VT for workers |
| Greenfield streaming platform | Reactive or purpose-built broker |

## Principal Interview Answer (30s)

“Virtual threads optimize the *thread-per-request blocking* model. Reactive optimizes *async streams with backpressure*. I pick based on I/O shape, team skill, and whether demand control is a first-class requirement — not based on fashion.”

### Related

[production-pitfalls.md](./production-pitfalls.md) · [../virtual-threads/when-vt-do-not-help.md](../virtual-threads/when-vt-do-not-help.md) · [../virtual-threads/vt-vs-platform-completablefuture-reactive.md](../virtual-threads/vt-vs-platform-completablefuture-reactive.md)

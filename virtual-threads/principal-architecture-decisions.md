# Principal Engineer Architecture Decisions

Decisions you should own — not “enable VT and hope.”

## Decision 1 — Adopt VT for Blocking Services?

**Adopt when:** I/O-bound request path; platform pool was the concurrency ceiling; stack-friendly sync code preferred over reactive rewrite.

**Defer when:** Stable reactive stack; CPU-bound core; org not ready for pool/admission redesign.

**Record:** ADR with measured bottleneck (thread dumps / metrics).

---

## Decision 2 — Do Not Pool Virtual Threads

Use `newVirtualThreadPerTaskExecutor()` / container VT support. Pooling VTs reintroduces artificial caps and defeats the model.

Pool **what is scarce**: DB connections, HTTP connections, CPU workers.

---

## Decision 3 — Admission Control Survives

Remove platform-thread cap ⇒ add explicit max in-flight, timeouts, dependency bulkheads. VT without admission is a stampede machine.

```text
edge maxInFlight ≤ f(memory, dep capacity)
per-dep Semaphore ≤ rate limit / pool
```

---

## Decision 4 — Split CPU and I/O

| Work | Executor |
|------|----------|
| Request I/O | Virtual threads |
| Heavy CPU (crypto, compression, ML) | Sized platform pool |
| Scheduler / carriers | Leave to JVM |

---

## Decision 5 — Pool Math Across the Fleet

```text
Σ (pods × hikari.max) + admin < db.max_connections
HTTP client max connections per host tuned
Timeouts < upstream SLA
```

VT does not change this spreadsheet — it makes wrong math fail faster under load.

---

## Decision 6 — Observability

Track: VT count / carriers, pool wait time, dep latency, in-flight, pinning (legacy), GC, RSS. Thread dumps still work; many VTs — filter carefully.

---

## Decision 7 — Migration Path

1. Measure thread wait vs dep saturation  
2. Enable VT in one service behind flag  
3. Keep pool sizes; add bulkheads if concurrency spikes  
4. Compare p99, error rate, RSS, dep RPS  
5. Roll or revert with ADR  

---

## Decision 8 — Framework Choice

| Path | Choice |
|------|--------|
| New blocking Spring MVC / Jakarta | VT |
| Existing WebFlux healthy | Keep |
| Mixed monolith | VT for blocking modules; don’t force one paradigm |

---

## Anti-Decisions

- “Raise Hikari to 5000 because VT”  
- “Rewrite all CF/reactive to VT in one quarter”  
- “Disable timeouts; VT will scale”  
- “One giant synchronized around business + JDBC”  

### Related

[scenarios.md](./scenarios.md) · [vt-vs-platform-completablefuture-reactive.md](./vt-vs-platform-completablefuture-reactive.md) · [when-vt-do-not-help.md](./when-vt-do-not-help.md)
